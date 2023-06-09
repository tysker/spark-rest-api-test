package dk.lyngby.service;

import dk.lyngby.Main;
import dk.lyngby.config.EMF_Creator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spark.Spark;
import utility.LoginToken;
import utility.TestData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class PersonServiceTest {

    private static final LoginToken LOGIN_TOKEN = LoginToken.getInstance();
    private static Object userToken;

    @BeforeAll
    static void setUpAll() {
        EMF_Creator.startFacadeWithTestDb();
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactoryForTest();
        TestData.createTestData(emf.createEntityManager());
        Main.main(new String[]{"7778"});
        userToken =  LOGIN_TOKEN.getUserToken();
    }

    @AfterAll
    static void tearDownAll() {
        EMF_Creator.endFacadeWithTestDb();
        Spark.stop();
    }

    @Test
    @DisplayName("GET /customers")
    void getAllPersons() {
        given()
                .when()
                .header("Authorization", userToken)
                .get("http://localhost:7778/api/v1/persons/")
                .then()
                .statusCode(200)
                .body("size()", equalTo(3));
    }

    @Test
    @DisplayName("GET /persons/{id}")
    void getPersonById() {
        var responds = given()
                .when()
                .header("Authorization", userToken)
                .get("http://localhost:7778/api/v1/persons/")
                .then()
                .extract()
                .response();

        var personId = responds.jsonPath().getString("[0]");

        given()
                .when()
                .header("Authorization", userToken)
                .get("http://localhost:7778/api/v1/persons/" + personId)
                .then().statusCode(200);
    }

    @Test
    void getAllCustomers() {
    }

    @Test
    void createCustomer() {
    }
}