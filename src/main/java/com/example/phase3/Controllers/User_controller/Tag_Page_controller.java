package com.example.phase3.Controllers.User_controller;
import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Albums;
import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;
import com.example.phase3.RunApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class Tag_Page_controller {
    @FXML
    private Button back;

    @FXML
    private Button findPhotos;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    public ListView<String> viewPopularTagList;

    @FXML
    private TextField searchTag;

    @FXML
    private Button showPopularTag;

    @FXML
    private RadioButton viewAllPhotos;

    @FXML
    private RadioButton viewMyPhotos;

    @FXML
    private ScrollPane viewPopularPhotos = new ScrollPane();

    @FXML
    private ScrollPane viewTaggedPhotos = new ScrollPane();

    @FXML
    private Button clear;

    @FXML
    private Button clear1;

    public Users currUser;

    public ArrayList<Albums> albumList = new ArrayList<Albums>();

    public ArrayList<String> albumStringList = new ArrayList<String>();

    String[] topContributorsList = new String[10];

    public void setCurrUser(Users currUser){
        this.currUser = currUser;
        viewAllPhotos.setSelected(true);
        viewMyPhotos.setOnMouseClicked(event -> {
            viewAllPhotos.setSelected(false);
            findTaggedPhotos();
        });
        viewAllPhotos.setOnMouseClicked(event -> {
            viewMyPhotos.setSelected(false);
            findTaggedPhotos();
        });
    }

    public void setButtonList(ArrayList<String> list){
        albumStringList = list;
    }

    public void setAlbumList(ArrayList<Albums> obj){
        albumList = obj;
    }

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public List<Image> taggedPhotos;

    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
        AnchorPane pane = fxmlLoader.load();
        User_Profile_controller userProfile = fxmlLoader.getController();
        userProfile.setTopContributorsList(topContributorsList);
        userProfile.setUser(currUser);
        userProfile.setButtonList(albumStringList);
        userProfile.setAlbumObj(albumList);
        mainAnchorPane.getChildren().setAll(pane);
    }

    public void findTaggedPhotos(){
        if(viewAllPhotos.isSelected()){
            if(!searchTag.getText().isEmpty()){
                DatabaseConnection db = new DatabaseConnection();
                taggedPhotos = db.getTaggedAllPhotos(searchTag.getText(), true, currUser);
                HBox pane = new HBox();
                pane.setPadding(new Insets(10, 0, 0, 0));
                pane.setSpacing(20);
                for(int i = 0; i < taggedPhotos.size(); i++){
                    ImageView img = new ImageView(taggedPhotos.get(i));
                    pane.getChildren().add(img);
                }
                viewTaggedPhotos.setContent(pane);
            }
        } else {
            if(!searchTag.getText().isEmpty()){
                DatabaseConnection db = new DatabaseConnection();
                taggedPhotos = db.getTaggedAllPhotos(searchTag.getText(), false, currUser);
                HBox pane = new HBox();
                pane.setPadding(new Insets(10, 0, 0, 0));
                pane.setSpacing(20);
                for(int i = 0; i < taggedPhotos.size(); i++){
                    ImageView img = new ImageView(taggedPhotos.get(i));
                    pane.getChildren().add(img);
                }
                viewTaggedPhotos.setContent(pane);
            }
        }
    }

    public void printPopularTags(List<String> list){
        ObservableList<String> viewList = FXCollections.observableArrayList(list);
        viewPopularTagList.setItems(viewList);
    }

    public void viewPopularTag(){
        if(viewPopularTagList.getSelectionModel().getSelectedItem() != null){
            DatabaseConnection db = new DatabaseConnection();
            List<Image> images = (db.viewPopularPhotoByTag(viewPopularTagList.getSelectionModel().getSelectedItem()));
            HBox pane = new HBox();
            pane.setPadding(new Insets(10, 0, 0, 0));
            pane.setSpacing(20);
            for(int i = 0; i < images.size(); i++){
                ImageView img = new ImageView(images.get(i));
                pane.getChildren().add(img);
            }
            viewPopularPhotos.setContent(pane);
        }
    }

    public void clear(){
        viewPopularPhotos.setContent(new Pane());
        viewPopularTagList.getSelectionModel().select(-1);
    }

    public void clear1(){
        viewTaggedPhotos.setContent(new Pane());
        searchTag.clear();
    }
}
