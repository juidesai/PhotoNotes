package edu.jui.csulb.photonotes;

/**
 * Created by jmd19 on 3/9/2017.
 */

public class Note {

    public String id;
    public String caption;
    public String image;

//    public Note(String caption, int id){
//        this.caption=caption;
//        this.id=id;
//        this.image=image;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;}

    @Override
    public String toString() {
        return caption;
    }
}

