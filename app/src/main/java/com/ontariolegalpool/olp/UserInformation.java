package com.ontariolegalpool.olp;

/**
 * Created by luka on 05/12/17.
 */

public class UserInformation  {

    private String first_name;
    private String last_name;
    private String email;
    private String phone_num;
    private String image;
    private String tag;

    public UserInformation(){

    }

    public UserInformation(String email, String first_name,
                           String last_name, String phone_num,
                           String image, String tag) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_num = phone_num;
        this.image = image;
        this.tag = tag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}