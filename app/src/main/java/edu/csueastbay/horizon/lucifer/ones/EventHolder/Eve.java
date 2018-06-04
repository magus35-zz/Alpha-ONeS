package edu.csueastbay.horizon.lucifer.ones.EventHolder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/* Class for obtaining the Name, location, age, and Pictures. Getter and setters that will be called
from inside the MainActivity class
        */
public class Eve {


    @SerializedName("name")
    @Expose()
    private String name;

    @SerializedName("url")
    @Expose()
    private String imageUrl;

    @SerializedName("Time")
    @Expose()
    private String time;

    @SerializedName("location")
    @Expose()
    private String location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

