package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.RouteList;
import com.truckcompany.repository.RouteListRepository;
import com.truckcompany.service.RouteListService;
import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.service.dto.TruckDTO;
import com.truckcompany.service.facade.RouteListFacade;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ManagedRouteListVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.truckcompany.web.rest.util.PaginationUtil.generatePaginationHttpHeaders;

/**
 * Created by Viktor Dobroselsky.
 */

@RestController
@RequestMapping(value = "/api")
public class RouteListResource {

    private final Logger log = LoggerFactory.getLogger(RouteListResource.class);

    @Inject
    private RouteListRepository routeListRepository;

    @Inject
    private RouteListService routeListService;

    @Inject
    private RouteListFacade routeListFacade;

    @RequestMapping(value = "/routelists",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagedRouteListVM>>
                        getAllRouteList(Pageable pageable,
                                        @RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate,
                                        @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate)
            throws URISyntaxException {
        log.debug("REST request get all RouteLists");

        Collection<SimpleGrantedAuthority> authorities =
            (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            List<RouteListDTO> routeLists = routeListFacade.findRouteLists();

            List<ManagedRouteListVM> managedRouteLists = routeLists.stream().map(ManagedRouteListVM::new)
                .collect(Collectors.toList());

            HttpHeaders headers = HeaderUtil.createAlert("routelist.getAll", null);
            return new ResponseEntity<List<ManagedRouteListVM>>(managedRouteLists, headers, HttpStatus.OK);
        } else {
            Page<RouteListDTO> page;
            if (startDate == null || endDate == null) {
                page = routeListFacade.findRouteLists(pageable);
            }
            else{
                page = routeListFacade.findRouteLists(pageable, startDate, endDate);
            }
            List<ManagedRouteListVM> managedRouteLists = page.getContent().stream()
                .map(ManagedRouteListVM::new)
                .collect(Collectors.toList());
            HttpHeaders headers = generatePaginationHttpHeaders(page, "/api/routelists");

            return new ResponseEntity(managedRouteLists, headers, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/routelists/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagedRouteListVM> getRouteList(@PathVariable Long id) {
        log.debug("REST request to get RouteList : {}", id);

        RouteList routeList = routeListService.getRouteListById(id);
        if (routeList == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        RouteListDTO routeListDTO = new RouteListDTO();
        routeListDTO.setId(routeList.getId());
        routeListDTO.setLeavingZonedDate(routeList.getLeavingDate());
        routeListDTO.setArrivalZonedDate(routeList.getArrivalDate());
        routeListDTO.setArrivalStorage(new StorageDTO(routeList.getArrivalStorage()));
        routeListDTO.setLeavingStorage(new StorageDTO(routeList.getLeavingStorage()));
        routeListDTO.setTruck(new TruckDTO(routeList.getTruck()));
        return new ResponseEntity<>(new ManagedRouteListVM(routeListDTO), HttpStatus.OK);
    }

    @RequestMapping(value = "/routelists",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> createRouteList(@RequestBody ManagedRouteListVM managedRouteListVM)
        throws URISyntaxException {
        log.debug("REST request to save Waybill");
        RouteList newRouteList = routeListService.createRouteList(managedRouteListVM);

        return ResponseEntity.created(new URI("/api/companies/" + newRouteList.getId()))
            .headers(HeaderUtil.createAlert("routeList.created", newRouteList.getId().toString()))
            .body(newRouteList);
    }

    @RequestMapping(value = "/routelists/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete RouteList: {}", id);
        routeListService.deleteRouteList(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("routelistManagement.deleted", id.toString())).build();
    }

    @RequestMapping(value = "/routelists",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity updateWaybill(@RequestBody ManagedRouteListVM managedRouteListVM) {
        log.debug("REST request to update Waybill : {}", managedRouteListVM);
        RouteList existingWaybill = routeListRepository.findOne(managedRouteListVM.getId());

        if (existingWaybill == null)
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("routelistManagement", "waybilldontexist", "Waybill doesn't exist!")).body(null);

        routeListService.updateRouteList(managedRouteListVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("userManagement.updated", managedRouteListVM.getId().toString()))
            .body(new ManagedRouteListVM(routeListService.getRouteListById(managedRouteListVM.getId())));
    }
}
