package com.truckcompany.web.rest;

import com.truckcompany.service.CompanyOwnerStatisticsService;
import com.truckcompany.web.rest.dataforhighcharts.NameAndValueStatisticData;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
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


    @RequestMapping(value = "/companyowner/statistic/consumption", method = RequestMethod.GET)
    public ResponseEntity getConsumptionStatistics(@RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                           LocalDate startDate,
                                                   @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                           LocalDate endDate){
        LOG.debug("REST get statistic from company owner");

        List<List<Double>> stat;

        if (startDate == null || endDate == null){
            stat = statisticsService.getConsumptionStatistics();
        }
        else{
            stat = statisticsService.getConsumptionStatistics(startDate, endDate);
        }
        return new ResponseEntity<>(stat, HttpStatus.OK);
    }

    @RequestMapping(value = "/companyowner/statistic/income", method = RequestMethod.GET)
    public ResponseEntity getIncomeStatistics(@RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                  LocalDate startDate,
                                            @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                LocalDate endDate){
        LOG.debug("REST get income statistic from company owner");

        List<List<Double>> stat;

        if (startDate == null || endDate == null){
            stat = statisticsService.getIncomeStatistics();
        }
        else{
            stat = statisticsService.getIncomeStatistics(startDate, endDate);
        }
        return new ResponseEntity<>(stat, HttpStatus.OK);
    }

    @RequestMapping(value = "/companyowner/statistic/profit", method = RequestMethod.GET)
    public ResponseEntity getProfitStatistics(@RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                  LocalDate startDate,
                                              @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                  LocalDate endDate){
        LOG.debug("REST get income statistic from company owner");

        List<List<Double>> stat;

        if (startDate == null || endDate == null){
            stat = statisticsService.getProfitStatistics();
        }
        else{
            stat = statisticsService.getProfitStatistics(startDate, endDate);
        }
        return new ResponseEntity<>(stat, HttpStatus.OK);
    }

    @RequestMapping(value = "/companyowner/statistic/loss", method = RequestMethod.GET)
    public ResponseEntity getLossStatistics(@RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       LocalDate startDate,
                                                   @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                       LocalDate endDate){
        LOG.debug("REST get loss statistic from company owner");

        List<List<Double>> stat;

        if (startDate == null || endDate == null){
            stat = statisticsService.getLossStatistics();
        }
        else{
            stat = statisticsService.getLossStatistics(startDate, endDate);
        }
        return new ResponseEntity<>(stat, HttpStatus.OK);
    }

    @RequestMapping(value = "/companyowner/statistic/topbestdrivers", method = RequestMethod.GET)
    public ResponseEntity getTopBestDriversStatistics(@RequestParam int amount){
        LOG.debug("REST get top best drivers statistic from company owner");

        List<NameAndValueStatisticData> statistics = statisticsService.getTopBestDrivers(amount);

        return new ResponseEntity(statistics, HttpStatus.OK);

    }


    @RequestMapping(value = "/companyowner/statistic/xls/topbestdrivers", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getTopBestDriversReport(@RequestParam int amount){
        LOG.debug("REST get top best drivers xls report from company owner");

        ByteArrayResource byteResource = null;
        Workbook workbook = statisticsService.getTopBestDriversReport(amount);
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



    @RequestMapping(value = "/companyowner/statistic/xls/common", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getCommonReport(@RequestParam(value="startDate")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                          LocalDate startDate,
                                                                  @RequestParam(value="endDate")
                                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                          LocalDate endDate){
        LOG.debug("REST get common xls report from company owner");

        ByteArrayResource byteResource = null;
        Workbook workbook = statisticsService.getCommonReport(startDate, endDate);
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



    @RequestMapping(value = "/companyowner/statistic/xls/routelists",
        method = RequestMethod.GET )
    public ResponseEntity<ByteArrayResource> getRouteListsReport(@RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                         LocalDate startDate,
                                                                 @RequestParam(value="endDate", required = false)   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                         LocalDate endDate)
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


    @RequestMapping(value = "/companyowner/statistic/xls/loss", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getLossReport(@RequestParam(value="startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                      LocalDate startDate,
                                                                  @RequestParam(value="endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                      LocalDate endDate){
        LOG.debug("REST get loss xls report from company owner");


        Workbook workbook;
        if (startDate== null || endDate == null){
            workbook = statisticsService.getLossReport();
        }
        else{
            workbook = statisticsService.getLossReport(startDate, endDate);
        }

        return new ResponseEntity<>(convertWorkbookToByteArrayResource(workbook), HttpStatus.OK);
    }

    @RequestMapping(value = "/companyowner/statistic/xls/losswithrp", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> getLossReportWithResponsiblePersons(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate startDate,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                               LocalDate endDate){
        LOG.debug("REST get loss xls report with responsible persons from company owner");

        Workbook workbook = statisticsService.getLossReportWithResponsiblePersons(startDate, endDate);

        return new ResponseEntity<>(convertWorkbookToByteArrayResource(workbook), HttpStatus.OK);
    }

    private ByteArrayResource convertWorkbookToByteArrayResource(Workbook workbook){
        ByteArrayResource byteResource = null;
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            workbook.write(outputStream);
            outputStream.flush();

            byteResource = new ByteArrayResource(outputStream.toByteArray());
        } catch (IOException e) {
            LOG.warn("", e);
        }

        return byteResource;

    }




}
