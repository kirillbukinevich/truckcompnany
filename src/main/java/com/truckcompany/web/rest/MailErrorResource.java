package com.truckcompany.web.rest;

import com.truckcompany.config.JHipsterProperties;
import com.truckcompany.domain.MailError;
import com.truckcompany.repository.MailErrorRepository;
import com.truckcompany.web.rest.vm.ManagedMailErrorVM;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Vladimir on 29.11.2016.
 */
@RestController
@RequestMapping("/api")
public class MailErrorResource {




}
