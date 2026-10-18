package com.gautamkappagal.calculator;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class CalculatorServiceTest {

    private final CalculatorService service = new CalculatorService(); // no Spring needed

    private static BigDecimal bd(String s) { return new BigDecimal(s); }

    @Test
    void addAddsTwoNumbers() {
        assertEquals(0, bd("7").compareTo(service.add(bd("5"), bd("2"))));  // compareTo, not equals
    }

    @Test
    void divideByZeroThrows() {
        BigDecimal a = bd("1"), b = bd("0");
        assertThrows(DivisionByZeroException.class, () -> service.divide(a, b));
    }

    @Test
void divideByDecimalZeroThrows() {
    BigDecimal a = bd("1"), b = bd("0.0");
    assertThrows(DivisionByZeroException.class, () -> service.divide(a, b));
}

    @Test
    void subtractTwoNumbers() {
        assertEquals(0, bd("3").compareTo(service.subtract(bd("5"), bd("2"))));
    }

    @Test
    void multiplyTwoNumbers() {
        assertEquals(0, bd("10").compareTo(service.multiply(bd("5"), bd("2"))));
    }

    @Test
    void divideTwoNumbers() {
        assertEquals(0, bd("2.5").compareTo(service.divide(bd("5"), bd("2"))));
    }

    @Test
    void divideNonTerminating() {
        assertDoesNotThrow(() -> service.divide(bd("1"), bd("3")));
    }
}