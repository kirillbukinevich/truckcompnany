package com.truckcompany.service.facade;

import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.service.dto.WaybillDTO;
import com.truckcompany.web.rest.vm.SolrWaybillVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

public interface WaybillFacade {

    Page<WaybillDTO> findWaybills(Pageable pageable);

    List<WaybillDTO> findWaybills();

    List<WaybillDTO> findWaybillsWithRouteListCreationDateBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

    Page<WaybillDTO> findWaybillsWithRouteListCreationDateBetween(Pageable pageable, ZonedDateTime fromDate, ZonedDateTime toDate);

    List<WaybillDTO> findWaybillsWithState(WaybillState state);

    List<WaybillDTO> findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState state, ZonedDateTime fromDate, ZonedDateTime toDate);

    Page<WaybillDTO> findWaybillWithStolenGoods(Pageable pageable);

    List<WaybillDTO> findWaybillsWithStolenGoodsAndRouteListArrivalDateBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

    List<SolrWaybillVM> findWaybillsAccordingQuery(String query);
}
