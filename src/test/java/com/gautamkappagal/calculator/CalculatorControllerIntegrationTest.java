package com.gautamkappagal.calculator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest   // starts the full Spring context (a mock web environment, no real port)
class CalculatorControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void addReturnsJsonResult() throws Exception {
        mockMvc.perform(get("/api/v1/add")
                .param("a", "2")
                .param("b", "3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operation").value("add"))
            .andExpect(jsonPath("$.result").value(5));
    }

    @Test
    void divideByZeroReturns400WithErrorBody() throws Exception {
        mockMvc.perform(get("/api/v1/divide")
                .param("a", "1")
                .param("b", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Division by zero is not allowed"));
    }

    @Test
    void subtractReturnsJsonResult() throws Exception {
        mockMvc.perform(get("/api/v1/subtract")
                .param("a", "5")
                .param("b", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.operation").value("subtract"))
            .andExpect(jsonPath("$.result").value(3));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/api/v1/add",
        "/api/v1/subtract",
        "/api/v1/multiply",
        "/api/v1/divide"
    })

    void missingParameterReturns400(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint)
                .param("a", "5"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message")
            .value("Missing required parameter: b"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/api/v1/add",
        "/api/v1/subtract",
        "/api/v1/multiply",
        "/api/v1/divide"
    })

    void nonNumericParameterReturns400(String endpoint) throws Exception {
        mockMvc.perform(get(endpoint)
                .param("a", "abc")
                .param("b", "2"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message")
            .value("Parameter 'a' must be a valid number, but was 'abc'"));
    }
}