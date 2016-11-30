package com.truckcompany.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.truckcompany.domain.Goods;
import com.truckcompany.repository.GoodsRepository;
import com.truckcompany.security.AuthoritiesConstants;
import com.truckcompany.service.GoodsService;
import com.truckcompany.web.rest.util.HeaderUtil;
import com.truckcompany.web.rest.util.PaginationUtil;
import com.truckcompany.web.rest.vm.GoodsVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api")
public class GoodsResource {

    private final Logger log = LoggerFactory.getLogger(GoodsResource.class);

    @Inject
    private GoodsRepository goodsRepository;

    @Inject
    private GoodsService goodsService;

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

    @RequestMapping(value = "/goods",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<List<GoodsVM>> getAllGoods(Pageable pageable) throws URISyntaxException {
        Page<Goods> page = goodsRepository.findAll(pageable);
        List<GoodsVM> goodsVMs = page.getContent().stream()
            .map(GoodsVM::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/goods");
        return new ResponseEntity<>(goodsVMs, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/goods/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<GoodsVM> getGoods(@PathVariable Integer id) {
        log.debug("REST request to get Goods : {}", id);
        return goodsService.getGoodsById(id)
            .map(GoodsVM::new)
            .map(goodsVM -> new ResponseEntity<>(goodsVM, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/goods",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Timed
//    @Secured(AuthoritiesConstants.USER)
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
//    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Void> deleteGoods(@PathVariable Integer id) {
        log.debug("REST request to delete Goods: {}", id);
        goodsService.deleteGoods(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("goodsManagement.deleted", id.toString())).build();
    }
}
