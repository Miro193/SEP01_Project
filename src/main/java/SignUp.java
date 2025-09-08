import java.util.Scanner;

public class SignUp {
    private String username;
    private String password;
    private String secondPassword;

    public SignUp(String username, String password, String secondPassword) {
        this.username = username;
        this.password = password;
        this.secondPassword = secondPassword;
    }

    // Getters and setters
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

    public String getSecondPassword() {
        return password;
    }

    public void setSecondPassword() {
        this.secondPassword = secondPassword;
    }

    // Optional: method to show login details (without exposing password)
    @Override
    public String toString() {
        return "SignUp{username='" + username + "'}";
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("UserName please: ");
//        String userName = scanner.nextLine();
//
//        System.out.print("Password please: ");
//        String password = scanner.nextLine();
//
//        System.out.print("Repeat password please: ");
//        String secondPassword = scanner.nextLine();
//
//        while (!secondPassword.equals(password)) {
//
//            System.out.println("Repassword was wrong! Write it again: ");
//            secondPassword = scanner.nextLine();
//
//        }
//
//        System.out.println("Username is: " + userName + "\nPassword is: " + password + "\nRePassword was correct.");
//    }
}
