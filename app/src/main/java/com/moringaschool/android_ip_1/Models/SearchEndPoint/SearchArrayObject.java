package com.moringaschool.android_ip_1.Models.SearchEndPoint;
import org.parceler.Parcel;

@Parcel
public class SearchArrayObject  {

    String title = "";
    String image = "";
    String id = "";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
