package com.truckcompany.service.dto;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;

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


    private List<User> users;

    public CompanyDTO(){

    }

    public CompanyDTO(Company company){
        this(company.getId(), company.getName(), company.getUsers());
    }

    public CompanyDTO(Long id, String name, List<User> users){
        this.id = id;
        this.name = name;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
