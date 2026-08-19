package com.example.studentsmanagement.JsonPlaceholderServiceTest;

import com.example.studentsmanagement.Client.JsonPlaceholderClient;
import com.example.studentsmanagement.Config.ExecutorConfig;
import com.example.studentsmanagement.Exception.JsonPlaceholderFallbackHandler;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import com.example.studentsmanagement.Service.JsonPlaceholderService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.springboot3.retry.autoconfigure.RetryAutoConfiguration;
import io.github.resilience4j.springboot3.timelimiter.autoconfigure.TimeLimiterAutoConfiguration;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        JsonPlaceholderService.class,
        JsonPlaceholderFallbackHandler.class,
        ExecutorConfig.class
})
@ImportAutoConfiguration(classes = {
        AopAutoConfiguration.class,
        CircuitBreakerAutoConfiguration.class,
        TimeLimiterAutoConfiguration.class,
        RetryAutoConfiguration.class
})
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class JsonPlaceholderServiceTest {

    @MockitoBean private JsonPlaceholderClient jsonPlaceholderClient;
    @MockitoBean private ObjectMapper objectMapper;

    @Autowired private JsonPlaceholderService jsonPlaceholderService;
    @Autowired private CircuitBreakerRegistry circuitBreakerRegistry;
    @Autowired private RetryRegistry retryRegistry;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    void setup() {
        reset(jsonPlaceholderClient);
        circuitBreaker = circuitBreakerRegistry.circuitBreaker("jsonPlaceholderClient");
        circuitBreaker.reset();
    }

    @Nested
    @Order(1)
    class RetryBehaviour {
        @Test
        void doesNotRetryWhenTheFirstCallSucceeds() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenReturn(List.of());

            // Act
            ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

            // Assert
            assertThat(result.success()).isTrue();
            verify(jsonPlaceholderClient, times(1)).getUsers();
        }

        @Test
        void retriesUntilTheCallSucceeds() {
            // Arrange
            when(jsonPlaceholderClient.getUsers())
                    .thenThrow(new RuntimeException("attempt 1"))
                    .thenThrow(new RuntimeException("attempt 2"))
                    .thenReturn(List.of());

            // Act
            ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

            // Assert
            assertThat(result.success()).isTrue();
            verify(jsonPlaceholderClient, times(3)).getUsers();
        }

        @Test
        void stopsAtMaxAttemptsThenFallsBack() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("fail"));

            // Act
            ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

            // Assert
            assertThat(result.success()).isFalse();
            assertThat(result.statusCode()).isEqualTo(504);
            verify(jsonPlaceholderClient, times(3)).getUsers();
        }
    }

    @Nested
    @Order(2)
    class CircuitBreakerBehaviour {
        @Test
        void staysClosedWhileCallsSucceed() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenReturn(List.of());

            // Act
            for (int i = 0; i < 5; i++) {
                jsonPlaceholderService.getUsers().join();
            }

            // Assert
            assertThat(circuitBreaker).isNotNull();
            assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
            verify(jsonPlaceholderClient, times(5)).getUsers();
        }

        @Test
        void opensAfterTheFailureThreshold() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("service down"));

            // Act
            for (int i = 0; i < 5; i++) {
                jsonPlaceholderService.getUsers().join();
            }

            // Assert
            assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        }

        @Test
        void stopsCallingTheProviderWhileOpen() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("service down"));
            for (int i = 0; i < 5; i++) {
                jsonPlaceholderService.getUsers().join();
            }
            assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
            reset(jsonPlaceholderClient);

            // Act
            ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

            // Assert
            assertThat(result.success()).isFalse();
            assertThat(result.statusCode()).isEqualTo(504);
            verify(jsonPlaceholderClient, never()).getUsers();
        }
    }

    @Nested
    @Order(3)
    class TimeLimiterBehaviour {
        @Test
        void fallsBackWhenTakeToLong() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenAnswer(invocation -> {
                Thread.sleep(3000);
                return List.of();
            });

            // Act
            ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

            // Assert
            assertThat(result.success()).isFalse();
            assertThat(result.statusCode()).isEqualTo(504);
        }

        @Test
        void cutsRetriesShortWhenTheTakeToLong() {
            // Arrange
            when(jsonPlaceholderClient.getUsers()).thenAnswer(invocation -> {
                Thread.sleep(400);
                throw new RuntimeException("slow and failing");
            });

            // Act
            ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

            // Assert
            assertThat(result.success()).isFalse();
            assertThat(result.statusCode()).isEqualTo(504);
            assertThat(result.message()).isEqualTo("Service temporarily unavailable");
            verify(jsonPlaceholderClient, atMost(2)).getUsers();
        }
    }
}
