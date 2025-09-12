package dataControler;
import datasourse.User;
import datasourse.ConnectionBD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class LoginController {
    @FXML private TextField usernameField;
    @FXML private TextField idField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLoginButton(ActionEvent event) {
        messageLabel.setText("");
        int id = Integer.parseInt(idField.getText());
        String username = usernameField.getText();
        try (Connection connection = ConnectionBD.GetConnection()) {
            String sql = "SELECT * FROM student WHERE id = ? AND username = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getInt("id"), rs.getString("username"));
                System.out.println("Logged in user: " + user.getName());
                boolean openWindow = false; // Cambia a true cuando quieras activarlo


                if (openWindow) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setTitle("Bienvenido");
                        stage.show();

                        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                messageLabel.setText("Incorrect ID or user.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error connecting to the database.");
        }



    }



    }


