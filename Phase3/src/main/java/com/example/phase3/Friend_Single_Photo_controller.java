package com.example.phase3;

import Database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class Friend_Single_Photo_controller {

    @FXML
    public Label caption;

    @FXML
    public Button comment;

    @FXML
    private TextField commentText;

    @FXML
    public RadioButton like;

    @FXML
    private ImageView photo;

    @FXML
    private AnchorPane singlePhotoPane;

    @FXML
    public Label updatedComment;

    @FXML
    private Label warning;

    @FXML
    public Label likesCount = new Label("Total Likes: 0");

    public Photos currPhoto = new Photos();

    public Users friendUser;

    In_Friend_Album_controller inFriendAlbumController;

    List<Photos> imgList = null;

    public Users currUser;

    public void setCurrUser(Users currUser){
        this.currUser = currUser;
        like.setOnAction(event -> {
            like.setDisable(true);
            warning.setText("You liked the photo!");
            DatabaseConnection db = new DatabaseConnection();
            db.addLike(currPhoto, currUser);
            likesCount.setText("Total Likes: "+db.likesCount(currPhoto.getPhotoId()));
        });
    }
    public void setParentController(In_Friend_Album_controller inFriendAlbumController) {
        this.inFriendAlbumController = inFriendAlbumController;
    }

    public void setFriendUser(Users friendUser){
        this.friendUser = friendUser;
    }

    public void setImgList(List<Photos> list){
        imgList = list;
    }

    public void setComment(String comment){
        updatedComment.setText(comment);
    }

    public void setImage(Image img){
        currPhoto.setPhoto(img);
        photo.setImage(img);
    }

    public void setCurrPhoto(Photos currPhoto){
        this.currPhoto = currPhoto;
    }

    public void comment(){
        if(commentText.getText().isEmpty()){
            warning.setText("Enter a comment!");
        } else {
            DatabaseConnection db = new DatabaseConnection();
            updatedComment.setText("Comment: "+commentText.getText());
            currPhoto.setComment(commentText.getText());
            db.addComment(currPhoto, currUser);
            comment.setDisable(true);
        }
    }
}
