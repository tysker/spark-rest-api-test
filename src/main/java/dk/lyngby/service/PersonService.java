package dk.lyngby.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dk.lyngby.service.dto.PersonDTO;
import dk.lyngby.model.facade.PersonFacade;
import dk.lyngby.config.EMF_Creator;
import jakarta.persistence.EntityManagerFactory;
import spark.Request;
import spark.Response;

public class PersonService {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE = PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public String getPersonById(Request request, Response response) {
        response.status(200);
        return GSON.toJson(FACADE.getById(Integer.parseInt(request.params(":id"))), PersonDTO.class);
    }

    public String getAllPersons(Request request, Response response) {
        response.status(200);
        return GSON.toJson(FACADE.getAll());
    }

    public String createPerson(Request request, Response response) {
        response.status(201);
        PersonDTO personDTO = GSON.fromJson(request.body(), PersonDTO.class);
        return GSON.toJson(FACADE.create(personDTO));
    }

    public String deletePerson(Request request, Response response) {
        response.status(200);
        FACADE.delete(Integer.parseInt(request.params(":id")));
        return GSON.toJson("Person deleted");
    }

    public String editPerson(Request request, Response response) {
        response.status(200);
        PersonDTO person = GSON.fromJson(request.body(), PersonDTO.class);
        return GSON.toJson("Not implemented yet");
    }

}
