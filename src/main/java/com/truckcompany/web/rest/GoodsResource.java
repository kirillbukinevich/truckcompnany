package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Goods;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.service.GoodsService;
import com.truckcompany.service.dto.GoodsDTO;
import com.truckcompany.service.facade.GoodsFacade;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.vm.GoodsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
public class GoodsResource {

    private final Logger log = LoggerFactory.getLogger(GoodsResource.class);

    @Inject
    private GoodsService goodsService;

    @Inject
    private GoodsFacade goodsFacade;

    @RequestMapping(value = "/goods",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<?> createGoods(@RequestBody GoodsVM goodsVM, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Goods : {}", goodsVM);
        Goods newGoods = goodsService.createGoods(goodsVM);
        return ResponseEntity.created(new URI("/api/goods/" + newGoods.getName()))
            .headers(HeaderUtil.createAlert("goodsManagement.created", newGoods.getName()))
            .body(newGoods);
    }

    @RequestMapping(value = "/goods/{waybillId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.DRIVER})
    public ResponseEntity<List<GoodsVM>> getAllGoodsForDriver(@PathVariable Long waybillId) throws URISyntaxException {
        log.debug("REST request get all Goods");
        List<GoodsDTO> list = goodsFacade.findGoods(waybillId);
        List<GoodsVM> goodsVMs = list.stream()
            .map(GoodsVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = HeaderUtil.createAlert("goods.getAll",null);
        return new ResponseEntity<List<GoodsVM>>(goodsVMs, headers, OK);
    }


    @RequestMapping(value = "/goods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.DRIVER,AuthoritiesConstants.DISPATCHER,AuthoritiesConstants.MANAGER})
    public ResponseEntity<List<GoodsVM>> updateGoods(@RequestBody List<GoodsVM> goodsVM) {
        log.debug("REST request to update {} Goods", goodsVM);
        goodsService.updateGoods(goodsVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("goodsManagement.updated", goodsVM.toString()))
            .body(null);
    }

    @RequestMapping(value = "/goods/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGoods(@PathVariable Integer id) {
        log.debug("REST request to delete Goods: {}", id);
        goodsService.deleteGoods(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("goodsManagement.deleted", id.toString())).build();
    }
}
