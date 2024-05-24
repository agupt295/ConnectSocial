package com.example.phase3.Controllers;

import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Photos;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User_Profile_controller {
    @FXML
    private ImageView addAlbum;

    @FXML
    private ImageView addFriend;

    @FXML
    private ImageView addImage;

    @FXML
    private ImageView viewTaggedPhotos;

    @FXML
    private Pane viewFriend;

    @FXML
    private Button logout;

    @FXML
    private Button delete;

    @FXML
    private Button open;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ListView<String> albumList;

    @FXML
    private Label name = new Label("");

    @FXML
    private Label warning;

    @FXML
    private ListView<String> viewTopContributors;

    String[] topContributorsList = new String[10];

    private Users currUser;

    private ArrayList albumsString = new ArrayList<String>();

    public ArrayList<Albums> albumsObject = new ArrayList<Albums>();

    public String albumChosen;

    List<Photos> imgList = null;

    public void setTopContributorsList(String[] list){
        this.topContributorsList = list;
        ObservableList<String> viewList = FXCollections.observableArrayList(topContributorsList);
        viewTopContributors.setItems(viewList);
    }

    public void setUser(Users currUser){
        this.currUser = currUser;
        name.setText(currUser.getUserfName() + " " + currUser.getUserlName());
        albumList.setOnMouseClicked(event -> {
            albumChosen = albumList.getSelectionModel().getSelectedItem();
        });
    }

    public void setAlbumObj(ArrayList<Albums> list){
        albumsObject = list;
    }

    public void setButtonList(ArrayList<String> list){
        albumsString = list;
        ObservableList<String> viewList = FXCollections.observableArrayList(albumsString);
        albumList.setItems(viewList);
    }

    public void albumList(ArrayList<Albums> list){
        try{
            albumsString = new ArrayList<String>();
            albumsObject = new ArrayList<Albums>();
            DatabaseConnection db = new DatabaseConnection();
            for(int i = 0; i < list.size(); i++){
                albumsString.add(db.getAlbumName(list.get(i)));
                Albums obj = new Albums();
                obj.setAlbumName(db.getAlbumName(list.get(i)));
                albumsObject.add(obj);
            }
            ObservableList<String> viewList = FXCollections.observableArrayList(albumsString);
            albumList.setItems(viewList);
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    public void login() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("login.fxml"));
            AnchorPane pane = fxmlLoader.load();
            mainAnchorPane.getChildren().setAll(pane);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void addFriend() throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add_friend.fxml"));
            AnchorPane pane = fxmlLoader.load();
            Add_Friend_controller addFriendObj = fxmlLoader.getController();
            addFriendObj.setUser(currUser);
            addFriendObj.setButtonList(albumsString);
            addFriendObj.setAlbumObjList(albumsObject);
            addFriendObj.setContriList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void addAlbum() throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add_album.fxml"));
            AnchorPane pane = fxmlLoader.load();
            Add_Album_controller addAlbumObj = fxmlLoader.getController();
            addAlbumObj.setUser(currUser);
            addAlbumObj.setButtonList(albumsString);
            addAlbumObj.setAlbums(albumsObject);
            addAlbumObj.setContriList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void viewFriendsList() throws IOException{
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("friend_list.fxml"));
            AnchorPane pane = fxmlLoader.load();
            DatabaseConnection db = new DatabaseConnection();
            ArrayList<String> FriendList = db.getFriendList(currUser);
            FriendList_controller friendListObj = fxmlLoader.getController();
            friendListObj.setUser(currUser);
            friendListObj.setButtonList(albumsString);
            friendListObj.viewFriendButton(FriendList);
            friendListObj.setAlbumList(albumsObject);
            friendListObj.setContriList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void addImage() throws IOException{
        try{
            if(albumsObject.size() != 0){
                FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("add_image.fxml"));
                AnchorPane pane = fxmlLoader.load();
                Add_Image_controller addImageObj = fxmlLoader.getController();
                addImageObj.setUser(currUser);
                addImageObj.setButtonList(albumsString);
                addImageObj.setAlbumObjList(albumsObject);
                addImageObj.setContriList(topContributorsList);
                mainAnchorPane.getChildren().setAll(pane);
            } else {
                warning.setText("You need to add an album first!");
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void openAlbum() throws IOException {
        try{
            if(albumList.getSelectionModel().getSelectedItem() != null){
                DatabaseConnection db = new DatabaseConnection();
                imgList = db.viewPhotos(albumList.getSelectionModel().getSelectedItem());
                FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("in_user_album.fxml"));
                AnchorPane pane = fxmlLoader.load();
                In_User_Album_controller inAlbumObj = fxmlLoader.getController();
                inAlbumObj.setUser(currUser, albumList.getSelectionModel().getSelectedItem());
                inAlbumObj.setAlbumList(albumsString, albumList);
                inAlbumObj.setAlbumObj(albumsObject);
                inAlbumObj.setImgList(imgList);
                inAlbumObj.setContriList(topContributorsList);
                inAlbumObj.refresh(imgList);
                Albums albumObj = new Albums();
                for(int i = 0; i < albumsObject.size(); i++){
                    if((albumsObject.get(i).getAlbumName()).equals(albumList.getSelectionModel().getSelectedItem())){
                        albumObj.setAlbumName(albumList.getSelectionModel().getSelectedItem());
                        inAlbumObj.setAlbum(albumObj);
                    }
                }
                mainAnchorPane.getChildren().setAll(pane);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void deleteAlbum(){
        try{
            if(albumList.getSelectionModel().getSelectedItem() != null) {
                for(int i = 0; i < albumsString.size(); i++){
                    if(albumsString.get(i).equals(albumChosen)){
                        albumsObject.remove(i);
                    }
                }
                albumsString.remove(albumChosen);
                DatabaseConnection db = new DatabaseConnection();
                db.deleteAlbum(albumChosen, currUser);
                ObservableList<String> viewList = FXCollections.observableArrayList(albumsString);
                albumList.setItems(viewList);
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void goToTag() throws IOException {
        try{
            DatabaseConnection db = new DatabaseConnection();
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("tag_page.fxml"));
            AnchorPane pane = fxmlLoader.load();
            Tag_Page_controller tagPage = fxmlLoader.getController();
            tagPage.setContriList(topContributorsList);
            tagPage.setCurrUser(currUser);
            tagPage.setButtonList(albumsString);
            tagPage.setAlbumList(albumsObject);
            tagPage.printPopularTags(db.getPopularTags());
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
