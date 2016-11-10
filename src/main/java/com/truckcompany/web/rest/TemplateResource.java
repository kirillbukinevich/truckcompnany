package com.truckcompany.web.rest;

import com.truckcompany.domain.Storage;
import com.truckcompany.domain.Template;
import com.truckcompany.service.TemplateService;
import com.truckcompany.service.util.UploadException;
import com.truckcompany.service.util.UploadUtil;
import com.truckcompany.web.rest.vm.ManagedStorageVM;
import com.truckcompany.web.rest.vm.ManagedTemplateVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static com.truckcompany.web.rest.util.HeaderUtil.createAlert;
import static java.lang.String.valueOf;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Vladimir on 07.11.2016.
 */
@RestController
@RequestMapping("/api")
public class TemplateResource {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateResource.class);

    @Inject
    private TemplateService templateService;

    @RequestMapping(value = "/templates/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedTemplateVM> getTemplate(@PathVariable Long id) {
        LOG.debug("REST request to get Template with id {}", id);

        Template template = templateService.getTemplate(id);
        HttpStatus status = template == null ? NOT_FOUND : OK;
        ManagedTemplateVM body = template == null ? null : new ManagedTemplateVM(template, template.getRecipient(), null);
        return new ResponseEntity<ManagedTemplateVM>(body, status);
    }


    @RequestMapping(value = "/templates", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedTemplateVM>> getTemplates() {
        LOG.debug("REST request to get Template");

        List<ManagedTemplateVM> managedTemplateVMs = templateService.getTemplatesCreatedByCurrentAdmin().stream()
            .map( template -> new ManagedTemplateVM(template, template.getRecipient(), template.getAdmin()))
            .collect(Collectors.toList());

        HttpHeaders headers = createAlert("templates.getAll", null);
        return new ResponseEntity<>(managedTemplateVMs, headers, OK);
    }

    @RequestMapping(value = "/templates", method = POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createStorage(@RequestBody ManagedTemplateVM template) throws URISyntaxException {
        LOG.debug("REST request to save Template: {};", template.getName());

        Template result = templateService.createTemplate(template);

        return ResponseEntity.created(new URI("/templates/" + result.getId()))
            .headers(createAlert("template.created", valueOf(result.getId())))
            .body(new ManagedTemplateVM(result));
    }


    @ResponseBody
    @RequestMapping(value = "/template/upload", method = RequestMethod.POST)
    public String testSendEmail(@RequestParam(value = "upload", required = false) Part file, HttpServletRequest request) throws IOException {
        LOG.debug("Upload image for template");
        String rootUploadDirectory = request.getServletContext().getRealPath("content/upload/templateimage");
        try {
            String name = UploadUtil.uploadImage(file, rootUploadDirectory);
            String rootURL = request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI()));
            String fullPath = rootURL + "/content/upload/templateimage/" + name;
            String result = String.format("<script type=\"text/javascript\">window.parent.CKEDITOR.tools.callFunction(\"%s\", \"%s\", \"%s\" );</script>",
                request.getParameter("CKEditorFuncNum"), fullPath, "File is uploaded.");
            return result;
        } catch (UploadException e) {
            LOG.debug("Problem with upload image for mail template", e);
            return null;
        }

    }
}
