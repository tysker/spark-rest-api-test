package dk.lyngby.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dk.lyngby.exceptions.API_Exception;
import dk.lyngby.exceptions.ExceptionDTO;
import dk.lyngby.model.facade.UserFacade;
import dk.lyngby.model.entities.User;
import dk.lyngby.security.TokenFactory;
import dk.lyngby.config.EMF_Creator;
import jakarta.persistence.EntityManagerFactory;
import spark.Request;
import spark.Response;

import javax.naming.AuthenticationException;

public class AuthenticationService {

    private final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
    private final TokenFactory TOKEN_FACTORY = TokenFactory.getInstance();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    public Object login(Request request, Response response) {
        try {
            String jsonString = request.body();
            String[] userInfos = TOKEN_FACTORY.parseJsonObject(jsonString, true);
            User user = USER_FACADE.getVerifiedUser(userInfos[0], userInfos[1]);
            String token = TOKEN_FACTORY.createToken(userInfos[0], user.getRolesAsStrings());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", userInfos[0]);
            responseJson.addProperty("token", token);
            response.status(200);
            return GSON.toJson(responseJson);
        } catch (AuthenticationException e) {
            return GSON.toJson(new ExceptionDTO(401, e.getMessage()));
        } catch (API_Exception e) {
            return GSON.toJson(new ExceptionDTO(e.getErrorCode(), e.getMessage()));
        }

    }

    public Object register(Request request, Response response)  {
        try {
            String jsonString = request.body();
            String[] userInfos = TOKEN_FACTORY.parseJsonObject(jsonString, false);

            User user = USER_FACADE.createUser(userInfos[0], userInfos[1], userInfos[2]);
            String token = TOKEN_FACTORY.createToken(userInfos[0], user.getRolesAsStrings());

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", userInfos[0]);
            responseJson.addProperty("token", token);
            response.status(201);
            return GSON.toJson(responseJson);
        } catch (API_Exception e) {
            return GSON.toJson(new ExceptionDTO(e.getErrorCode(), e.getMessage()));
        }
    }
}
