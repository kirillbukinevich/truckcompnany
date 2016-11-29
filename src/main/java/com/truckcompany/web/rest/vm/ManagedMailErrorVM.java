package com.truckcompany.web.rest.vm;

import com.truckcompany.domain.MailError;
import com.truckcompany.domain.Template;
import com.truckcompany.service.dto.MailErrorDTO;

/**
 * Created by Vladimir on 29.11.2016.
 */
public class ManagedMailErrorVM extends MailErrorDTO {
    public ManagedMailErrorVM(MailError mailError, Template template){
        super(mailError, template);
    }
}
