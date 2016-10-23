package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;

import javax.validation.constraints.Size;

/**
 * Created by Vladimir on 21.10.2016.
 */
public class CompanyDTO {

    private Long id;

    @Size(min = 1, max = 50)
    private String name;


    public CompanyDTO(){

    }

    public CompanyDTO(Company company){
        this(company.getId(), company.getName());
    }

    public CompanyDTO(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString(){
        return "CompanyDTO{" +
            "name=" + name +
            "}";
    }

    public void setName(String name) {
        this.name = name;
    }
}
