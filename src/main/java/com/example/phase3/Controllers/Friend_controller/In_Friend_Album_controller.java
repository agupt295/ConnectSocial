package com.example.phase3.Controllers.Friend_controller;

import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Photos;
import com.example.phase3.Classes.Albums;
import Database.DatabaseConnection;
import com.example.phase3.RunApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class In_Friend_Album_controller {

    @FXML
    private Label albumName;

    @FXML
    private Button back;

    @FXML
    private FlowPane images;

    @FXML
    private AnchorPane mainAnchorPane;

    public Users currUser = new Users();

    public Users friendUser = new Users();

    public Albums albumSelected = new Albums();

    public ArrayList<Albums> friendAlbumObj = new ArrayList<Albums>();

    public ArrayList<String> friendString = new ArrayList<String>();

    public ArrayList<Albums> currAlbumsObj = new ArrayList<Albums>();

    public ArrayList<String> currAlbums = new ArrayList<String>();

    List<Photos> imgList = null;

    String[] topContributorsList = new String[10];

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public void setImgList(List<Photos> list){
        imgList = list;
    }

    public void refresh(){
        try{
            images.getChildren().clear();
            DatabaseConnection db = new DatabaseConnection();
            for(int i = 0; i < imgList.size(); i++){
                Image img = imgList.get(i).getPhoto();
                FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("friend_single_photo.fxml"));
                Pane pane = fxmlLoader.load();
                Friend_Single_Photo_controller singlePhoto = fxmlLoader.getController();
                singlePhoto.setParentController(this);
                singlePhoto.setCurrPhoto(imgList.get(i));
                singlePhoto.setCurrUser(currUser);
                singlePhoto.setFriendUser(friendUser);
                singlePhoto.setImgList(imgList);
                singlePhoto.setImage(img);
                singlePhoto.currPhoto.setPhoto(img);
                singlePhoto.likesCount.setText("Total Likes: "+db.likesCount(imgList.get(i).getPhotoId()));
                singlePhoto.caption.setText(db.getCaption(imgList.get(i).getPhotoId()).toUpperCase());
                if(db.userCommentPresent(singlePhoto.currPhoto.getPhotoId(), currUser)){
                    singlePhoto.setComment(db.getComment(imgList.get(i).getPhotoId(), currUser));
                    singlePhoto.comment.setDisable(true);
                }
                if(db.likeFound(singlePhoto.currPhoto.getPhotoId(), currUser)){
                    singlePhoto.like.setSelected(true);
                    singlePhoto.like.setDisable(true);
                    System.out.println(singlePhoto.currPhoto.getPhotoId()+" photo_id is liked by user");
                }
                images.getChildren().add(pane);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void setFriendUser(Users friend){
        friendUser = friend;
    }

    public void setCurrUser(Users user){
        this.currUser = user;
    }

    public void setAlbumSelected(Albums album){
        albumSelected = album;
        albumName.setText(albumSelected.getAlbumName());
    }

    public void setFriendAlbumObj(ArrayList<Albums> albums){
        friendAlbumObj = albums;
    }

    public void setCurrAlbums(ArrayList<String> albums){
        currAlbums = albums;
    }

    public void setCurrAlbumsObj(ArrayList<Albums> AlbumObj){
        currAlbumsObj = AlbumObj;
    }

    public void setFriendList(ArrayList<String> list){
        friendString = list;
    }

    public void back(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("friend_profile.fxml"));
            AnchorPane pane = fxmlLoader.load();
            Friend_Profile_controller friendProfile = fxmlLoader.getController();
            friendProfile.setFriendAlbumListObj(friendAlbumObj);
            friendProfile.setFriendUser(friendUser);
            friendProfile.setCurrFriends(friendString);
            friendProfile.setCurrUser(currUser);
            friendProfile.setCurrAlbumList(currAlbums);
            friendProfile.setCurrAlbumsObj(currAlbumsObj);
            friendProfile.setContriList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
