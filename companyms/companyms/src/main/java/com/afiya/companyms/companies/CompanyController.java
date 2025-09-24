package com.afiya.companyms.companies;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private  CompanyService companyService;
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }
@GetMapping
    public ResponseEntity<List<Company>>  getAllCompany(){

     return new ResponseEntity<>( companyService.getAllCompany(),HttpStatus.OK);
    }
   @GetMapping("/{id}")
    public ResponseEntity<Company> getbyid(@PathVariable Long id){
       Company company=companyService.getbyid(id);
       if (company!=null){
           return new ResponseEntity<>(company, HttpStatus.OK) ;
       }
      return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }
    @PutMapping("/{id}")
    public ResponseEntity<String> updatedCompany(@PathVariable Long id,@RequestBody Company company){
        companyService.updatedcompany(company,id);
        return  new ResponseEntity<>("company updated successfully", HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> addCompany(@RequestBody Company company){
        companyService.create(company);
        return new ResponseEntity<>("Company added successfully",HttpStatus.OK);
    }
    @DeleteMapping("/{id}")

    public ResponseEntity<String> delete(@PathVariable Long id) {
      boolean isdeleted=  companyService.delete(id);
      if(isdeleted) {

          return new ResponseEntity<>("Company deleted successfully", HttpStatus.OK);

      }else{
          return new ResponseEntity<>("Company Not found", HttpStatus.NOT_FOUND);

      }

}
}
