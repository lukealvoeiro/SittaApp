package mil.android.babysitter.data;

public class User {

    private String uid;
    private String name;
    private String email;
    private String bio;
    private String imageUrl;
    private String phoneNumber;
    private boolean babysitter;

    public User(){ }

    public User(String uid, String name, String email, String phoneNumber, boolean babysitter) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.babysitter = babysitter;
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
}

