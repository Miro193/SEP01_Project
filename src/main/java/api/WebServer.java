package api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.UserDao;
import io.javalin.Javalin;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class WebServer {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        ObjectMapper objectMapper = new ObjectMapper();

        Javalin app = Javalin.create().start(7070);


        app.post("/login", ctx -> {
            try {

                Map<String, String> loginRequest = objectMapper.readValue(ctx.body(), Map.class);
                String username = loginRequest.get("username");
                String password = loginRequest.get("password");


                User user = userDao.login(username, password);

                if (user != null && user.getPassword().equals(password)) {
                    Map<String, String> successResponse = new HashMap<>();
                    successResponse.put("status", "success");
                    successResponse.put("message", "Login successful for user: " + username);
                    ctx.json(successResponse);
                } else {

                    ctx.status(401);
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("status", "error");
                    errorResponse.put("message", "Invalid username or password");
                    ctx.json(errorResponse);
                }
            } catch (Exception e) {
                ctx.status(500);
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("status", "error");
                errorResponse.put("message", "An unexpected error occurred: " + e.getMessage());
                ctx.json(errorResponse);
            }
        });
    }
}
