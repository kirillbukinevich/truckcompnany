package com.truckcompany.web.rest;

import com.truckcompany.domain.Truck;
import com.truckcompany.domain.User;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.CompanyOwnerStatisticsService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.facade.RouteListFacade;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.ChartDataVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.truckcompany.web.rest.util.PaginationUtil.generatePaginationHttpHeaders;

/**
 * Created by Vlad Momotov on 24.11.2016.
 */

@RestController
@RequestMapping("/api")
public class CompanyOwnerStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyOwnerStatisticsResource.class);

    @Inject
    private CompanyOwnerStatisticsService statisticsResource;

    @Inject

    @RequestMapping(value = "/companyowner/statistic/consumption", method = RequestMethod.GET)
    public ResponseEntity getConsumptionStatistics(){
        LOG.debug("REST get statistic from company owner");

        List<List<Long>> stat = statisticsResource.getConsumptionStatstics();

        return new ResponseEntity<>(stat, HttpStatus.OK);

    }
}
