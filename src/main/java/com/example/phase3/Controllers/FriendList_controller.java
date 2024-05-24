package com.example.phase3.Controllers;

import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Albums;
import Database.DatabaseConnection;
import com.example.phase3.RunApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class FriendList_controller {
    @FXML
    private Button back;

    @FXML
    private Label friendCount;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ListView<String> friendList;

    @FXML
    private Button viewProfile;

    @FXML
    private Label warning;

    public Users currUser = new Users();

    public Users friendUser = new Users();

    public ArrayList<String> albumStringList = new ArrayList<String>();

    public ArrayList<Albums> albumList = new ArrayList<Albums>();

    String[] topContributorsList = new String[10];

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public void setUser(Users currUser){
        this.currUser = currUser;
    }

    public void setButtonList(ArrayList<String> list){
        albumStringList = list;
    }

    public ArrayList<String> friendsList;

    public void setAlbumList(ArrayList<Albums> list){
        this.albumList = list;
    }

    public void viewFriendButton(ArrayList<String> list){
        friendsList = list;
        ObservableList<String> viewList = FXCollections.observableArrayList(friendsList);
        friendList.setItems(viewList);
    }
    public void backToProfile(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
            AnchorPane pane = fxmlLoader.load();
            User_Profile_controller ctrl = fxmlLoader.getController();
            ctrl.setUser(currUser);
            ctrl.setButtonList(albumStringList);
            ctrl.setAlbumObj(albumList);
            ctrl.setTopContributorsList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void viewFriendProfile(){
        try{
            if(friendList.getSelectionModel().getSelectedItem() != null){
                DatabaseConnection db = new DatabaseConnection();
                FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("friend_profile.fxml"));
                AnchorPane pane = fxmlLoader.load();
                Friend_Profile_controller friendProfileObj = fxmlLoader.getController();
                friendProfileObj.setCurrUser(currUser);
                String names = friendList.getSelectionModel().getSelectedItem();
                String[] name = names.split(" ");
                friendUser = new Users();
                friendUser.setUserfName(name[0]);
                friendUser.setUserlName(name[1]);
                friendUser.setUserEmail(db.getUserEmail(friendUser.getUserfName(), friendUser.getUserlName()));
                friendProfileObj.setFriendUser(friendUser);
                friendProfileObj.setCurrAlbumList(albumStringList);
                friendProfileObj.setCurrAlbumsObj(albumList);
                friendProfileObj.setCurrFriends(friendsList);
                friendProfileObj.setContriList(topContributorsList);
                mainAnchorPane.getChildren().setAll(pane);
            } else {
                warning.setText("Select a friend to view their profile");
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
