package com.truckcompany.service.util;

import com.truckcompany.domain.Company;
import com.truckcompany.web.rest.util.HeaderUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.truckcompany.service.util.UploadImageErrors.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Vladimir on 04.11.2016.
 */
public class UploadUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UploadUtil.class);

    private static Set<String> accessExt = new HashSet<>(Arrays.asList("JPG", "PNG", "BMP", "GIF", "TIF"));

    public static String uploadImage(Part image, String realImageName, String uploadDirectory) throws IOException, UploadException {
        if (image == null) {
            throw new UploadException(IMAGE_NOT_EXCITED);
        }

        if (!accessExt.contains(getExtensionFile(realImageName))) {
            throw new UploadException(INVALID_EXTENSTION);
        }

        if (!createFolderForUpload(uploadDirectory)) {
            throw new UploadException(INVALID_UPLOAD_DIRECTORY);
        }

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        byte[] filecontent = null;
        try {
            String nameImage = getUniqueFileName(realImageName);
            File newImage = new File(uploadDirectory + File.separator + nameImage);
            fileOutputStream = new FileOutputStream(newImage);

            inputStream = image.getInputStream();
            filecontent = IOUtils.toByteArray(inputStream);
            fileOutputStream.write(filecontent);
            return nameImage;
        } catch (IOException ex) {
            throw new UploadException(UploadImageErrors.UPLOAD_ERROR, ex);
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
    }

    public static boolean deleteFile(String filePath){
        File file = new File(filePath);
        return file.exists() ? file.delete() : true;
    }




    private static boolean createFolderForUpload(String uploadFolder) {
        File folder = new File(uploadFolder);
        return (folder.exists()) ? true : folder.mkdirs();
    }

    private static String getUniqueFileName(String fileName) {
        String exp = getExtensionFile(fileName);
        return UUID.randomUUID().toString().replaceAll("-", EMPTY) + "." + exp;
    }

    private static String getExtensionFile(String fileName) {
        String[] spliteFileName = fileName.split("\\.");
        return spliteFileName.length == 2 ? spliteFileName[1].toUpperCase() : EMPTY;
    }

}
