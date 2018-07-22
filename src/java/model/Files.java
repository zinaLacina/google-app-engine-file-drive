/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import config.TypeFile;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lacinazina
 */
public class Files implements Serializable{
    
    String FileName;
    boolean status;
    boolean isFolder;
    String fileSize;
    int parentFolder = 0;
    TypeFile typeFile;
    User owner;
    List<User> accessRead;
    List<User> fullAccess;
    Date creationDate;

    public Files(boolean status, boolean isFolder, String fileSize, TypeFile typeFile, User owner, Date creationDate) {
        this.status = status;
        this.isFolder = isFolder;
        this.fileSize = fileSize;
        this.typeFile = typeFile;
        this.owner = owner;
        this.creationDate = creationDate;
    }
    
    
    
    

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String FileName) {
        this.FileName = FileName;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isIsFolder() {
        return isFolder;
    }

    public void setIsFolder(boolean isFolder) {
        this.isFolder = isFolder;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(int parentFolder) {
        this.parentFolder = parentFolder;
    }

    public TypeFile getTypeFile() {
        return typeFile;
    }

    public void setTypeFile(TypeFile typeFile) {
        this.typeFile = typeFile;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<User> getAccessRead() {
        return accessRead;
    }

    public void setAccessRead(List<User> accessRead) {
        this.accessRead = accessRead;
    }

    public List<User> getFullAccess() {
        return fullAccess;
    }

    public void setFullAccess(List<User> fullAccess) {
        this.fullAccess = fullAccess;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
            
    
    
    
}
