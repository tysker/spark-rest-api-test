package dk.lyngby.security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.lyngby.service.dto.UserDTO;
import spark.Request;
import spark.Response;

import javax.naming.AuthenticationException;

import static spark.Spark.halt;

public class Authentication {
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();

    public void authenticate(Request request, Response response) throws AuthenticationException {

        String token = request.headers("Authorization").split(" ")[1];

        if(token == null){
            halt(401, GSON.toJson("You are not authorized to access this resource"));
        }

        try {
            UserDTO user = TOKEN_FACTORY.verifyToken(token);
        } catch (AuthenticationException exception) {
            halt(401, GSON.toJson("Not authenticated - do login"));
        }
    }
}
