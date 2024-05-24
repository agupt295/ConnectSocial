package com.example.phase3.Controllers;

import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Albums;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Add_Friend_controller {
    @FXML
    private Button back;

    @FXML
    private Button search;

    @FXML
    private Button addRecommended;

    @FXML
    private TextField searchText;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ListView<String> searchedFriend;

    @FXML
    private Label warning = new Label("");

    @FXML
    private ScrollPane youMayLike;

    @FXML
    private ListView<String> recommendations;

    @FXML
    private Button add;

    public Users currUser;

    public ArrayList<String> buttonList;

    public ArrayList<Albums> albumObjList;

    public String oneFriend;

    public String selectedItem = "";

    @FXML
    private Label warning1 = new Label();

    String[] topContributorsList = new String[10];

    public void setButtonList(ArrayList<String> list){
        buttonList = list;
    }

    public void setAlbumObjList(ArrayList<Albums> list){
        albumObjList = list;
    }

    public void setContriList(String[] list){
        topContributorsList = list;
    }

    public void setUser(Users currUser){
        this.currUser = currUser;
        DatabaseConnection db = new DatabaseConnection();
        List<String> recommendedFriends = db.getRecommendedFriend(currUser);
        ObservableList<String> viewList = FXCollections.observableArrayList(recommendedFriends);
        recommendations.setItems(viewList);
        List<Image> viewYouMayAlsoLike = db.YouMayAlsoLike(currUser);
        HBox pane = new HBox();
        pane.setPadding(new Insets(10, 0, 0, 0));
        pane.setSpacing(20);
        for(int i = 0; i < viewYouMayAlsoLike.size(); i++){
            ImageView img = new ImageView(viewYouMayAlsoLike.get(i));
            img.setFitWidth(200);
            img.setFitHeight(200);
            pane.getChildren().add(img);
        }
        youMayLike.setContent(pane);
    }

    public void backToProfile() throws IOException {
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

    public void searchFriend(){
        try{
            if(searchText.getText().isEmpty()){
                searchedFriend.setItems(null);
                warning.setText("Enter the email of the friend you want to add");
            } else {
                warning.setText("");
                DatabaseConnection db = new DatabaseConnection();
                oneFriend = db.searchFriendByEmail(searchText.getText());
                ObservableList<String> viewList = FXCollections.observableArrayList(oneFriend);
                searchedFriend.setItems(viewList);

                searchedFriend.setOnMouseClicked(event -> {
                    selectedItem = searchedFriend.getSelectionModel().getSelectedItem();
                    System.out.println("Selected item: " + selectedItem);
                });
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void addFriend(){
        try{
            if(!selectedItem.equals(null)){
                DatabaseConnection db = new DatabaseConnection();
                if(db.searchFriendByName(selectedItem, currUser)){
                    warning.setText("Friend is already added");
                } else {
                    db.addFriend(selectedItem, currUser);
                    warning.setText("The friend is added!");
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void setAddRecommended(){
        if(recommendations.getSelectionModel().getSelectedItem()!=null){
            DatabaseConnection db = new DatabaseConnection();
            db.addRecommended(currUser, recommendations.getSelectionModel().getSelectedItem());
            recommendations.getItems().remove(recommendations.getSelectionModel().getSelectedItem());
            warning1.setText("The friend is added!");
        }
    }

    public void setAddYouMayAlsoLike(){
        DatabaseConnection db = new DatabaseConnection();

    }
}
