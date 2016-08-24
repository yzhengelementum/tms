package com.tms.rest.publicapi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by yzheng on 8/24/16.
 */

@Path("version")
public class VersionResource {
    @NoArgsConstructor
    @Setter
    @Getter
    private static class VersionResponse {
        private String version;
        private String buildDate;
    }

    private Properties versionProperties;

    public VersionResource() {
        this.versionProperties = new Properties();
        try (InputStream inputStream = this.getClass().getResourceAsStream("/version.properties")) {
            this.versionProperties.load(inputStream);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public VersionResponse getVersion() {
        VersionResponse response = new VersionResponse();
        response.setVersion(this.versionProperties.getProperty("version"));
        response.setBuildDate(this.versionProperties.getProperty("build.date"));
        return response;
    }
}
