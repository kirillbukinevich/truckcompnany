package com.truckcompany.service;


import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.service.dto.GoodsDTO;
import com.truckcompany.service.dto.WaybillDTO;
import com.truckcompany.service.facade.WaybillFacade;
import com.truckcompany.web.rest.dataforhighcharts.NameAndValueStatisticData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Vlad Momotov on 24.11.2016.
 */

@Service
@Transactional
public class CompanyOwnerStatisticsService {
    private final Logger log = LoggerFactory.getLogger(CompanyOwnerStatisticsService.class);

    @Inject
    private WaybillFacade waybillFacade;

    @Inject
    private UserService userService;


    public List<List<Double>> getConsumptionStatistics(){
        log.debug("get consumption statistic");

        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithState(WaybillState.DELIVERED);

        Map<Long, Double> map = getConsumptionDataMap(waybills);

        List<List<Double>> result = convertToConvenientForGraphView(map);
        Collections.sort(result, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return result;
    }

    public List<List<Double>> getConsumptionStatistics(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade
            .findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState.DELIVERED,
                fromDate.atStartOfDay(ZoneOffset.systemDefault()),
                toDate.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).minusNanos(1));

        Map<Long, Double> map = getConsumptionDataMap(waybills);

        List<List<Double>> result = convertToConvenientForGraphView(map);
        Collections.sort(result, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return result;
    }

    public List<List<Double>> getIncomeStatistics(){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithState(WaybillState.DELIVERED);
        Map<Long, Double> map = getIncomeDataMap(waybills);
        List<List<Double>> arrayForGraph = convertToConvenientForGraphView(map);
        Collections.sort(arrayForGraph, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return arrayForGraph;
    }

    public List<List<Double>> getIncomeStatistics(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade
            .findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState.DELIVERED,
                fromDate.atStartOfDay(ZoneOffset.systemDefault()),
                toDate.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).minusNanos(1));

        Map<Long, Double> map = getIncomeDataMap(waybills);

        List<List<Double>> arrayForGraph = convertToConvenientForGraphView(map);


        Collections.sort(arrayForGraph, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return arrayForGraph;
    }

    public List<List<Double>> getProfitStatistics(){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithState(WaybillState.DELIVERED);

        Map<Long, Double> profit = getProfitDataMap(waybills);

        List<List<Double>> arrayForGraph = convertToConvenientForGraphView(profit);

        Collections.sort(arrayForGraph, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return arrayForGraph;
    }

    public List<List<Double>> getProfitStatistics(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade
            .findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState.DELIVERED,
                fromDate.atStartOfDay(ZoneOffset.systemDefault()),
                toDate.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).minusNanos(1));


        Map<Long, Double> profit = getProfitDataMap(waybills);

        List<List<Double>> arrayForGraph = convertToConvenientForGraphView(profit);

        Collections.sort(arrayForGraph, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));
        return arrayForGraph;
    }

    public List<List<Double>> getLossStatistics(){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithState(WaybillState.DELIVERED);
        return createGraphLossData(waybills);

    }

    public List<List<Double>> getLossStatistics(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState.DELIVERED,
            fromDate.atStartOfDay(ZoneOffset.systemDefault()),
            toDate.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).minusNanos(1));
        return createGraphLossData(waybills);
    }

    public HSSFWorkbook getCommonReport(LocalDate fromDate, LocalDate toDate){
        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("report");

        DataFormat dataFormat = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));

        CellStyle currencyStyle = book.createCellStyle();
        currencyStyle.setDataFormat(dataFormat.getFormat("$#,##0.00;-$#,##0.00")) ;  // currency format with dolar sign

        createHeaderForCommonReport(sheet);

        LocalDate startDate = fromDate;
        LocalDate endDate;
        int rowIndex = 1;

        double totalConsumption = 0, totalIncome = 0, totalProfit = 0, totalLoss = 0;
        while (startDate.isBefore(toDate.plusDays(1))){

            if (Period.between(startDate, toDate).getDays() < 7){
                endDate = toDate;
            }
            else{
                endDate = startDate.plusDays(6);
            }

            List<WaybillDTO> waybills = waybillFacade
                .findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState.DELIVERED,
                    startDate.atStartOfDay(ZoneOffset.systemDefault()),
                    endDate.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).minusNanos(1));

            Map<Long, Double> consumptionDataMap = getConsumptionDataMap(waybills);
            double weekConsumption = consumptionDataMap.values().stream()
                .mapToDouble(Double::valueOf)
                .sum();
            totalConsumption += weekConsumption;

            double weekIncome = getIncomeDataMap(waybills).values().stream()
                .mapToDouble(Double::valueOf)
                .sum();
            totalIncome += weekIncome;

            double weekProfit = getProfitDataMap(waybills).values().stream()
                .mapToDouble(Double::valueOf)
                .sum();
            totalProfit += weekProfit;

            double weekLoss = getLossDataMap(waybills).values().stream()
                .mapToDouble(Double::valueOf)
                .sum();
            totalLoss += weekLoss;

            Row row = sheet.createRow(rowIndex++);

            Cell startDateCell = row.createCell(0);
            startDateCell.setCellStyle(dateStyle);
            startDateCell.setCellValue(GregorianCalendar
                .from(Instant.ofEpochMilli(startDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli())
                    .atZone(ZoneOffset.systemDefault())));

            Cell endDateCell = row.createCell(1);
            endDateCell.setCellStyle(dateStyle);
            endDateCell.setCellValue(GregorianCalendar
                .from(Instant.ofEpochMilli(endDate.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli())
                    .atZone(ZoneOffset.systemDefault())));

            Cell consumptionCell = row.createCell(2);
            consumptionCell.setCellStyle(currencyStyle);
            consumptionCell.setCellValue(weekConsumption);

            Cell incomeCell = row.createCell(3);
            incomeCell.setCellStyle(currencyStyle);
            incomeCell.setCellValue(weekIncome);

            Cell lossCell = row.createCell(4);
            lossCell.setCellStyle(currencyStyle);
            lossCell.setCellValue(weekLoss);

            Cell profitCell = row.createCell(5);
            profitCell.setCellStyle(currencyStyle);
            profitCell.setCellValue(weekProfit);

            startDate = startDate.plusDays(7);

        }

        rowIndex++;
        Row total  = sheet.createRow(rowIndex);

        Cell totalNameCell = total.createCell(0);
        totalNameCell.setCellValue("Total");
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 1));

        Cell totalConsumptionCell = total.createCell(2);
        totalConsumptionCell.setCellStyle(currencyStyle);
        totalConsumptionCell.setCellValue(totalConsumption);

        Cell totalIncomeCell = total.createCell(3);
        totalIncomeCell.setCellStyle(currencyStyle);
        totalIncomeCell.setCellValue(totalIncome);

        Cell totalLossCell = total.createCell(4);
        totalLossCell.setCellStyle(currencyStyle);
        totalLossCell.setCellValue(totalLoss);

        Cell totalProfitCell = total.createCell(5);
        totalProfitCell.setCellStyle(currencyStyle);
        totalProfitCell.setCellValue(totalProfit);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        return book;
    }

    public HSSFWorkbook getRouteListsReport(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithRouteListCreationDateBetween(fromDate.atStartOfDay(ZoneId.systemDefault()),
            toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1));

        HSSFWorkbook book = new HSSFWorkbook() ;
        Sheet sheet = book.createSheet("report");

        DataFormat dataFormat = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));

        CellStyle currencyStyle = book.createCellStyle();
        currencyStyle.setDataFormat((short)8) ;  // currency format with dolar sign


        createHeaderForRouteListReport(sheet);

        for (int i=0; i<waybills.size(); ++i) {
            Row row = sheet.createRow(i+1);
            fillRowForRouteListReport(row, dateStyle, currencyStyle, waybills.get(i));
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);

        return book;
    }

    public HSSFWorkbook getRouteListsReport(){
        List<WaybillDTO> waybills = waybillFacade.findWaybills();

        HSSFWorkbook book = new HSSFWorkbook() ;
        Sheet sheet = book.createSheet("routelist report");

        DataFormat dataFormat = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));

        CellStyle currencyStyle = book.createCellStyle();
        currencyStyle.setDataFormat((short)8) ;  // currency format with dolar sign


        createHeaderForRouteListReport(sheet);

        for (int i=0; i<waybills.size(); ++i) {
            Row row = sheet.createRow(i+1);
            fillRowForRouteListReport(row, dateStyle, currencyStyle, waybills.get(i));
        }


        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);
        sheet.autoSizeColumn(8);

        return book;
    }

    public HSSFWorkbook getLossReport(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade
            .findWaybillsWithStolenGoodsAndRouteListArrivalDateBetween(fromDate.atStartOfDay(ZoneId.systemDefault()),
                toDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusNanos(1));
        return createLossReport(waybills);
    }

    public HSSFWorkbook getLossReportWithResponsiblePersons(LocalDate fromDate, LocalDate toDate){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState.DELIVERED,
            fromDate.atStartOfDay(ZoneOffset.systemDefault()),
            toDate.plusDays(1).atStartOfDay(ZoneOffset.systemDefault()).minusNanos(1));

        Map<String, List<WaybillDTO>> driversLoss = getTopWorstDriversDataMap(waybills);

        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet();

        CellStyle currencyStyle = book.createCellStyle();
        currencyStyle.setDataFormat((short)8) ;  // currency format with dolar sign

        Row header = sheet.createRow(0);

        Cell nameHeader = header.createCell(0);
        nameHeader.setCellValue("Name");

        Cell valueHeader = header.createCell(1);
        valueHeader.setCellValue("Value");

        int rowIndex = 1;

        for(String login: driversLoss.keySet()){
            double driverLoss = driversLoss.get(login)
                .stream()
                .mapToDouble(this::countWaybillLoss)
                .sum();

            if (driverLoss > 0) {

                User driver = userService.getUserByLogin(login).get();

                Row row = sheet.createRow(rowIndex++);

                Cell name = row.createCell(0);
                name.setCellValue(driver.getFirstName() + " " + driver.getLastName());

                Cell value = row.createCell(1);
                value.setCellStyle(currencyStyle);
                value.setCellValue(driverLoss);
            }

        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        return book;
    }

    public List<NameAndValueStatisticData> getTopBestDrivers(int amount){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithState(WaybillState.DELIVERED);

        Map<String, List<WaybillDTO>> loginAndValueMap = getTopBestDriversDataMap(waybills, amount);

        List<NameAndValueStatisticData> statisticData = new ArrayList<>();
        for(String login: loginAndValueMap.keySet()){
            double driverProfit = loginAndValueMap.get(login)
                .stream()
                .mapToDouble(this::countWaybillProfitWithLoss)
                .sum();

            if (driverProfit > 0) {


                NameAndValueStatisticData record = new NameAndValueStatisticData();
                Optional<User> driver = userService.getUserByLogin(login);
                record.setName(driver.get().getFirstName() + " " + driver.get().getLastName());
                record.setY(driverProfit);
                statisticData.add(record);
            }
        }
        return statisticData;
    }

    public HSSFWorkbook getTopBestDriversReport(int amount){
        List<WaybillDTO> waybills = waybillFacade.findWaybillsWithState(WaybillState.DELIVERED);
        Map<String, List<WaybillDTO>> loginAndValueMap = getTopBestDriversDataMap(waybills, amount);

        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("top best drivers report");

        DataFormat dataFormat = book.createDataFormat();
        CellStyle currencyStyle = book.createCellStyle();
        currencyStyle.setDataFormat(dataFormat.getFormat("$#,##0.00;-$#,##0.00")) ;

        Row header = sheet.createRow(0);

        Cell nameHeader = header.createCell(0);
        nameHeader.setCellValue("Name");

        Cell valueHeader = header.createCell(1);
        valueHeader.setCellValue("Value");

        int rowIndex = 1;

        for(String login: loginAndValueMap.keySet()){
            double driverProfit = loginAndValueMap.get(login)
                .stream()
                .mapToDouble(this::countWaybillProfitWithLoss)
                .sum();

            if (driverProfit > 0) {

                User driver = userService.getUserByLogin(login).get();

                Row row = sheet.createRow(rowIndex++);

                Cell name = row.createCell(0);
                name.setCellValue(driver.getFirstName() + " " + driver.getLastName());

                Cell value = row.createCell(1);
                value.setCellStyle(currencyStyle);
                value.setCellValue(driverProfit);
            }

        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        return book;
    }

    private void createHeaderForRouteListReport(Sheet sheet){
        Row header = sheet.createRow(0);

        Cell id = header.createCell(0);
        id.setCellValue("Number");

        Cell creationDate = header.createCell(1);
        creationDate.setCellValue("Creation date");


        Cell leavingDate = header.createCell(2);
        leavingDate.setCellValue("Leaving date");

        Cell arrivalDate = header.createCell(3);
        arrivalDate.setCellValue("Arrival date");

        Cell truckNumber = header.createCell(4);
        truckNumber.setCellValue("Truck number");

        Cell leavingStorage = header.createCell(5);
        leavingStorage.setCellValue("Leaving storage");

        Cell arrivalStorage = header.createCell(6);
        arrivalStorage.setCellValue("Arrival storage");

        Cell state = header.createCell(7);
        state.setCellValue("State");

        Cell fuelCost = header.createCell(8);
        fuelCost.setCellValue("Fuel cost");

        Cell distance = header.createCell(9);
        distance.setCellValue("Distance");

        Cell waybillID = header.createCell(10);
        waybillID.setCellValue("Waybill number");

    }

    private void createHeaderForCommonReport(Sheet sheet){
        Row header = sheet.createRow(0);

        Cell startDate = header.createCell(0);
        startDate.setCellValue("Start date");

        Cell endDate = header.createCell(1);
        endDate.setCellValue("End date");

        Cell consumption = header.createCell(2);
        consumption.setCellValue("Consumption");

        Cell income = header.createCell(3);
        income.setCellValue("Income");

        Cell loss = header.createCell(4);
        loss.setCellValue("Loss");

        Cell profit = header.createCell(5);
        profit.setCellValue("Profit");
    }

    private void fillRowForRouteListReport(Row row, CellStyle dateStyle,CellStyle currencyStyle, WaybillDTO waybill){
        Cell id = row.createCell(0);
        id.setCellValue(waybill.getRouteList().getNumber());

        Cell creationDate = row.createCell(1);
        creationDate.setCellStyle(dateStyle);
        creationDate.setCellValue(GregorianCalendar.from(waybill.getRouteList().getCreationDate()));

        Cell leavingDate = row.createCell(2);
        leavingDate.setCellStyle(dateStyle);
        leavingDate.setCellValue(GregorianCalendar.from(waybill.getRouteList().getLeavingDate()));

        Cell arrivalDate = row.createCell(3);
        arrivalDate.setCellStyle(dateStyle);
        arrivalDate.setCellValue(GregorianCalendar.from(waybill.getRouteList().getArrivalDate()));

        Cell truckNumber = row.createCell(4);
        truckNumber.setCellValue(waybill.getRouteList().getTruck().getRegNumber());

        Cell leavingStorage = row.createCell(5);
        leavingStorage.setCellValue(waybill.getRouteList().getLeavingStorage().getName());

        Cell arrivalStorage = row.createCell(6);
        arrivalStorage.setCellValue(waybill.getRouteList().getArrivalStorage().getName());

        Cell state = row.createCell(7);
        state.setCellValue(waybill.getRouteList().getState());

        Cell fuelCost = row.createCell(8);
        fuelCost.setCellStyle(currencyStyle);
        fuelCost.setCellValue(waybill.getRouteList().getFuelCost());

        Cell distance = row.createCell(9);
        distance.setCellValue(waybill.getRouteList().getDistance());

        Cell waybillID = row.createCell(10);
        waybillID.setCellValue(waybill.getNumber());
    }

    private List<List<Double>> createGraphLossData(List<WaybillDTO> waybills){

        Map<Long,Double> map = getLossDataMap(waybills);
        List<List<Double>> result = convertToConvenientForGraphView(map);

        Collections.sort(result, (o1, o2) -> o1.get(0).compareTo(o2.get(0)));

        return result;
    }

    private HSSFWorkbook createLossReport(List<WaybillDTO> waybills){
        HSSFWorkbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("loss report");

        DataFormat dataFormat = book.createDataFormat();
        CellStyle dateStyle = book.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("dd.mm.yyyy"));

        CellStyle currencyStyle = book.createCellStyle();
        currencyStyle.setDataFormat(dataFormat.getFormat("$#,##0.00;-$#,##0.00")) ;

        CellStyle doubleStyle = book.createCellStyle();
        doubleStyle.setDataFormat(dataFormat.getFormat("0.00"));

        Row header = sheet.createRow(0);

        Cell date = header.createCell(0);
        date.setCellValue("Date");

        Cell waybillNumber = header.createCell(1);
        waybillNumber.setCellValue("Waybill №");

        Cell goodsName = header.createCell(2);
        goodsName.setCellValue("Goods");

        Cell goodsAcceptedAmount = header.createCell(3);
        goodsAcceptedAmount.setCellValue("Accepted");

        Cell goodsDeliveredAmount = header.createCell(4);
        goodsDeliveredAmount.setCellValue("Delivered");

        Cell goodsPrice = header.createCell(5);
        goodsPrice.setCellValue("Price");

        Cell lossAmount = header.createCell(6);
        lossAmount.setCellValue("Loss amount");

        int index = 1;

        double total = 0;

        for (WaybillDTO waybill : waybills){
            double totalLoss = 0;
            for (GoodsDTO good : waybill.getGoods()){
                double goodLoss = (good.getAcceptedNumber() - good.getDeliveredNumber()) * good.getPrice();
                if (goodLoss > 0) {
                    totalLoss += goodLoss;

                    Row row = sheet.createRow(index++);

                    Cell dateCell = row.createCell(0);
                    dateCell.setCellStyle(dateStyle);
                    dateCell.setCellValue(GregorianCalendar.from(waybill.getRouteList().getArrivalDate()));

                    Cell waybillNumberCell = row.createCell(1);
                    waybillNumberCell.setCellValue(waybill.getNumber());

                    Cell goodsNameCell = row.createCell(2);
                    goodsNameCell.setCellValue(good.getName());

                    Cell goodsAcceptedAmountCell = row.createCell(3);
                    goodsAcceptedAmountCell.setCellStyle(doubleStyle);
                    goodsAcceptedAmountCell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    goodsAcceptedAmountCell.setCellValue(good.getAcceptedNumber());

                    Cell goodsDeliveredAmountCell = row.createCell(4);
                    goodsDeliveredAmountCell.setCellStyle(doubleStyle);
                    goodsDeliveredAmountCell.setCellValue(good.getDeliveredNumber());

                    Cell goodsPriceCell = row.createCell(5);
                    goodsPriceCell.setCellStyle(currencyStyle);
                    goodsPriceCell.setCellValue(good.getPrice());

                    Cell lossAmountCell = row.createCell(6);
                    lossAmountCell.setCellStyle(currencyStyle);
                    lossAmountCell.setCellValue(goodLoss);
                }

            }

            Row row = sheet.createRow(index);

            Cell totalDate = row.createCell(0);
            totalDate.setCellStyle(dateStyle);
            totalDate.setCellValue(GregorianCalendar.from(waybill.getRouteList().getArrivalDate()));

            Cell totalWaybillNumber = row.createCell(1);
            totalWaybillNumber.setCellValue(waybill.getNumber());

            Cell totalCell = row.createCell(2);
            totalCell.setCellValue("Total: ");

            sheet.addMergedRegion(new CellRangeAddress(index, index, 2,5));

            Cell totalLossCell = row.createCell(6);
            totalLossCell.setCellStyle(currencyStyle);
            totalLossCell.setCellValue(totalLoss);

            index += 2;


            total += totalLoss;
        }

        Row totalRow = sheet.createRow(index);

        Cell totalCell = totalRow.createCell(0);
        totalCell.setCellValue("Total: ");
        sheet.addMergedRegion(new CellRangeAddress(index, index, 0, 5));

        Cell totalValue = totalRow.createCell(6);
        totalValue.setCellStyle(currencyStyle);
        totalValue.setCellValue(total);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);

        return book;
    }


    private List<List<Double>> convertToConvenientForGraphView(Map<Long, Double> map){
        List<List<Double>> result = new ArrayList<>();
        for (Map.Entry<Long, Double> entry : map.entrySet()){
            ArrayList<Double> list = new ArrayList<>();
            list.add(Double.valueOf(entry.getKey()));
            list.add(entry.getValue());
            result.add(list);
        }
        return result;
    }

    private Map<Long, Double> getLossDataMap(List<WaybillDTO> waybills){
        Function<WaybillDTO, Double> valueMapper = this::countWaybillLoss;

        Map<Long, Double> map = waybills.stream()
            .collect(Collectors.toMap(s -> s.getRouteList().getArrivalDate().truncatedTo(ChronoUnit.DAYS)
                    .toInstant().toEpochMilli(),
                valueMapper, (a,b) -> a +b));

        return map;
    }

    private Map<Long, Double> getConsumptionDataMap(List<WaybillDTO> waybills){
        Map<Long, Double> map = waybills.stream()
            .collect(Collectors.toMap(s -> s.getRouteList().getArrivalDate().truncatedTo(ChronoUnit.DAYS)
                    .toInstant().toEpochMilli(),
                this::countWaybillConsumption,
                (a,b) -> a+ b));
        return map;
    }

    private Map<Long, Double> getProfitDataMap(List<WaybillDTO> waybills){
        // profit without loss;
        Map<Long, Double> profitMap = waybills.stream()
            .collect(Collectors.toMap(s-> s.getRouteList().getArrivalDate().truncatedTo(ChronoUnit.DAYS)
                    .toInstant().toEpochMilli(),
                s-> countWaybillConsumption(s)*s.getMargin()/100 ,
                (a,b) -> a+ b));

        // loss
        Map<Long,Double> lossMap = getLossDataMap(waybills);

        Set<Long> dates = new HashSet<> (profitMap.keySet());
        dates.addAll(lossMap.keySet());

        Map<Long, Double> profit = Stream.concat(profitMap.keySet().stream(), lossMap.keySet().stream())
            .distinct()
            .collect(Collectors.toMap(k -> k, k-> profitMap.getOrDefault(k, (double) 0) - lossMap.getOrDefault(k, (double) 0)));

        return profit;

    }

    private Map<Long, Double> getIncomeDataMap(List<WaybillDTO> waybills){

        Map<Long, Double> map = waybills.stream()
            .collect(Collectors.toMap(s-> s.getRouteList().getArrivalDate().truncatedTo(ChronoUnit.DAYS)
                    .toInstant().toEpochMilli(),
                s-> countWaybillConsumption(s)*(1+(s.getMargin()/100)),
                (a,b) -> a+ b));

        return map;
    }

    private Double countWaybillLoss(WaybillDTO waybill){
        return  waybill.getGoods().stream()
            .filter(goodsDTO -> goodsDTO.getDeliveredNumber() != null)
            .mapToDouble(goods -> (goods.getAcceptedNumber() - goods.getDeliveredNumber())*goods.getPrice())
            .sum();
    }

    private Map<String, List<WaybillDTO>> getTopBestDriversDataMap(List<WaybillDTO> waybills, int amount){
        Function<WaybillDTO, String> keyMapper = w -> w.getDriver().getLogin();
        Function<WaybillDTO, List<WaybillDTO>> valueMapper = this::waybillToList;
        BinaryOperator<List<WaybillDTO>> mergeFunction = this::mergeLists;

        Map<String, List<WaybillDTO>> loginAndValueMap = waybills.stream()
            .collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue((o1, o2) -> {
                Double profit1 = o1.stream().mapToDouble(this::countWaybillProfitWithLoss)
                    .sum();
                Double profit2 = o2.stream().mapToDouble(this::countWaybillProfitWithLoss)
                    .sum();
                return profit2.compareTo(profit1);
            }))
            .limit(amount)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap:: new
            ));

        return loginAndValueMap;
    }

    private Map<String, List<WaybillDTO>> getTopWorstDriversDataMap(List<WaybillDTO> waybills ){
        Function<WaybillDTO, String> keyMapper = w -> w.getDriver().getLogin();
        Function<WaybillDTO, List<WaybillDTO>> valueMapper = this::waybillToList;
        BinaryOperator<List<WaybillDTO>> mergeFunction = this::mergeLists;

        Map<String, List<WaybillDTO>> loginAndValueMap = waybills.stream()
            .collect(Collectors.toMap(keyMapper, valueMapper, mergeFunction))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue((o1, o2) -> {
                Double loss1 = o1.stream().mapToDouble(this::countWaybillLoss)
                    .sum();
                Double loss2 = o2.stream().mapToDouble(this::countWaybillLoss)
                    .sum();
                return  loss2.compareTo(loss1);
            }))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap:: new
            ));

        return loginAndValueMap;
    }

    private List<WaybillDTO> waybillToList(WaybillDTO waybill){
        List<WaybillDTO> result = new ArrayList<>();
        result.add(waybill);
        return result;
    }

    private List<WaybillDTO> mergeLists(List<WaybillDTO> a, List<WaybillDTO> b){
        a.addAll(b);
        return a;
    }

    private double countWaybillProfitWithLoss(WaybillDTO waybill){
        return countWaybillConsumption(waybill)*waybill.getMargin()/100 - countWaybillLoss(waybill);
    }

    private double countWaybillConsumption(WaybillDTO waybill){
        return waybill.getRouteList().getTruck().getConsumption()*waybill.getRouteList().getFuelCost()
            *waybill.getRouteList().getDistance();
    }


}
