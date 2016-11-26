package com.truckcompany.service;

import com.truckcompany.service.dto.RouteListDTO;
import com.truckcompany.service.facade.RouteListFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Vlad Momotov on 24.11.2016.
 */

@Service
@Transactional
public class CompanyOwnerStatisticsService {


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

}
