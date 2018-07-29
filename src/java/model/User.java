package model;

import com.google.appengine.api.datastore.Key;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Muhammad Wannous
 */
public class User implements Serializable {

    private long userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String passWord;
    private Long remainMemory;
    private String photo;
    private Date dateCreated;

    public User(String firstName, String lastName, String userName, String passWord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Long getRemainMemory() {
        return remainMemory;
    }

    public void setRemainMemory(Long remainMemory) {
        this.remainMemory = remainMemory;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    

}
