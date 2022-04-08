package ece651.riskgame.shared;

import java.io.Serializable;

public class UserInit implements Serializable {
    private boolean is_login;
    private String Username;
    private String Password;

    public UserInit(boolean is_login, String username, String password) {
        this.is_login = is_login;
        Username = username;
        Password = password;
    }

    public UserInit() {
        is_login = false;
        Username = "";
        Password = "";
    }

    public boolean isIs_login() {
        return is_login;
    }

    public void setIs_login(boolean is_login) {
        this.is_login = is_login;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
