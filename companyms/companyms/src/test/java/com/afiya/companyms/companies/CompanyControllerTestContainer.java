
package com.afiya.companyms.companies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test does EXACTLY what your course is teaching:
 * 1. It uses @Testcontainers to enable the framework.
 * 2. It uses @DataJpaTest (from Lesson 112) to test only the repository.
 * 3. It uses PostgreSQLContainer (from Lesson 111) as the database.
 */
@DataJpaTest
@Testcontainers
class CompanyControllerTestContainer {

    // This line creates the PostgreSQL container (Lesson 111)
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    // Because we use @DataJpaTest, we can auto-wire the repository
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testFindById_WhenCompanyExists() {
        // --- ARRANGE ---
        // This test runs against a REAL, empty PostgreSQL container
        Company companyToSave = new Company();
        companyToSave.setName("Testcontainers Corp");
        companyToSave.setDescription("A company for testing");

        // Save it to the database
        Company savedCompany = companyRepository.save(companyToSave);

        // --- ACT ---
        // Try to find the company we just saved
        Optional<Company> foundCompany = companyRepository.findById(savedCompany.getId());

        // --- ASSERT ---
        // Check that it was found and the name is correct
        assertTrue(foundCompany.isPresent());
        assertEquals("Testcontainers Corp", foundCompany.get().getName());
    }

    @Test
    void testFindById_WhenCompanyDoesNotExist() {
        // --- ARRANGE ---
        // (Do not save anything)

        // --- ACT ---
        Optional<Company> foundCompany = companyRepository.findById(99L);

        // --- ASSERT ---
        assertFalse(foundCompany.isPresent());
    }
}