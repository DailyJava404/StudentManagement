package com.example.studentsmanagement.JsonPlaceholderServiceTest;

import com.example.studentsmanagement.Service.Calculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CalculatorTest {

    private Calculator calculator = new Calculator();

    @Test
    void calculateAdd() {
        // Act
        int a = 10;
        int b = 20;

        // Arrange
        int result = calculator.add(a, b);

        // Assert
        assertThat(result).isEqualTo(30);

    }

    @Test
    void calculateDivide() {
        // Act
        int a = 10;
        int b = 2;

        // Arrange
        int add = calculator.divide(a, b);

        // Assert
        assertThat(add).isEqualTo(5);
    }
}
