package com.example.phase3;

import Database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Add_Image_controller {
    @FXML
    private Button back;

    @FXML
    private TextField caption;

    @FXML
    private Button upload;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    public ListView<String> chooseAlbum;

    @FXML
    private ImageView photo;

    @FXML
    private Label warning = new Label();

    public Users currUser;

    public ArrayList<String> buttonList;

    public ArrayList<Albums> albumObjList;

    public boolean photoSelected = true;

    String[] topContributorsList = new String[10];

    Image image = null;

    public void setContriList(String[] list){
        topContributorsList = list;
    }
    public void setButtonList(ArrayList<String> list){
        buttonList = list;
        ObservableList<String> viewList = FXCollections.observableArrayList(buttonList);
        chooseAlbum.setItems(viewList);
    }

    public void setAlbumObjList(ArrayList<Albums> list){
        albumObjList = list;
    }

    public void setUser (Users currUser){
        this.currUser = currUser;
    }

    public void profile() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
            AnchorPane pane = fxmlLoader.load();
            User_Profile_controller ctrl = fxmlLoader.getController();
            ctrl.setUser(currUser);
            ctrl.setButtonList(buttonList);
            ctrl.setAlbumObj(albumObjList);
            ctrl.setTopContributorsList(topContributorsList);
            mainAnchorPane.getChildren().setAll(pane);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void uploadImage(){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Photo");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(upload.getScene().getWindow());
            if (selectedFile != null) {
                image = new Image(selectedFile.toURI().toString());
                photo.setImage(image);
                photo.setPreserveRatio(true);
                photo.setFitWidth(300);
            } else {
                photoSelected = false;
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void checkCaptionAndUploadImage(){
        try{
            if(!photoSelected){
                warning.setText("Select a photo!");
            } else if (chooseAlbum.getSelectionModel().getSelectedItem() == null){
                warning.setText("Choose an Album!");
            } else if(caption.getText().equals("")){
                warning.setText("Enter a caption!");
            } else {
                DatabaseConnection db = new DatabaseConnection();
                db.addPhoto(caption.getText(), chooseAlbum.getSelectionModel().getSelectedItem(), image.getUrl().substring(5));
                profile();
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
