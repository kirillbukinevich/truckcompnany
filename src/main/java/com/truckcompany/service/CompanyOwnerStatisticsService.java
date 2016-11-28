package com.truckcompany.service;

import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.facade.RouteListFacade;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.result.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Vlad Momotov on 24.11.2016.
 */

@Service
@Transactional
public class CompanyOwnerStatisticsService {
    private final Logger log = LoggerFactory.getLogger(CompanyOwnerStatisticsService.class);

    @Inject
    private RouteListFacade routeListFacade;

    public List<List<Long>> getConsumptionStatstics(){
        List<RouteListDTO> routeListDTOs = routeListFacade.findRouteLists();

        Map<Long, Long> map = routeListDTOs.stream()
                .collect(Collectors.toMap(s-> s.getCreationDate().toInstant().toEpochMilli(),
                        s-> s.getTruck().getConsumption()*s.getFuelCost()*s.getDistance(),
                        (a,b) -> a+ b));

        List<List<Long>> result = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : map.entrySet()){
            ArrayList<Long> list = new ArrayList<>();
            list.add(entry.getKey());
            list.add(entry.getValue());
            result.add(list);
        }

        Collections.sort(result, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return result;
    }

    public HSSFWorkbook getRouteListsReport(ZonedDateTime fromDate, ZonedDateTime toDate){
        HSSFWorkbook book = new HSSFWorkbook() ;
        Sheet sheet = book.createSheet("test");

        Row row = sheet.createRow(0);

        Cell name = row.createCell(0);
        name.setCellValue("Vlad");

        Cell birthdate = row.createCell(1);

        DataFormat format = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(format.getFormat("dd.mm.yyyy"));
        birthdate.setCellStyle(dateStyle);

        birthdate.setCellValue(new Date(110, 10, 10));

        sheet.autoSizeColumn(1);

        return book;
    }

}
