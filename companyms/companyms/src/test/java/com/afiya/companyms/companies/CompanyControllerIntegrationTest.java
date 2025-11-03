package com.afiya.companyms.companies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is a FULL end-to-end integration test.
 * It starts a REAL web server on a RANDOM port and
 * uses a REAL database (or the H2 in-memory one specified below).
 */

// 1. (Lesson 96) Start a REAL server on a RANDOM port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// 2. (Lesson 95) Load an alternative configuration file.
//    We'll tell it to use an H2 in-memory database just for this test.
@TestPropertySource(locations = "classpath:application-test.properties")
class CompanyControllerIntegrationTest {

    // 3. (Lesson 98) Get the tool that can make REAL HTTP requests
    //    to our server.
    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * This test combines Lesson 97 (Test Create User) and Lesson 98 (TestRestTemplate).
     * It will create a new company via a POST request, then
     * fetch it back with a GET request to prove it was saved.
     */
    @Test
    void testCreateAndGetCompany() {

        // --- ARRANGE (Create a company object) ---
        Company newCompany = new Company();
        newCompany.setName("Integration Test Corp");
        newCompany.setDescription("A company created by a real HTTP test");

        // --- ACT 1: CREATE (Lesson 97) ---
        // Perform a REAL HTTP POST request to "/companies"
        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "/companies",  // The URL
                newCompany,    // The request body (will be converted to JSON)
                String.class   // The expected response body type
        );

        // --- ASSERT 1 ---
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertEquals("Company added successfully", createResponse.getBody());


        // --- ACT 2: GET (Lesson 98) ---
        // Now, let's fetch the company we just created.
        // (We'll assume it's ID 1 if the H2 database is clean)
        ResponseEntity<Company> getResponse = restTemplate.getForEntity(
                "/companies/1", // The URL
                Company.class   // The expected response (will be parsed from JSON)
        );

        // --- ASSERT 2 ---
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Integration Test Corp", getResponse.getBody().getName());
    }
}
