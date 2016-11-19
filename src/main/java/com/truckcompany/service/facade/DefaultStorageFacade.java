package com.truckcompany.service.facade;

import com.truckcompany.config.JHipsterProperties;
import com.truckcompany.domain.Storage;
import com.truckcompany.domain.StorageIndex;
import com.truckcompany.domain.User;
import com.truckcompany.repository.search.SearchableStorageDefinition;
import com.truckcompany.repository.search.StorageSearchRepository;
import com.truckcompany.security.SecurityUtils;
import com.truckcompany.service.StorageService;
import com.truckcompany.service.UserService;
import com.truckcompany.service.dto.StorageDTO;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

import static com.truckcompany.repository.search.SearchableStorageDefinition.*;
import static com.truckcompany.security.SecurityUtils.isCurrentUserInRole;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.solr.core.query.result.HighlightEntry.*;

/**
 * Created by Vladimir on 01.11.2016.
 */
@Component
@Transactional
public class DefaultStorageFacade implements StorageFacade {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultStorageFacade.class);

    private static final Pattern IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}");

    @Inject
    private StorageService storageService;

    @Inject
    private UserService userService;

    @Inject
    private StorageSearchRepository storageSearchRepository;

    @Override
    public List<StorageDTO> findStorages() {

        Function<Storage, StorageDTO> toDTO = convertToStorageDto();

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            List<Storage> storages = emptyList();
            if (isCurrentUserInRole("ROLE_ADMIN")) {
                storages = storageService.getStoragesBelongsCompany(user.getCompany());
            }
            if (isCurrentUserInRole("ROLE_DISPATCHER")) {
                storages = storageService.getStoragesBelongsCompanyAndActivated(user.getCompany());
            }
            return storages.stream().map(toDTO).collect(toList());
        } else{
            return emptyList();
        }
    }

    @Override
    public List<StorageDTO> findStoragesAccordingQuery(String query) {

        Collection<String> fragments = splitSearchTermAndRemoveIgnoredCharacters(query);
        String res = "";
        for (String fragment : fragments){
            res = res + "*" + fragment + "* ";
        }
        LOG.debug("ATTENTION: {}", res);
        List<StorageDTO> storages = new ArrayList<>();
        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!optionalUser.isPresent()){
            return Collections.emptyList();
        }

        User user = optionalUser.get();

        HighlightPage<StorageIndex> hightlightStorages = storageSearchRepository.findByNameLikeOrAddressLikeAndIdCompany(res, user.getCompany().getId(), new PageRequest(0, 100));


        int i = 0;
        for (HighlightEntry<StorageIndex> storage :hightlightStorages.getHighlighted()){
            for (Highlight highlight : storage.getHighlights()){
                for (String snippet : highlight.getSnipplets()){
                    if (highlight.getField().getName().equals(NAME_FIELD_NAME)){
                        hightlightStorages.getContent().get(i).setName(snippet);
                    }
                    if (highlight.getField().getName().equals(ADDRESS_FIELD_NAME)){
                        hightlightStorages.getContent().get(i).setAddress(snippet);
                    }
                }
            }
            i++;
        }

        return hightlightStorages.getContent().stream().map(storage -> new StorageDTO(storage)).collect(toList());


        /* if (StringUtils.isBlank(query)){
            return Collections.emptyList();
        }

        Optional<User> optionalUser = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        if (!optionalUser.isPresent()){
            return Collections.emptyList();
        }

        User user = optionalUser.get();
        List<StorageDTO> storages = new ArrayList<>();



        HighlightPage<StorageIndex> hightlightStorages = storageSearchRepository.findByNameOrAddressStartsWithAndIdCompany(query, query, user.getCompany().getId(), new PageRequest(0, 100));

/*
        for (HighlightEntry<StorageIndex> hightlightStorage:hightlightStorages.getHighlighted()){
            Storage storage = getHightlightedStorage(hightlightStorage);
            storages.add(new StorageDTO(storage));
        }
      /*  for (hightlightStorages.getHighlighted())
/*
        List<HighlightEntry<Storage>> hightlightStorages = storageSearchRepository.findByNameOrAddressStartsWith(query, query);
        List<StorageDTO> storages = Collections.emptyList();

        for (HighlightEntry<Storage> hightlightStorage : hightlightStorages){
            Storage storage = getHightlightedStorage(hightlightStorage);
            storages.add(new StorageDTO(storage));
        }*/

      //  return storages;

    }

    @Override
    public Storage updateStorage(ManagedStorageVM managedStorageVM) throws UpdateStorageException {
        Storage existingStorage = storageService.getStorageById(managedStorageVM.getId());
        boolean isUserAdmin = isCurrentUserInRole("ROLE_ADMIN"); //todo security check
        if(!isUserAdmin) {
            throw new UpdateStorageException("No permission, because you're not administrator.");
        }
        boolean isValidStorage = isValidStorage(managedStorageVM.getCompany().getId()); //todo security check
        if(!isValidStorage) {
            throw new UpdateStorageException("This storage does not belong your company.");
        }
        if (existingStorage == null) {
            throw new UpdateStorageException("The storage is not found.");
        }
        existingStorage.setName(managedStorageVM.getName());
        existingStorage.setActivated(managedStorageVM.isActivated());
        existingStorage.setAddress(managedStorageVM.getAddress());
        return storageService.updateStorage(existingStorage);
    }


    private boolean isValidStorage(Long idCompany){
        Optional<User> user = userService.getUserByLogin(SecurityUtils.getCurrentUserLogin());
        return user.isPresent() ? idCompany.equals(user.get().getCompany().getId()) : false;

    }


    private Function<Storage, StorageDTO> convertToStorageDto() {
        return storage -> new StorageDTO(storage, storage.getCompany());
    }

    private Storage getHightlightedStorage(HighlightEntry<Storage> storageHighlightEntry){
        Storage storage = null;
        for (Highlight highlights: storageHighlightEntry.getHighlights()){
            storage = storageHighlightEntry.getEntity();
            String nameField = highlights.getField().getName();
            if (nameField.equals(NAME_FIELD_NAME)){
                storage.setName(highlights.getSnipplets().get(0));
            }
        }
        return storage;
    };

    private Collection<String> splitSearchTermAndRemoveIgnoredCharacters(String searchTerm) {
        String[] searchTerms = StringUtils.split(searchTerm, " ");
        List<String> result = new ArrayList<String>(searchTerms.length);
        for (String term : searchTerms) {
            if (StringUtils.isNotEmpty(term)) {
                result.add(IGNORED_CHARS_PATTERN.matcher(term).replaceAll(" "));
            }
        }
        return result;
    }
}
