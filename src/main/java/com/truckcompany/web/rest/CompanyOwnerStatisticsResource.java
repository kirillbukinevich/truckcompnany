package com.truckcompany.web.rest;

import com.truckcompany.service.CompanyOwnerStatisticsService;
import com.truckcompany.service.RouteListService;
import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.facade.RouteListFacade;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.ZonedDateTime;
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

    @Inject
    private RouteListFacade routeListFacade;

/*
    @RequestMapping(value = "/companyowner/statistic/consumption", method = RequestMethod.GET)
    public ResponseEntity getConsumptionStatistics(){
        LOG.debug("REST get statistic from company owner");

        List<List<Long>> stat = statisticsService.getConsumptionStatstics();

        return new ResponseEntity<>(stat, HttpStatus.OK);
    }
*/

    @RequestMapping(value = "/companyowner/statistic/xls/routelists",
        method = RequestMethod.GET )
    public ResponseEntity<ByteArrayResource> getRouteListsReport(@RequestParam(value="startDate", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                     ZonedDateTime startDate,
                                                                 @RequestParam(value="endDate", required = false)
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                     ZonedDateTime endDate)
    {
        LOG.debug("REST get route lists xls report from company owner");

        ByteArrayResource byteResource = null;
        Workbook workbook;
        if (startDate== null || endDate == null){
            workbook = statisticsService.getRouteListsReport();
        }
        else{
            workbook = statisticsService.getRouteListsReport(startDate, endDate);
        }
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            outputStream.flush();

            byteResource = new ByteArrayResource(outputStream.toByteArray());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(byteResource, HttpStatus.OK);
    }


}