package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Company;
import com.truckcompany.service.dto.CompanyDTO;

import javax.servlet.http.Part;

/**
 * Created by Vladimir on 21.10.2016.
 */
public class ManagedCompanyVM extends CompanyDTO {

    private String login;

    private String email;

    private String password;


    public ManagedCompanyVM(){
    }

    public ManagedCompanyVM(Company company){
        super(company);

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
