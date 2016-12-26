package com.truckcompany.service.facade;

import com.truckcompany.domain.User;
import com.truckcompany.domain.Waybill;
import com.truckcompany.domain.WaybillIndex;
import com.truckcompany.domain.enums.WaybillState;
import com.truckcompany.repository.search.WaybillSearchRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.UserService;
import com.truckcompany.service.WaybillService;
import com.truckcompany.service.dto.WaybillDTO;
import com.truckcompany.service.util.SearchUtil;
import com.truckcompany.web.rest.vm.SolrWaybillVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.truckcompany.repository.search.SearchableWaybillDefinition.*;
import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.emptyList;

@Component
@Transactional
public class DefaultWaybillFacade implements WaybillFacade {
    private static final Pattern IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}");
    private final Logger log = LoggerFactory.getLogger(DefaultWaybillFacade.class);
    @Inject
    private UserService userService;

    @Inject
    private WaybillService waybillService;

    @Inject
    private WaybillSearchRepository waybillSearchRepository;

    @Override
    public List<WaybillDTO> findWaybills() {

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybills for user \'{}\'", user.getLogin());
            List<WaybillDTO> waybills = emptyList();
            if (isCurrentUserInRole("ROLE_DRIVER")) {
                Optional<Waybill> waybillOptional = waybillService.getWaybillByDriver(user);
                if (waybillOptional.isPresent()) {
                    Waybill waybill = waybillOptional.get();
                    waybills = new ArrayList<>();
                    waybills.add(new WaybillDTO(waybill));
                } else {
                    return emptyList();
                }
            } else if (isCurrentUserInRole("ROLE_COMPANYOWNER") || isCurrentUserInRole("ROLE_MANAGER") || isCurrentUserInRole("ROLE_DISPATCHER")) {
                waybills = waybillService.getWaybillByCompany(user.getCompany())
                    .stream()
                    .map(WaybillDTO::new)
                    .collect(Collectors.toList());
            }
            return waybills;
        } else {
            return emptyList();
        }
    }

    @Override
    public List<WaybillDTO> findWaybillsWithRouteListCreationDateBetween(ZonedDateTime fromDate, ZonedDateTime toDate) {
        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybills for user \'{}\'", user.getLogin());
            List<WaybillDTO> waybills = emptyList();
            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                waybills = waybillService.getWaybillByCompanyAndRouteListCreationDateBetween(user.getCompany(), fromDate, toDate)
                    .stream()
                    .map(WaybillDTO::new)
                    .collect(Collectors.toList());
            }

            return waybills;
        } else {
            return emptyList();
        }
    }

    @Override
    public Page<WaybillDTO> findWaybillsWithRouteListCreationDateBetween(Pageable pageable, ZonedDateTime fromDate, ZonedDateTime toDate) {
        Page<Waybill> pageWaybills = new PageImpl<>(emptyList());

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybill for user \'{}\'", user.getLogin());

            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                pageWaybills = waybillService
                    .getPageWaybillsByCompanyAndRouteListCreationDateBetween(pageable, user.getCompany(), fromDate, toDate);
            }
        }
        return new PageImpl<>(pageWaybills.getContent().stream()
            .map(WaybillDTO::new)
            .collect(Collectors.toList()), pageable, pageWaybills.getTotalElements());
    }

    @Override
    public List<WaybillDTO> findWaybillsWithState(WaybillState state) {
        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get waybills with state " + state.toString() + " for user \'{}\'", user.getLogin());
            List<WaybillDTO> waybills = emptyList();
            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                waybills = waybillService.getWaybillByCompanyAndState(user.getCompany(), state)
                    .stream()
                    .map(WaybillDTO::new)
                    .collect(Collectors.toList());
            }

            return waybills;
        } else {
            return emptyList();
        }
    }

    @Override
    public List<WaybillDTO> findWaybillsWithStateAndRouteListArrivalDateBetween(WaybillState state, ZonedDateTime fromDate, ZonedDateTime toDate) {
        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get waybills with state {}, date between {} and {} for user \'{}\'", state.toString(), fromDate,
                toDate, user.getLogin());
            List<WaybillDTO> waybills = emptyList();

            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                waybills = waybillService.getWaybillByCompanyAndStateAndRouteListArrivalDateBetween(user.getCompany(), state,
                    fromDate, toDate)
                    .stream()
                    .map(WaybillDTO::new)
                    .collect(Collectors.toList());
            }

            return waybills;
        } else {
            return emptyList();
        }
    }

    @Override
    public Page<WaybillDTO> findWaybillWithStolenGoods(Pageable page) {
        Page<Waybill> pageWaybills = new PageImpl<>(emptyList());

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybills with stolen goods for user \'{}\'", user.getLogin());
            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                pageWaybills = waybillService.getPageWaybillByCompanyAndWithStolenGoods(page, user.getCompany());
            }

        }
        return new PageImpl<>(pageWaybills.getContent()
            .stream()
            .map(WaybillDTO::new)
            .collect(Collectors.toList()), page, pageWaybills.getTotalElements());
    }

    @Override
    public List<WaybillDTO> findWaybillsWithStolenGoodsAndRouteListArrivalDateBetween(ZonedDateTime fromDate, ZonedDateTime toDate) {
        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybills with stolen goods for user \'{}\'", user.getLogin());
            List<WaybillDTO> waybills = emptyList();
            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                waybills = waybillService.getWaybillsByCompanyAndWithStolenGoodsAndRouteListArrivalDateBetween(user.getCompany(), fromDate, toDate);
            }
            return waybills;

        } else {
            return emptyList();
        }
    }


    @Override
    public Page<WaybillDTO> findWaybills(Pageable pageable) {
        Page<Waybill> pageWaybills = new PageImpl<>(emptyList());

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybills for user \'{}\'", user.getLogin());
            if (isCurrentUserInRole("ROLE_COMPANYOWNER")) {
                pageWaybills = waybillService.getPageWaybillByCompany(pageable, user.getCompany());
            } else if (isCurrentUserInRole("ROLE_DISPATCHER")) {
                pageWaybills = waybillService.getPageWaybillByDispatcher(pageable, user);
            } else if (isCurrentUserInRole("ROLE_DRIVER")) {
                pageWaybills = waybillService.getPageHistoryWaybillByDriver(pageable, user);
            }
        }

        return new PageImpl<>(pageWaybills.getContent()
            .stream()
            .map(WaybillDTO::new)
            .collect(Collectors.toList()), pageable, pageWaybills.getTotalElements());
    }

    @Override
    public Page<WaybillDTO> findWaybillsForDriverTimetable(Pageable pageable) {
        Page<Waybill> pageWaybills = new PageImpl<>(emptyList());

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            log.debug("Get all waybills for user \'{}\'", user.getLogin());
            pageWaybills = waybillService.getPageTimetableWaybillByDriver(pageable, user);
        }

        return new PageImpl<>(pageWaybills.getContent()
            .stream()
            .map(WaybillDTO::new)
            .collect(Collectors.toList()), pageable, pageWaybills.getTotalElements());
    }

    @Override
    public List<SolrWaybillVM> findWaybillsAccordingQuery(String query) {

        Collection<String> strings = SearchUtil.splitSearchTermAndRemoveIgnoredCharacters(query, IGNORED_CHARS_PATTERN);

        String res = "";
        for (String fragment : strings) {
            res = res + "*" + fragment + "* ";
        }
        Optional<User> userOptional = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!userOptional.isPresent()) {
            return emptyList();
        }

        User user = userOptional.get();

        HighlightPage<WaybillIndex> waybillIndexHighlightPage = waybillSearchRepository.findByAllFieldsAndCompanyId(res, user.getCompany().getId(), new PageRequest(0, 100));

        int i = 0;
        for (HighlightEntry<WaybillIndex> waybill : waybillIndexHighlightPage.getHighlighted()) {
            for (HighlightEntry.Highlight highlight : waybill.getHighlights()) {
                for (String snippet : highlight.getSnipplets()) {
                    if (highlight.getField().getName().equals(NUMBER_FIELD_NAME)) {
                        waybillIndexHighlightPage.getContent().get(i).setNumber(snippet);
                    }
                    if (highlight.getField().getName().equals(DRIVER_FIRST_NAME_FIELD_NAME)) {
                        waybillIndexHighlightPage.getContent().get(i).setDriverFirstName(snippet);
                    }
                    if (highlight.getField().getName().equals(DRIVER_LAST_NAME_FIELD_NAME)) {
                        waybillIndexHighlightPage.getContent().get(i).setDriverLastName(snippet);
                    }
                    if (highlight.getField().getName().equals(DISPATCHER_FIRST_NAME_FIELD_NAME)) {
                        waybillIndexHighlightPage.getContent().get(i).setDispatcherFirstName(snippet);
                    }
                    if (highlight.getField().getName().equals(DISPATCHER_LAST_NAME_FIELD_NAME)) {
                        waybillIndexHighlightPage.getContent().get(i).setDispatcherLastName(snippet);
                    }
                }
            }
            i++;
        }
        return waybillIndexHighlightPage.getContent().stream().map(SolrWaybillVM::new).collect(Collectors.toList());
    }
}
