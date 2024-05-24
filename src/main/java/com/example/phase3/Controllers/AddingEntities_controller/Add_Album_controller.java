package com.example.phase3.Controllers.AddingEntities_controller;

import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Albums;
import Database.DatabaseConnection;
import com.example.phase3.Controllers.User_controller.User_Profile_controller;
import com.example.phase3.RunApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.ArrayList;

public class Add_Album_controller {
    @FXML
    private TextField albumName;

    @FXML
    private Button back;

    @FXML
    private Button create;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Label warning = new Label("");

    String[] topContributorsList = new String[10];

    public Users currUser;

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public void setButtonList(ArrayList<String> list){
        AlbumButtonList = list;
    }

    public ArrayList<String> AlbumButtonList;

    public ArrayList<Albums> albumsObjList;

    public void setUser(Users currUser){
        this.currUser = currUser;
    }

    public void setAlbums(ArrayList<Albums> list){
        albumsObjList = list;
    }

    public void profile() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
            AnchorPane pane = fxmlLoader.load();
            User_Profile_controller ctrl = fxmlLoader.getController();
            ctrl.setUser(currUser);
            ctrl.setButtonList(AlbumButtonList);
            ctrl.setAlbumObj(albumsObjList);
            ctrl.setTopContributorsList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void addAlbum(){
        DatabaseConnection db = new DatabaseConnection();
        try{
            if(albumName.getText().isEmpty()){
                warning.setText("Fill in the Album Name!");
            } else if(db.albumPresent(albumName.getText())){
                warning.setText("Album already present");
            } else {
                Albums newAlbum = new Albums();
                newAlbum.setAlbumName(albumName.getText());
                db = new DatabaseConnection();
                db.addAlbum(newAlbum, currUser);
                AlbumButtonList.add(albumName.getText());
                albumsObjList.add(newAlbum);
                profile();
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
