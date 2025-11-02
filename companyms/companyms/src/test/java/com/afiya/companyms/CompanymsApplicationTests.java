//package com.afiya.companyms;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//class CompanymsApplicationTests {
//
//	@Test
//	void contextLoads() {
//        assertEquals(true, true,"Context loads successfully");
//	}
//
//}
package com.afiya.companyms;

// JUnit 5 imports
import org.junit.jupiter.api.Test;
// --- NEW IMPORTS FOR PARAMETERIZED TEST ---
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Spring Boot testing imports
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// Import your classes
import com.afiya.companyms.companies.Company;
import com.afiya.companyms.companies.CompanyRepository;
import com.afiya.companyms.companies.CompanyService;

// JUnit assertion imports
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CompanymsApplicationTests {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Test // This original test stays the same
    void contextLoads() {
    }

    // --- HERE IS YOUR PARAMETERIZED TEST ---

    /**
     * Test Name: testGetCompanyById_WhenCompanyExists_ShouldReturnCompany
     * This will now run ONCE FOR EACH @CsvSource line
     */
    @ParameterizedTest // 1. Use @ParameterizedTest instead of @Test
    @CsvSource({ // 2. Provide your data
            "Test Corp, A company for testing",
            "Acme Inc., The cartoon company",
            "Innovate Solutions, A tech startup"
    })
    // 3. Add parameters to the method to receive the data
    void testGetCompanyById_WhenCompanyExists_ShouldReturnCompany(String companyName, String companyDescription) {

        // ARRANGE - Set up the test using the parameters
        Company testCompany = new Company();
        testCompany.setName(companyName); // Use the parameter
        testCompany.setDescription(companyDescription); // Use the parameter

        Company savedCompany = companyRepository.save(testCompany);
        Long savedCompanyId = savedCompany.getId();

        // ACT - Call the "System Under Test"
        Company foundCompany = companyService.getbyid(savedCompanyId);

        // ASSERT - Check the expected result using the parameters
        assertNotNull(foundCompany, "The service returned null");

        assertEquals(companyName,
                foundCompany.getName(),
                "The company name did not match what was expected.");

        assertEquals(companyDescription,
                foundCompany.getDescription(),
                "The company description did not match.");
    }

}

