package com.example.phase3;

import Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;
import java.util.List;

public class Friend_Profile_controller {
    @FXML
    private Button back;

    @FXML
    private ListView<String> friendAlbums;

    @FXML
    private Label friendName = new Label();

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button open;

    @FXML
    private Label warning;

    public Users currUser = new Users();

    public Users friendUser = new Users();

    public ArrayList currAlbums = new ArrayList<String>();

    public ArrayList<String> currFriends = new ArrayList<String>();

    public ArrayList currAlbumsObj = new ArrayList<Albums>();

    public ArrayList<String> friendAlbumsString = new ArrayList<String>();

    public ArrayList<Albums> friendAlbumsObj = new ArrayList<Albums>();

    public void setCurrUser(Users currUser){
        this.currUser = currUser;
    }

    List<Photos> imgList = null;

    public void setFriendUser(Users user){
        this.friendUser = user;
        friendName.setText(friendUser.getUserfName()+" "+friendUser.getUserlName());
        DatabaseConnection db = new DatabaseConnection();
        friendAlbumsObj = db.getAlbumList(friendUser);
        friendAlbumsString = new ArrayList<String>();
        for(int i = 0; i < friendAlbumsObj.size(); i++){
            friendAlbumsString.add(db.getAlbumName(friendAlbumsObj.get(i)));
        }
        ObservableList<String> viewList = FXCollections.observableArrayList(friendAlbumsString);
        friendAlbums.setItems(viewList);
    }

    public void setCurrFriends(ArrayList<String> friends){
        currFriends = friends;
    }

    public void setCurrAlbumList(ArrayList<String> albums){
        this.currAlbums = albums;
    }

    public void setCurrAlbumsObj(ArrayList <Albums> albumObj){
        this.currAlbumsObj = albumObj;
    }

    String[] topContributorsList = new String[10];

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public void setFriendAlbumListObj(ArrayList<Albums> albumsObj){
        DatabaseConnection db = new DatabaseConnection();
        this.friendAlbumsObj = albumsObj;
        for(int i = 0; i < friendAlbumsObj.size(); i++){
            friendAlbumsString.add(db.getAlbumName(friendAlbumsObj.get(i)));
        }
    }

    public void backBtn(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("friend_list.fxml"));
            AnchorPane pane = fxmlLoader.load();
            FriendList_controller friendListObj = fxmlLoader.getController();
            friendListObj.setUser(currUser);
            friendListObj.viewFriendButton(currFriends);
            friendListObj.setAlbumList(currAlbumsObj);
            friendListObj.setButtonList(currAlbums);
            friendListObj.setContriList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void OpenAlbum(){
        try{
            if(friendAlbums.getSelectionModel().getSelectedItem() != null){
                DatabaseConnection db = new DatabaseConnection();
                imgList = db.viewPhotos(friendAlbums.getSelectionModel().getSelectedItem());
                FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("in_friend_album.fxml"));
                AnchorPane pane = fxmlLoader.load();
                In_Friend_Album_controller inFriendAlbumController = fxmlLoader.getController();
                inFriendAlbumController.setFriendUser(friendUser);
                inFriendAlbumController.setFriendAlbumObj(friendAlbumsObj);
                inFriendAlbumController.setFriendList(currFriends);
                inFriendAlbumController.setCurrUser(currUser);
                inFriendAlbumController.setCurrAlbums(currAlbums);
                inFriendAlbumController.setCurrAlbumsObj(currAlbumsObj);
                inFriendAlbumController.setImgList(imgList);
                inFriendAlbumController.refresh();
                Albums album = new Albums();
                album.setAlbumName(friendAlbums.getSelectionModel().getSelectedItem());
                inFriendAlbumController.setAlbumSelected(album);
                inFriendAlbumController.setContriList(topContributorsList);
                mainAnchorPane.getChildren().setAll(pane);
            } else if(friendAlbumsString.size() == 0) {
                warning.setText(friendUser.getUserfName() + " " + friendUser.getUserlName()+ " does not have any albums/photo stored");
            } else {
                warning.setText("You need to select an Album first");
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
