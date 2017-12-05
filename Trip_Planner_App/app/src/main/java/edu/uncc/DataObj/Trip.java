package edu.uncc.DataObj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kalyan on 4/16/2017.
 */

public class Trip implements Serializable{
    private String title, location, photoURL;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String people;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPeople() {
        return people;
    }

    public void addPeople(String userId) {
        if(people==null){
            people = "";
            people+= userId;
        }else{
            if(!people.contains(userId)){
                people+= ","+userId;
            }
        }
    }
}
