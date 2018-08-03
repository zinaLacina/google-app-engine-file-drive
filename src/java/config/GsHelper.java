/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import java.io.IOException;
import java.nio.channels.Channels;

/**
 *
 * @author lacinazina
 */
public class GsHelper {
    
    public static GcsService service;
    
    public static void createFolder(String name) throws IOException {
    Channels.newOutputStream(
            createFile(name + "/")
    ).close();
}

public static GcsOutputChannel createFile(String name) throws IOException {
    return getService().createOrReplace(
            new GcsFilename(getName(), name),
            GcsFileOptions.getDefaultInstance()
    );
}

private static String name;

public static String getName() {
    if (name == null) {
        name = AppIdentityServiceFactory.getAppIdentityService().getDefaultGcsBucketName();
    }
    return name;
}



public static GcsService getService() {
    if (service == null) {
        service = GcsServiceFactory.createGcsService(
                new RetryParams.Builder()
                        .initialRetryDelayMillis(10)
                        .retryMaxAttempts(10)
                        .totalRetryPeriodMillis(15000)
                        .build());
    }
    return service;
}
}
