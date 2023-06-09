package routes.authentication;

import dk.lyngby.Main;
import dk.lyngby.config.EMF_Creator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spark.Spark;
import utility.CreateAuthenticationData;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AuthenticationResourceTest {

    @BeforeAll
    public static void setupClass() {
        Main.main(new String[]{"7778"});
        EMF_Creator.startFacadeWithTestDb();
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactoryForTest();
        CreateAuthenticationData.createTestData(emf.createEntityManager());
    }

    @AfterAll
    public static void tearDownClass() {
        EMF_Creator.endFacadeWithTestDb();
        Spark.stop();
    }

    @Test
    @DisplayName("Login test - returns token")
    void loginTest() {
        String json = String.format("{username: \"%s\", password: \"%s\"}", "usertest", "user123");

        var response = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7778/api/v1/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .body("username", equalTo("usertest"));
        response.log().body();
    }

    @Test
    @DisplayName("Register test - returns token")
    void registerTest() {
        String json = String.format("{username: \"%s\", password: \"%s\", role: \"%s\"}", "register", "register123", "user");

        var responds = given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("http://localhost:7778/api/v1/register")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .body("username", equalTo("register"));
        responds.log().body();
    }

}
