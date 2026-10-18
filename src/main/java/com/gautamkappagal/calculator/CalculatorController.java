package com.gautamkappagal.calculator;

import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController                 // handles HTTP; return values are written as JSON
@RequestMapping("/api/v1")      // common URL prefix for every method below
public class CalculatorController {

    private final CalculatorService service;

    // Constructor injection: Spring passes in the CalculatorService bean
    public CalculatorController(CalculatorService service) {
        this.service = service;
    }

    @GetMapping("/add")
    public CalculationResponse add(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return new CalculationResponse("add", a, b, service.add(a, b));
    }

    @GetMapping("/subtract")
    public CalculationResponse subtract(@RequestParam("a") BigDecimal a, @RequestParam("b") BigDecimal b) {
        return new CalculationResponse("subtract", a, b, service.subtract(a, b));
    }
}