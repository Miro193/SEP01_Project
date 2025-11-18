package model;

public class User {
    private int userID;
    private String username;
    private String password;
    private String confirmPassword;

    public User( String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }


    @Override
    public String toString() {
        return "User{id=" + userID + ", username='" + username + "'}";
    }
}
