package com.example.phase3.Controllers.User_controller;
import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Photos;
import com.example.phase3.Classes.Albums;
import Database.DatabaseConnection;
import com.example.phase3.RunApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.*;

public class In_User_Album_controller {
    @FXML
    private Label albumName;

    @FXML
    private Button back;

    @FXML
    private FlowPane viewPhotosList;

    @FXML
    private AnchorPane mainAnchorPane;

    public Users currUser;

    public ListView<String> albums;

    @FXML
    public ImageView displayImage;

    public ArrayList<String> strings;

    public Albums currAlbum;

    public ArrayList<Albums> albumList;

    List<Photos> imgList = null;

    String[] topContributorsList = new String[10];

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public void setUser(Users currUser, String albumName) {
        DatabaseConnection db = new DatabaseConnection();
        int album_id = db.getAlbumId(albumName);
        this.currUser = currUser;
    }

    public void setAlbum(Albums obj){
        currAlbum = obj;
        albumName.setText(currAlbum.getAlbumName());
    }

    public void setAlbumList(ArrayList<String> strings, ListView<String> list){
        this.strings = strings;
        albums = list;
    }

    public void setAlbumObj(ArrayList<Albums> obj){
        albumList = obj;
    }

    public void back() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
            AnchorPane pane = fxmlLoader.load();
            User_Profile_controller userProfile = fxmlLoader.getController();
            userProfile.setUser(currUser);
            userProfile.setButtonList(strings);
            userProfile.setAlbumObj(albumList);
            userProfile.setTopContributorsList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void setImgList(List<Photos> imgList){
        this.imgList = imgList;
    }

    public void refresh(List<Photos> imgList) throws IOException {
        try{
            viewPhotosList.getChildren().clear();
            DatabaseConnection db = new DatabaseConnection();
            for(int i = 0; i < imgList.size(); i++){
                Image img = imgList.get(i).getPhoto();
                FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("single_photo.fxml"));
                Pane pane = fxmlLoader.load();
                Single_Photo_controller singlePhoto = fxmlLoader.getController();
                singlePhoto.setParentController(this);
                singlePhoto.setCurrUser(currUser);
                singlePhoto.setImgList(imgList);
                singlePhoto.setCurrPhoto(imgList.get(i));
                singlePhoto.setImage(img);
                singlePhoto.currPhoto.setPhoto(img);
                singlePhoto.caption.setText(db.getCaption(imgList.get(i).getPhotoId()));
                singlePhoto.likesCount.setText("Total Likes: "+db.likesCount(imgList.get(i).getPhotoId()));
                singlePhoto.setTagList(db.getTagList(singlePhoto.currPhoto.getPhotoId()));
                viewPhotosList.getChildren().add(pane);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
