package com.example.phase3;

import Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.util.List;

public class Single_Photo_controller {

    @FXML
    private ListView<String> otherComments;

    @FXML
    private ImageView photo;

    @FXML
    private Button searchComment;

    @FXML
    private TextField searchCommentText;

    @FXML
    private AnchorPane singlePhotoPane;

    @FXML
    private Label warning;

    @FXML
    private CheckBox delete;

    @FXML
    public Label caption;

    @FXML
    private TextField addTextTag;

    @FXML
    private Button tag;

    @FXML
    public Label likesCount = new Label("Total Likes: 0");

    @FXML
    private ListView<String> viewTags;

    public Users currUser;

    public Photos currPhoto = null;

    List<Photos> imgList = null;

    List<String> tagList = null;

    In_User_Album_controller inUserAlbumController;

    public void setCurrUser(Users currUser){
        this.currUser = currUser;
    }

    public void setTagList(List<String> list){
        tagList = list;
        ObservableList<String> viewList = FXCollections.observableArrayList(tagList);
        viewTags.setItems(viewList);
    }

    public void setImage(Image img){
        photo.setImage(img);
        currPhoto.setPhoto(img);
    }

    public void setCurrPhoto(Photos cPhoto){
        currPhoto = cPhoto;
    }

    public void update(){
        DatabaseConnection db = new DatabaseConnection();
        try{
            if(delete.isSelected()){
                imgList = db.deletePhotos(currPhoto.getPhotoId());
                inUserAlbumController.refresh(imgList);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void setImgList(List<Photos> images){
        imgList = images;
    }

    public void setParentController(In_User_Album_controller inUserAlbumController) {
        this.inUserAlbumController = inUserAlbumController;
    }

    public void searchComment(){
        try{
            if(searchCommentText.getText().isEmpty()){
                warning.setText("Enter a comment to search!");
            } else {
                DatabaseConnection db = new DatabaseConnection();
                ObservableList<String> viewList = FXCollections.observableArrayList(db.friendsMadeComment(searchCommentText.getText()));
                otherComments.setItems(viewList);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void addTag(){
        try{
            if(addTextTag.getText().isEmpty()){
                warning.setText("Add a Tag!");
            } else {
                DatabaseConnection db = new DatabaseConnection();
                warning.setText(null);
                db.addTag(addTextTag.getText(), currPhoto.getPhotoId());
                tagList = db.getTagList(currPhoto.getPhotoId());
                viewTags.getItems().clear();
                ObservableList<String> viewList = FXCollections.observableArrayList(tagList);
                viewTags.setItems(viewList);
                addTextTag.clear();
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
