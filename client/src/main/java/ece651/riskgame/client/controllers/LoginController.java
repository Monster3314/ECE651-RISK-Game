package ece651.riskgame.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ece651.riskgame.shared.UserInit;

public class LoginController {

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    private UserInit userInit;

    public LoginController(UserInit userInit) {
        this.userInit = userInit;
    }

    @FXML
    void login(ActionEvent event) {
        userInit.setIs_login(true);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());
    }

    @FXML
    void register(ActionEvent event) {
        userInit.setIs_login(false);
        userInit.setUsername(username.getText());
        userInit.setPassword(password.getText());
    }

}
