package JSON;

import java.util.List;

/**
 * Created by bruno on 06/04/2017.
 */

public class Results {

    public static String gender;


    public Results(){};

    public static String getGender() {
        return gender;
    }

    public static void setGender(String gender) {
        Results.gender = gender;
    }

    public List<Name> getName() {
        return name;
    }

    public Object setName(List<Name> name) {
        this.name = name;
        return null;
    }

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Login> getLogin() {
        return login;
    }

    public void setLogin(List<Login> login) {
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Picture> getPicture() {
        return picture;
    }

    public void setPicture(List<Picture> picture) {
        this.picture = picture;
    }

    public List<Name> name;
    public List<Location> location;

    public String email;
    public List<Login> login;

    public String phone;

    public List<Picture> picture;

}
