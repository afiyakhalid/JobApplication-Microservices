package com.afiya.companyms.companies;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

// --- Import all necessary static methods ---
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * This test class focuses ONLY on the CompanyController.
 * It uses @WebMvcTest to load only the web layer.
 * The CompanyService mock is provided manually via @TestConfiguration.
 */
@WebMvcTest(CompanyController.class)
class CompanyRestControllerTest {

    // 1. The tool to perform fake HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // 2. This is the MOCK CompanyService, provided by our TestConfig below.
    @Autowired
    private CompanyService companyService;

    // 3. The tool to convert objects to JSON
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This is the new, preferred way to provide mocks.
     * This inner class creates a MOCK bean of the CompanyService
     * and adds it to the test's application context.
     *
     */

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CompanyService companyService() {
            // Creates a mock of the CompanyService
            return mock(CompanyService.class);
        }
    }
    @BeforeEach
    void setUp() {
        reset(companyService);
    }
    // --- Test Methods for all 5 Endpoints ---

    @Test
    void testGetbyid_WhenCompanyExists() throws Exception {
        // ARRANGE
        Company fakeCompany = new Company();
        fakeCompany.setId(1L);
        fakeCompany.setName("TestCo");

        when(companyService.getbyid(1L)).thenReturn(fakeCompany);

        // ACT & ASSERT
        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestCo"));
    }

    @Test
    void testGetbyid_WhenCompanyNotFound() throws Exception {
        // ARRANGE
        when(companyService.getbyid(1L)).thenReturn(null);

        // ACT & ASSERT
        mockMvc.perform(get("/companies/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllCompany() throws Exception {
        // ARRANGE
        List<Company> companyList = new ArrayList<>();
        companyList.add(new Company());
        when(companyService.getAllCompany()).thenReturn(companyList);

        // ACT & ASSERT
        mockMvc.perform(get("/companies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1)); // Check that the JSON array has 1 item
    }

    @Test
    void testAddCompany() throws Exception {
        // ARRANGE
        Company newCompany = new Company();
        newCompany.setName("New Corp");

        // No 'when' needed since the service method returns void

        // ACT & ASSERT
        mockMvc.perform(post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCompany)))
                .andExpect(status().isOk())
                .andExpect(content().string("Company added successfully"));

        // VERIFY that the service's create method was called
        verify(companyService, times(1)).create(any(Company.class));
    }

    @Test
    void testUpdatedCompany() throws Exception {
        // ARRANGE
        Company updatedCompany = new Company();
        updatedCompany.setName("Updated Name");

        when(companyService.updatedcompany(any(Company.class), eq(1L))).thenReturn(true);

        // ACT & ASSERT
        mockMvc.perform(put("/companies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCompany)))
                .andExpect(status().isOk())
                .andExpect(content().string("company updated successfully"));

        // VERIFY that the service's update method was called
        verify(companyService, times(1)).updatedcompany(any(Company.class), eq(1L));
    }

    @Test
    void testDelete_WhenCompanyExists() throws Exception {
        // ARRANGE
        when(companyService.delete(1L)).thenReturn(true); // Your service logic returns true

        // ACT & ASSERT
        mockMvc.perform(delete("/companies/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Company deleted successfully"));

        // VERIFY that the service's delete method was called
        verify(companyService, times(1)).delete(1L);
    }

    @Test
    void testDelete_WhenCompanyNotFound() throws Exception {
        // ARRANGE
        when(companyService.delete(1L)).thenReturn(false); // Your service logic returns false

        // ACT & ASSERT
        mockMvc.perform(delete("/companies/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Company Not found"));

        // VERIFY that the service's delete method was still called
        verify(companyService, times(1)).delete(1L);
    }
}