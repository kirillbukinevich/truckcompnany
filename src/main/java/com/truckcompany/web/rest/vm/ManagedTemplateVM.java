package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Template;
import com.truckcompany.domain.User;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.service.dto.TemplateDTO;

/**
 * Created by Vladimir on 08.11.2016.
 */
public class ManagedTemplateVM extends TemplateDTO{

    public ManagedTemplateVM(){
    }

    public ManagedTemplateVM(Template template){
        super(template);
    }

    public ManagedTemplateVM(Template template, User recipient, User admin){
        super(template, recipient, admin);
    }

    public ManagedTemplateVM(TemplateDTO template){
        super(template);
    }
}
