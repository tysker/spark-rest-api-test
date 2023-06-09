package dk.lyngby.model.facade;

import dk.lyngby.exceptions.API_Exception;
import dk.lyngby.model.entities.Role;
import dk.lyngby.model.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import javax.naming.AuthenticationException;

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public User getVerifiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public User createUser(String username, String password, String user_role) throws API_Exception {
        EntityManager em = emf.createEntityManager();
        User user = new User(username, password);
        Role role = em.find(Role.class, user_role);

        if (role == null) {
            role = createRole(user_role);
        }

        user.addRole(role);

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new API_Exception(400, "Username already exists", e);
        } finally {
            em.close();
        }
        return user;
    }

    public Role createRole(String role_name) {
        EntityManager em = emf.createEntityManager();
        Role role = new Role(role_name);

        try {
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return role;
    }
}
