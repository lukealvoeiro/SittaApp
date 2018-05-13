package mil.android.babysitter.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String uid;
    private String name;
    private String email;
    private String bio;
    private String imageUrl;
    private String phoneNumber;
    //private String location;
    private boolean babysitter;
    private List<User> rejectedUsers;
    private List<User> acceptedUsers;

    public User(){ }

    public User(String uid, String name, String email, String phoneNumber, boolean babysitter) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.babysitter = babysitter;
        this.acceptedUsers = new ArrayList<User>();
        this.rejectedUsers = new ArrayList<User>();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBabysitter() {
        return babysitter;
    }

    public void setBabysitter(boolean babysitter) {
        this.babysitter = babysitter;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<User> getRejectedUsers() {
        return rejectedUsers;
    }

    public void setRejectedUsers(List<User> rejectedUsers) {
        this.rejectedUsers = rejectedUsers;
    }

    public List<User> getAcceptedUsers() {
        return acceptedUsers;
    }

    public void setAcceptedUsers(List<User> acceptedUsers) {
        this.acceptedUsers = acceptedUsers;
    }

    public void addAcceptedUser(User user) {
        this.acceptedUsers.add(user);
    }

    public void addRejectedUser(User user) {
        this.rejectedUsers.add(user);
    }

    public void removeAcceptedUser(User user) {
        this.acceptedUsers.remove(user);
    }

}

