package com.afiya.companyms.companies;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
@Entity

public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String description;
    private String name;


    //private List<Review> reviews;
    public Company() {

    }

    public long getId() {
        return id;   // ✅ add this getter
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
