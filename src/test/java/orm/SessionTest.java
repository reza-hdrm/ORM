package orm;

import orm.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class SessionTest {
    private Session session;

    @BeforeEach
    public void beforeClass() {
        session = new Session();
    }

    @Test
    public void save() {
        session.beginTransaction();
        User user = new User();
        user.setFirstName("Reza");
        user.setLastName("Heydarimehr");
        user.setEmail("rezacolonelup@gmail.com");
        user.setMobile(933991484);
        session.save(user);
        User userDB = (User) session.find(User.class, 8);
        session.commit();
        session.close();
        Assertions.assertEquals(" ", userDB.getFirstName(), "Reza");
    }

    @Test
    public void update() {
        session.beginTransaction();
        User userDB = (User) session.find(User.class, 7);

    }

    @Test
    public void find() {
        session = new Session();
        session.beginTransaction();
        User userDB = (User) session.find(User.class, 7);
        Assertions.assertEquals(" ", userDB.getFirstName(), "Reza");
        session.commit();
        session.close();
    }

    @Test
    public void delete() {
        //todo be test better
        session = new Session();
        session.beginTransaction();
        int id = 6;
        User userDB = (User) session.find(User.class, id);
        Assertions.assertEquals("this user isn't found :", userDB.getFirstName(), "Reza");
        session.commit();
        session.delete(userDB);
        session.commit();
        userDB = (User) session.find(User.class, id);
        Assertions.assertNotEquals("this user is deleted :", userDB.getFirstName(), "Reza");
        session.commit();
        session.close();
    }

}