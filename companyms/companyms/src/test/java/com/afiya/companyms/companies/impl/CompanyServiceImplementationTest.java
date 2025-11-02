package com.afiya.companyms.companies.impl;

import com.afiya.companyms.companies.Company;
import com.afiya.companyms.companies.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// 1. Tell JUnit to use the Mockito extension to initialize mocks
@ExtendWith(MockitoExtension.class)
class CompanyServiceImplementationTest {

    // 2. Create a FAKE version of the CompanyRepository
    @Mock
    private CompanyRepository companyRepository;

    // 3. Create a REAL instance of the service and tell Mockito
    //    to inject the FAKE repository into it.
    @InjectMocks
    private CompanyServiceImplementation companyService;

    @Test
    void testGetbyid_WhenCompanyExists() {
        // --- ARRANGE ---
        // 1. Create a sample company to be "returned" by the fake repository
        Company fakeCompany = new Company();
        fakeCompany.setId(1L);
        fakeCompany.setName("TestCo");
        fakeCompany.setDescription("A test company");

        // 2. This is the most important Mockito part!
        //    We program the fake repository:
        //    "WHEN the findById(1L) method is called,
        //     THEN return our fakeCompany wrapped in an Optional."
        when(companyRepository.findById(1L)).thenReturn(Optional.of(fakeCompany));

        // --- ACT ---
        // Call the real service method. When it calls companyRepository.findById(1L),
        // Mockito will intercept it and return our fakeCompany.
        Company foundCompany = companyService.getbyid(1L);

        // --- ASSERT ---
        // Check that the service returned the data we told the mock to provide
        assertNotNull(foundCompany);
        assertEquals(1L, foundCompany.getId());
        assertEquals("TestCo", foundCompany.getName());

        // (Optional but good) Verify that the findById method was
        // called exactly 1 time.
        verify(companyRepository, times(1)).findById(1L);
    }

    @Test
    void testGetbyid_WhenCompanyDoesNotExist() {
        // --- ARRANGE ---
        // Program the fake repository:
        // "WHEN findById(1L) is called, THEN return an empty Optional."
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        // --- ACT ---
        Company foundCompany = companyService.getbyid(1L);

        // --- ASSERT ---
        // Check that the service correctly returned null
        assertNull(foundCompany);
    }
}
