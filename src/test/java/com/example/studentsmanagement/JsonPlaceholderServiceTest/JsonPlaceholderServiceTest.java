package com.example.studentsmanagement.JsonPlaceholderServiceTest;

import com.example.studentsmanagement.Client.JsonPlaceholderClient;
import com.example.studentsmanagement.Model.Response.ApiResponse;
import com.example.studentsmanagement.Model.Response.JsonPlaceholderResponse;
import com.example.studentsmanagement.Service.JsonPlaceholderService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JsonPlaceholderServiceTest {

    @MockitoBean
    private JsonPlaceholderClient jsonPlaceholderClient;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private JsonPlaceholderService jsonPlaceholderService;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setup() {
        reset(jsonPlaceholderClient);
        circuitBreakerRegistry.circuitBreaker("jsonPlaceholderClient").reset();
        cacheManager.getCache("users").clear();
    }

    @Test
    @Order(1)
    void shouldRetryAndEventuallySucceed() {

        // Arrange
        when(jsonPlaceholderClient.getUsers())
                .thenThrow(new RuntimeException("fail : 1"))
                .thenThrow(new RuntimeException("fail : 2"))
                .thenThrow(new RuntimeException("fail : 3"))
                .thenThrow(new RuntimeException("fail : 4"))
                .thenReturn(List.of());

        // Act
        ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

        // Assert
        assertThat(result.success()).isTrue();
        verify(jsonPlaceholderClient, atLeast(5)).getUsers();
    }

    @Test
    @Order(2)
    void shouldCallFallbackWhenAllRetriesFail() {

        // Arrange
        when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("fail"));

        // Act
        ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

        // Assert
        assertThat(result.success()).isFalse();
        assertThat(result.statusCode()).isEqualTo(504);
        verify(jsonPlaceholderClient, atLeast(5)).getUsers();
    }

    @Test
    @Order(3)
    void shouldStayClosedWhenAllCallsSucceed() {

        // Arrange
        when(jsonPlaceholderClient.getUsers()).thenReturn(List.of());
        CircuitBreaker result = circuitBreakerRegistry.circuitBreaker("jsonPlaceholderClient");

        // Act
        for (int i = 0; i < 5; i++) {
            jsonPlaceholderService.getUsers().join();
        }

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getState()).isEqualTo(CircuitBreaker.State.CLOSED);
    }

    @Test
    @Order(4)
    void shouldReturnFallbackResponseWhenProviderThrowsTimeoutException() {

        // Arrange
        when(jsonPlaceholderClient.getUsers()).thenAnswer(invocation -> {
            throw new TimeoutException("Provider timeout");
        });

        // Act
        ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

        // Assert
        assertThat(result.success()).isFalse();
        assertThat(result.statusCode()).isEqualTo(504);
    }

    @Test
    @Order(5)
    void shouldReturnFallbackResponseWhenServiceFails() {

        // Arrange
        when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("service down"));

        // Act
        ApiResponse<List<JsonPlaceholderResponse>> result = jsonPlaceholderService.getUsers().join();

        // Assert
        assertThat(result.success()).isFalse();
        assertThat(result.statusCode()).isEqualTo(504);
        assertThat(result.message()).isEqualTo("Service temporarily unavailable");
    }

    @Test
    @Order(6)
    void shouldOpenCircuitAfterFailureThreshold() {

        // Arrange
        when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("always fails"));
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("jsonPlaceholderClient");

        // Act
        for (int i = 0; i < 5; i++) {
            jsonPlaceholderService.getUsers().join();
        }

        // Assert
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
    }

    @Test
    @Order(7)
    void shouldTransitionToHalfOpenAfterWaitDuration() throws InterruptedException {

        // Arrange
        when(jsonPlaceholderClient.getUsers()).thenThrow(new RuntimeException("always fails"));
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("jsonPlaceholderClient");

        // Act
        for (int i = 0; i < 5; i++) {
            jsonPlaceholderService.getUsers().join();
        }
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);
        Thread.sleep(3500);
        jsonPlaceholderService.getUsers().join();

        // Assert
        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.HALF_OPEN);
    }
}
