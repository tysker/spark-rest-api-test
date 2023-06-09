package utility;

import dk.lyngby.model.entities.Person;
import jakarta.persistence.EntityManager;

public class TestData {

    public static void createTestData(EntityManager em) {
        // Create test data here
        Person person1 = new Person("John", "Doe", "12345678");
        Person person2 = new Person("Jane", "Doe", "87654321");
        Person person3 = new Person("John", "Smith", "12348765");
        try (em; em) { // try-with-resources
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(person1);
            em.persist(person2);
            em.persist(person3);
            em.getTransaction().commit();
        }
    }
}
