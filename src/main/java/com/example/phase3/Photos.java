package com.example.phase3;

import javafx.scene.image.Image;

public class Photos {
    private Image img;

    private String caption;

    public String comment = null;

    public int album_id;

    public int photo_id = -1;

    public void setPhoto(Image img) {
        this.img = img;
    }

    public Image getPhoto(){
        return img;
    }

    public void setAlbumId(int id){
        album_id = id;
    }

    public void setPhotoId(int id){
        photo_id = id;
    }

    public int getPhotoId(){
        return photo_id;
    }

    public void setCaption(String caption){
        this.caption = caption;
    }

    // alternative method in DB connection
    public String getCaption(){
        return caption;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return comment;
    }
}
