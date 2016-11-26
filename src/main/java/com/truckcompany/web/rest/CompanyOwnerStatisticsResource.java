package com.truckcompany.web.rest;

import com.truckcompany.service.CompanyOwnerStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Vlad Momotov on 24.11.2016.
 */

@RestController
@RequestMapping("/api")
public class CompanyOwnerStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyOwnerStatisticsResource.class);

    @Inject
    private CompanyOwnerStatisticsService statisticsResource;

    @RequestMapping(value = "/companyowner/statistic/consumption", method = RequestMethod.GET)
    public ResponseEntity getConsumptionStatistics(){
        LOG.debug("REST get statistic from company owner");

        List<List<Long>> stat = statisticsResource.getConsumptionStatstics();

        return new ResponseEntity<>(stat, HttpStatus.OK);

    }
}
