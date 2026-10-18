package com.gautamkappagal.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import org.springframework.stereotype.Service;

@Service   // tells Spring: create and manage one instance of this class
public class CalculatorService {

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);   // BigDecimal is immutable: add() returns a NEW value
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new DivisionByZeroException();
        }

        return a.divide(b, MathContext.DECIMAL64);
    }
}