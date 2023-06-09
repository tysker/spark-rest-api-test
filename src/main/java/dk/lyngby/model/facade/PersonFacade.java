package dk.lyngby.model.facade;

import dk.lyngby.service.dto.PersonDTO;
import dk.lyngby.model.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PersonFacade {

        private static PersonFacade instance;
        private static EntityManagerFactory emf;

        private PersonFacade() {}

        public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
            if (instance == null) {
                emf = _emf;
                instance = new PersonFacade();
            }
            return instance;
        }

        private EntityManager getEntityManager() {
            return emf.createEntityManager();
        }

        public List<PersonDTO> getAll() {
            try (EntityManager em = emf.createEntityManager()) {
                TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
                List<Person> personList = query.getResultList();
                return PersonDTO.getPersonDTOs(personList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public PersonDTO getById(int id) {
            EntityManager em = emf.createEntityManager();
            Person person = em.find(Person.class, id);
            if (person == null)
                throw new EntityNotFoundException("The Person entity with ID: " + id + " Was not found");
            PersonDTO personDTO = new PersonDTO(person.getEmail(), person.getFirstName(), person.getLastName());
            System.out.println(personDTO);
            return personDTO;
        }


        public PersonDTO create(PersonDTO personDTO) {
            Person person = new Person(personDTO.getEmail(), personDTO.getFirstName(), personDTO.getLastName());
            try (EntityManager em = getEntityManager()) {
                em.getTransaction().begin();
                em.persist(person);
                em.getTransaction().commit();
            }
            person.setId(person.getId());
            return new PersonDTO(person);
        }

        public void delete(int id) {
            EntityManager em = getEntityManager();
            Person person = em.find(Person.class, id);
            if (person == null)
                throw new EntityNotFoundException("The Person entity with ID: " + id + " Was not found");
            try {
                em.getTransaction().begin();
                em.remove(em.find(Person.class, id));
                em.getTransaction().commit();
            } finally {
                em.close();
            }
        }

        public PersonDTO edit() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

}
