package com.example.phase3.Controllers.Authentication_controller;

import com.example.phase3.Classes.Users;
import com.example.phase3.Classes.Albums;
import Database.DatabaseConnection;
import com.example.phase3.Controllers.User_controller.User_Profile_controller;
import com.example.phase3.RunApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Login_controller {
    @FXML
    public TextField email;

    @FXML
    private Button login;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private PasswordField password;

    @FXML
    private Button signin;

    @FXML
    private Label warning;

    public Users currUser;

    String[] topContributorsList = new String[10];

    public void sign_in() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("sign-in.fxml"));
            BorderPane pane = fxmlLoader.load();
            mainAnchorPane.getChildren().setAll(pane);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void profile() throws IOException{
        try{
            if(email.getText().isEmpty() || password.getText().isEmpty()){
                warning.setText("Fill in all the blanks!");
            } else {
                DatabaseConnection db = new DatabaseConnection();
                if(db.login(email.getText(), password.getText())){
                    currUser = new Users(email.getText());
                    currUser.setUserfName(db.getProfilefName(email.getText()));
                    currUser.setUserlName(db.getProfilelName(email.getText()));
                    ArrayList albums = new ArrayList<Albums>();
                    albums = db.getAlbumList(currUser);
                    FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
                    AnchorPane pane = fxmlLoader.load();
                    User_Profile_controller ctrl = fxmlLoader.getController();
                    topContributorsList = db.getTopContributorList();
                    ctrl.setTopContributorsList(topContributorsList);
                    ctrl.setUser(currUser);
                    ctrl.albumList(albums);
                    mainAnchorPane.getChildren().setAll(pane);
                } else {
                    warning.setText("The entered credentials are incorrect!");
                    password.setText(null);
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
}
