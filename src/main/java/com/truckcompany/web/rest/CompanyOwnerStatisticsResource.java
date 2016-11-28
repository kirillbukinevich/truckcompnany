package com.truckcompany.web.rest;

import com.truckcompany.service.CompanyOwnerStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Vlad Momotov on 24.11.2016.
 */

@RestController
@RequestMapping("/api")
public class CompanyOwnerStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyOwnerStatisticsResource.class);

    @Inject
    private CompanyOwnerStatisticsService statisticsService;

    @RequestMapping(value = "/companyowner/statistic/consumption", method = RequestMethod.GET)
    public ResponseEntity getConsumptionStatistics(){
        LOG.debug("REST get statistic from company owner");

        List<List<Long>> stat = statisticsService.getConsumptionStatstics();

        return new ResponseEntity<>(stat, HttpStatus.OK);
    }

    @RequestMapping(value = "/companyowner/statistic/xls/routelists", method = RequestMethod.GET)
    public ResponseEntity getRouteListsReport(){
        LOG.debug("REST get route lists xls report from company owner");

        byte[] report = statisticsService.getRouteListsReport(null, null);

        ByteArrayResource byteArrayResource = new ByteArrayResource(report);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/excel"));
        return new ResponseEntity<>(byteArrayResource, headers,  HttpStatus.OK);
    }


}
