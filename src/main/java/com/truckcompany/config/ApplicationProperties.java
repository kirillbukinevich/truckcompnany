package com.truckcompany.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Vladimir on 25.10.2016.
 */


@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String rootFolderForUpload;

    public String getRootFolderForUpload() {
        return rootFolderForUpload;
    }

    public void setRootFolderForUpload(String rootFolderForUpload) {
        this.rootFolderForUpload = rootFolderForUpload;
    }
}
