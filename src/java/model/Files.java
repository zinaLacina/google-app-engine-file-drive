/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lacinazina
 */
public class Files implements Serializable{
    
    private long fileId;
    String fileName;
    int status;
    int isFolder;
    long fileSize;
    long parentFolder = 0;
    String typeFile;
    long ownerid;
    List<Long> accessRead;
    List<Long> fullAccess;
    Date creationDate;
    

    public Files(int status, int isFolder, long fileSize, String typeFile, long owner, Date creationDate,String fileName) {
        this.status = status;
        this.isFolder = isFolder;
        this.fileSize = fileSize;
        this.typeFile = typeFile;
        this.ownerid = owner;
        this.creationDate = creationDate;
        this.fileName = fileName;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(long ownerid) {
        this.ownerid = ownerid;
    }
    
    
    
    

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String FileName) {
        this.fileName = FileName;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int isIsFolder() {
        return isFolder;
    }

    public void setIsFolder(int isFolder) {
        this.isFolder = isFolder;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public long getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(long parentFolder) {
        this.parentFolder = parentFolder;
    }

    public String getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(String typeFile) {
        this.typeFile = typeFile;
    }

    public long getOwner() {
        return ownerid;
    }

    public void setOwner(long owner) {
        this.ownerid = owner;
    }

    public List<Long> getAccessRead() {
        return accessRead;
    }

    public void setAccessRead(List<Long> accessRead) {
        this.accessRead = accessRead;
    }

    public List<Long> getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(List<Long> fullAccess) {
        this.fullAccess = fullAccess;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
            
    
    
    
}
