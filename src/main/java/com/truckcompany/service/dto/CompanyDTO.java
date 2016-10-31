package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import com.truckcompany.domain.enums.CompanyStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Vladimir on 21.10.2016.
 */
public class CompanyDTO {

    private Long id;

    @Size(min = 1, max = 50)
    private String name;


    private CompanyStatus status;

    private String logo;

    private Set<User> users;

    public CompanyDTO(){

    }

    public CompanyDTO(Company company){
        this(company.getId(), company.getName(), company.getStatus(), company.getLogo(), company.getUsers());
    }

    public CompanyDTO(Long id, String name, CompanyStatus status, String logo, Set<User> users){
        this.id = id;
        this.name = name;
        this.status = status;
        this.logo = logo;
        this.users = users;
    }


    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("CompanyDTO{")
            .append("id=").append(id).append(", ")
            .append("name=").append(name);

        /*if ((users != null) || (users.size() > 0)){
            str.append(", Users {");
            for (User user : users){
                str.append(user).append(", ");
            }
            str.delete(str.length()-2, str.length()).append("}");
        }*/

        str.append(" }");

        return str.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public void setStatus(CompanyStatus status) {
        this.status = status;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
