package com.truckcompany.web.rest;

import com.truckcompany.service.MailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by Vladimir on 27.10.2016.
 */
@RestController
@RequestMapping("/api")
public class TestResource {

    @Inject
    private MailService mailService;

    @RequestMapping(value = "/sendmessage", method = RequestMethod.GET)
    @ResponseBody
    public void testSendEmail(){
        mailService.sendEmail("i.topolev.vladimir@gmail.com", "TEST", "TSET", false, true);
    }
}
