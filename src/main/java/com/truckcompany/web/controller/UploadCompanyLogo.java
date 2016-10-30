package com.truckcompany.web.controller;

import com.truckcompany.config.ApplicationProperties;
import com.truckcompany.domain.Company;
import com.truckcompany.repository.CompanyRepository;
import com.truckcompany.web.rest.util.HeaderUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Vladimir on 25.10.2016.
 */
@Controller
@Transactional
@RequestMapping("/api")
public class UploadCompanyLogo {

    @Autowired
    ServletContext context;


    @Inject
    private ApplicationProperties applicationProperties;


    private Set<String> accessExt = new HashSet<>(Arrays.asList("JPG","PNG","BMP","GIF","TIF"));

    private final Logger log = LoggerFactory.getLogger(UploadCompanyLogo.class);

    @Inject
    private CompanyRepository companyRepository;




    @RequestMapping(value = "/deletelogo/{company_id}", method = RequestMethod.GET)
    @ResponseBody
    public void deleteLogo(@PathVariable Long company_id, HttpServletRequest request) throws IOException {
        Company company = companyRepository.getOne(company_id);
        String uploadFolder = request.getServletContext().getRealPath("content/upload/logocompany");
        deleteFile(uploadFolder + File.separator + company.getLogo());
        company.setLogo(null);
        companyRepository.save(company);
    }


    @RequestMapping(value = "/uploadlogo", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file", required = false) Part file,
                                     @RequestParam(value = "file_name") String fileName,
                                     @RequestParam(value = "company_id") Long company_id,
                                     HttpServletRequest request) throws IOException {

        String ext = getExtensionFile(fileName).toUpperCase();
        if (!accessExt.contains(ext)){
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("logoCompany", "imageinvalidformat", "Possible image extensions are JPG, PNG, BMP, GIF, TIF"))
                    .body(null);
        }

        if (file != null){
            FileOutputStream fileOutputStream = null;
            byte[] filecontent = null;
            String deleteLogo = null;
            try{
                String uploadFolder = request.getServletContext().getRealPath("content/upload/logocompany");
                createFolderForUpload(uploadFolder);

                String nameFileLogo = getUniqueFileName(fileName);
                File logo = new File(uploadFolder + File.separator + nameFileLogo);
                fileOutputStream = new FileOutputStream(logo);

                InputStream inputStream = file.getInputStream();
                filecontent = IOUtils.toByteArray(inputStream);

                fileOutputStream.write(filecontent);

                Company company = companyRepository.getOne(company_id);
                deleteLogo = company.getLogo();
                company.setLogo(nameFileLogo);
                companyRepository.save(company);
                return new ResponseEntity<>(HeaderUtil.createEntityCreationAlert("image",nameFileLogo),HttpStatus.OK);
            } catch (IOException ex){
                log.debug("Can not upload logo for company", ex);
                return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("logoCompany", "uploadimageproblem", "The image can not be loaded"))
                    .body(null);
            } finally {
                fileOutputStream.close();
                deleteFile(applicationProperties.getRootFolderForUpload() + "logocompany" + File.separator + deleteLogo);
            }
        } else{
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("logoCompany", "uploadimageproblem", "You can choose file"))
                .body(null);
        }
    }

    private boolean deleteFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()) return false;
        boolean mark = file.delete();
        return mark;

    }

    private void createFolderForUpload(String rootFolder){
        /*log.debug("ROOT: {}", rootFolder);*/
        File folder = new File(rootFolder);
        if (!folder.exists()) {
            boolean mkdir = folder.mkdirs();
        }
    }

    private String getExtensionFile(String fileName) {
        String[] spliteFileName = fileName.split("\\.");
        return spliteFileName.length == 2 ? spliteFileName[1] : EMPTY;
    }

    private String getUniqueFileName(String fileName) {
        String exp = getExtensionFile(fileName);
        return UUID.randomUUID().toString().replaceAll("-", EMPTY) + "." + exp;
    }


}
