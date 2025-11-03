package com.afiya.companyms.companies;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This annotation is the key:
 * 1. It loads ONLY the JPA components (your @Entity and @Repository).
 * 2. It connects to an in-memory H2 database (not your real PostgreSQL).
 * 3. It wraps every test in a transaction and rolls it back,
 * so each test is perfectly clean.
 */
@DataJpaTest
class CompanyRepositoryTest {

    // Spring will inject the REAL CompanyRepository,
    // but it will be connected to the FAKE H2 database.
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testFindById_WhenCompanyExists() {
        // --- 1. ARRANGE ---
        // Create a new company object (using your Company entity)
        Company companyToSave = new Company();
        companyToSave.setName("Test Corp");
        companyToSave.setDescription("A company for testing");

        // Save it to the in-memory H2 database
        Company savedCompany = companyRepository.save(companyToSave);

        // --- 2. ACT ---
        // Try to find the company we just saved by its ID
        Optional<Company> foundCompany = companyRepository.findById(savedCompany.getId());

        // --- 3. ASSERT ---
        // Check that the company was actually found
        assertTrue(foundCompany.isPresent(), "Company should be found in the database");

        // Check that the data is correct
        assertEquals("Test Corp", foundCompany.get().getName());
    }

    @Test
    void testFindById_WhenCompanyDoesNotExist() {
        // --- 1. ARRANGE ---
        // (We don't save anything)

        // --- 2. ACT ---
        // Try to find a company with a non-existent ID
        Optional<Company> foundCompany = companyRepository.findById(99L);

        // --- 3. ASSERT ---
        // Check that the company was NOT found
        assertFalse(foundCompany.isPresent(), "Company should not be found");
    }
}
