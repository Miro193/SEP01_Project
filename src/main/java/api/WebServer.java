package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import controller.LoginController;
import dao.UserDao;
import io.javalin.Javalin;
import model.User;

import java.util.Map;

public class WebServer {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        ObjectMapper objectMapper = new ObjectMapper();

        Javalin app = Javalin.create().start(7070);

        // Define the /login endpoint
        app.post("/login", ctx -> {
            try {
                // Get the username and password from the JSON request body
                Map<String, String> loginRequest = objectMapper.readValue(ctx.body(), Map.class);
                String username = loginRequest.get("username");
                String password = loginRequest.get("password");

                // Use the existing UserDao to authenticate the user
                User user = userDao.login(username, password);

                if (user != null && user.getPassword().equals(password)) {
                    // User authenticated successfully
                    ctx.json(Map.of("status", "success", "message", "Login successful for user: " + username));
                } else {
                    // Authentication failed
                    ctx.status(401); // Unauthorized
                    ctx.json(Map.of("status", "error", "message", "Invalid username or password"));
                }
            } catch (Exception e) {
                ctx.status(500); // Internal Server Error
                ctx.json(Map.of("status", "error", "message", "An unexpected error occurred: " + e.getMessage()));
            }
        });
    }
}
