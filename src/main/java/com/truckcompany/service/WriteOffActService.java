package com.truckcompany.service;

import com.truckcompany.domain.Company;
import com.truckcompany.domain.User;
import com.truckcompany.domain.WriteOffAct;
import com.truckcompany.repository.UserRepository;
import com.truckcompany.repository.WriteOffActRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.web.rest.vm.ManagedWriteOffVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by Viktor Dobroselsky.
 */
@Service
@Transactional
public class WriteOffActService {
    private final Logger log = LoggerFactory.getLogger(WriteOffActService.class);

    @Inject
    private WriteOffActRepository writeOffActRepository;

    @Inject
    UserService userService;

    public WriteOffAct getWriteOffActById (Long id) {
        WriteOffAct writeOffAct = writeOffActRepository.getOne(id);
        log.debug("Get Information about WriteOffAct with id: {}", id);
        return writeOffAct;
    }

    public List<WriteOffAct> getWriteOffActByCompany (Company company){
        log.debug("Get writeOffAct for company with id: {}", company.getId());
        return writeOffActRepository.findByCompany(company);
    }

    public WriteOffAct createWriteOffAct (ManagedWriteOffVM managedWriteOffVM) {
        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        WriteOffAct writeOffAct = new WriteOffAct();
        writeOffAct.setCount(managedWriteOffVM.getCount());
        writeOffAct.setDate(managedWriteOffVM.getDate());

        writeOffAct.setCompany(optionalUser.get().getCompany());
        writeOffActRepository.save(writeOffAct);
        log.debug("Created Information for WriteOffAct");
        return writeOffAct;
    }

    public void updateWriteOffAct (ManagedWriteOffVM managedWriteOffVM) {
        writeOffActRepository.findOneById(managedWriteOffVM.getId()).ifPresent(writeOffAct -> {
            writeOffAct.setDate(managedWriteOffVM.getDate());
            writeOffAct.setCount(managedWriteOffVM.getCount());

            writeOffActRepository.save(writeOffAct);
            log.debug("Changed fields for Waybill {}", writeOffAct);
        });
    }

    public void deleteWriteOffAct (Long id) {
        WriteOffAct writeOffAct = writeOffActRepository.findOne(id);
        if (writeOffAct != null) {
            writeOffActRepository.delete(writeOffAct);
            log.debug("Deleted WriteOffAct {}", id);
        }
    }
}
