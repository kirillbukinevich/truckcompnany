package com.truckcompany.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Checkpoint;
import com.truckcompany.repository.CheckpointRepository;
import com.truckcompany.service.CheckpointService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedCheckPointVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CheckpointResource {
    private final Logger log = LoggerFactory.getLogger(CheckpointResource.class);


    @Inject
    private CheckpointService checkpointService;



    @RequestMapping(value = "/checkpoint",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Checkpoint>> getAllCheckPoints()throws URISyntaxException{
        log.debug("REST request get all Checkpoints");

        List<Checkpoint> checkpoints = checkpointService.getCheckpoints();
        List<ManagedCheckPointVM> managedCheckPointVMs = checkpoints.stream()
            .map(ManagedCheckPointVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = HeaderUtil.createAlert("checkpoint.getAll",null);
        return new ResponseEntity(managedCheckPointVMs,headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/checkpoint_mark_date/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void changeCompanyStatus(@PathVariable Long id){
        checkpointService.markDate(id);
    }

}
