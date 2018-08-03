/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import config.Defs;
import model.*;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import model.User;

/**
 *
 * @author lacinazina
 */
public class Help {

    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000));
    }

    public static String format(double bytes, int digits) {
        String[] dictionary = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};
        int index = 0;
        for (index = 0; index < dictionary.length; index++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
    }

    public boolean getRecent(Date date) {
        Date today = new Date();
        return date.compareTo(today) == 0;
    }

    public boolean noSpace() {
        return true;
    }

    public static String getFileExt(String fileName) {
        if (fileName != null) {
            Filename fileN = new Filename(fileName, '/', '.');
            String extension = fileN.extension();
            if (extension != null) {
                return extension;
            } else {
                return "none";
            }
        } else {
            return "none";
        }

    }

    public static long getFileSize(File file) {
        // Get length of file in bytes
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;
        return fileSizeInMB;
    }

    public static long reductUserQuota(Long fileSize, User user) {
        long size = user.getRemainMemory() - fileSize;
        //user.setRemainMemory(size);
        return size;
    }

    public static List<Files> folder(long userId) throws ParseException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        Filter currentIdUser = new FilterPredicate(Defs.ENTITY_PROPERTY_OWNER, FilterOperator.EQUAL, userId);
        Filter isFolder = new FilterPredicate(Defs.ENTITY_PROPERTY_FOLDER, FilterOperator.EQUAL, 1);
        Filter validFilter = CompositeFilterOperator.and(currentIdUser, isFolder);

        Query fileQuery = new Query(Defs.DATASTORE_KIND_FILES_STRING).setFilter(validFilter);
        //Query folderQuery = new Query(Defs.ENTITY_PROPERTY_FOLDER).setFilter(1);
        List<Entity> files = datastore.prepare(fileQuery).asList(FetchOptions.Builder.withDefaults());

        List<Files> result = new ArrayList<>();
        if (!files.isEmpty()) {
            Iterator<Entity> allFiles = files.iterator();
            Files filesModel;
            while (allFiles.hasNext()) {
                Entity log = allFiles.next();
                Date date1 = (Date) log.getProperty(Defs.ENTITY_PROPERTY_CREATED);
                filesModel = new Files(1, 1, 2,
                        (String) log.getProperty(Defs.ENTITY_PROPERTY_FILETYPE), userId,
                        date1, (String) log.getProperty(Defs.ENTITY_PROPERTY_FILENAME_STRING));
                filesModel.setFileId(log.getKey().getId());
                result.add(filesModel);

            }
        }
        return result;
    }

    public static List<User> getUser() throws ParseException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Query usersQuery = new Query(Defs.DATASTORE_KIND_USER_STRING);
        //Query folderQuery = new Query(Defs.ENTITY_PROPERTY_FOLDER).setFilter(1);
        List<Entity> users = datastore.prepare(usersQuery).asList(FetchOptions.Builder.withDefaults());

        List<User> result = new ArrayList<>();
        if (!users.isEmpty()) {
            Iterator<Entity> allUsers = users.iterator();

            while (allUsers.hasNext()) {
                Entity log = allUsers.next();
                User user = new User((String) log.getProperty(Defs.ENTITY_PROPERTY_FIRSTNAME_STRING),
                        (String) log.getProperty(Defs.ENTITY_PROPERTY_LASTNAME_STRING),
                        (String) log.getProperty(Defs.PARAM_USERNAME_STRING),
                        "");

                user.setUserId(log.getKey().getId());

                result.add(user);

            }
        }
        return result;
    }

    public static String userFolder(User user){
        return user.getFirstName()+""+user.getUserId();
    }
}
