package com.example.phase3;
import java.sql.*;

import Database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SignIn_controller {

    @FXML
    private TextField address;

    @FXML
    private DatePicker dob;

    @FXML
    private TextField email;

    @FXML
    private TextField fName;

    @FXML
    private RadioButton female;

    @FXML
    private TextField lName;

    @FXML
    private Button login;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private RadioButton male;

    @FXML
    private PasswordField password;

    @FXML
    private Button signin;

    @FXML
    private Label warning;

    public Users currUser;

    String[] topContributorsList = new String[10];

    public void login() throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("login.fxml"));
            AnchorPane pane = fxmlLoader.load();
            mainBorderPane.setCenter(pane);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void profile(ActionEvent event) throws IOException {
        try{
            if(fName.getText().isEmpty() || lName.getText().isEmpty() || (dob.getValue()==null) || email.getText().isEmpty() || address.getText().isEmpty() || password.getText().isEmpty() || (!male.isSelected() && !female.isSelected())){
                warning.setText("Enter all fields");
            } else {
                DatabaseConnection db = new DatabaseConnection();
                if(db.signin(email.getText())){
                    FXMLLoader fxmlLoader = new FXMLLoader(RunApplication.class.getResource("user_profile.fxml"));
                    AnchorPane pane = fxmlLoader.load();
                    currUser = new Users();
                    currUser.setUserfName(fName.getText());
                    currUser.setUserlName(lName.getText());
                    currUser.setUserEmail(email.getText());
                    currUser.setDob(dob.getValue().getYear(), dob.getValue().getMonthValue(), dob.getValue().getDayOfMonth());
                    if(male.isSelected()){ currUser.setGender("M"); } else if(female.isSelected()){ currUser.setGender("F"); }
                    currUser.setHometown(address.getText());
                    currUser.setPassword(password.getText());
                    db.addUser(currUser);
                    User_Profile_controller ctrl = fxmlLoader.getController();
                    topContributorsList = db.getTopContributorList();
                    ctrl.setTopContributorsList(topContributorsList);
                    ctrl.setUser(currUser);
                    mainBorderPane.setCenter(pane);
                } else {
                    warning.setText("Account with email: " + email.getText() + " already exists!");
                    password.setText(null);
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}