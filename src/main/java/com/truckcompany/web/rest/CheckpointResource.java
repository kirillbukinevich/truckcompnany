package com.truckcompany.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Checkpoint;
import com.truckcompany.domain.RouteList;
import com.truckcompany.domain.User;
import com.truckcompany.repository.CheckpointRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.CheckpointService;
import com.truckcompany.service.RouteListService;
import com.truckcompany.service.UserService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CheckpointResource {
    private final Logger log = LoggerFactory.getLogger(CheckpointResource.class);


    @Inject
    private CheckpointService checkpointService;

    @Inject
    private RouteListService routeListService;


    @RequestMapping(value = "/checkpoint/{routeListId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Checkpoint>> getAllCheckPoints(@PathVariable Long routeListId) throws URISyntaxException {
        log.debug("REST request get all Checkpoints");

        List<Checkpoint> checkpoints = Collections.emptyList();
        checkpoints = checkpointService.getCheckpoints(routeListId);
        for (Checkpoint checkpoint : checkpoints) {
            checkpoint.setRouteList(null);
        }
        List<ManagedCheckPointVM> managedCheckPointVMs = checkpoints.stream()
            .map(ManagedCheckPointVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = HeaderUtil.createAlert("checkpoint.getAll", null);
        return new ResponseEntity(managedCheckPointVMs, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/checkpoint_mark_date/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void markDate(@PathVariable Long id) {
        checkpointService.markDate(id);
    }


    @RequestMapping(value = "/checkpoints/{routeListId}",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public ResponseEntity<List<Checkpoint>> createCheckpoints(@RequestBody List<Checkpoint> checkpoints,
                                                              @PathVariable Long routeListId) {
        log.debug("REST request to create {} Checkpoints", checkpoints.size());
        RouteList routeList = routeListService.getRouteListById(routeListId);
        List<Checkpoint> checkpointList = checkpointService.createCheckpoints(checkpoints, routeList);
        HttpHeaders headers = HeaderUtil.createAlert("checkpoints.created", null);
        return new ResponseEntity<>(checkpointList, headers, HttpStatus.OK);
    }
}
