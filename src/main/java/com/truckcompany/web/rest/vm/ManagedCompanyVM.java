package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Company;
import com.truckcompany.service.dto.CompanyDTO;

/**
 * Created by Vladimir on 21.10.2016.
 */
public class ManagedCompanyVM extends CompanyDTO {

    private Long id;

    public ManagedCompanyVM(){
    }

    public ManagedCompanyVM(Company company){
        super(company);
        this.id = company.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
