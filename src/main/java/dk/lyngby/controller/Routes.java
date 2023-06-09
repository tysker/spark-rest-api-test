package dk.lyngby.controller;

import dk.lyngby.security.Authentication;
import dk.lyngby.service.AuthenticationService;
import dk.lyngby.service.PersonService;

import static spark.Spark.*;

public class Routes {
    private static final PersonService service = new PersonService();
    private static final AuthenticationService authenticationService = new AuthenticationService();
    private static final Authentication authentication = new Authentication();
    private static final String API_CONTEXT = "/api/v1";

    public static void initializeRoutes() {
        path(API_CONTEXT, () -> {
            path("/", () -> {
                post("/login", authenticationService::login);
                post("/register", authenticationService::register);
            });
            path("/persons", () -> {
                before("/*", authentication::authenticate);
                post("/", service::createPerson);
                get("/", service::getAllPersons);
                get("/:id", service::getPersonById);
                patch("/:id", service::editPerson);
                delete("/:id", service::deletePerson);
            });
        });
    }
}
