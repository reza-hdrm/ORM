package orm;

import entity.User;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SessionTest {
    Session session;

    @Test
    public void save() {
        session = new Session();
        session.beginTransaction();
        User user = new User();
        user.setFirstName("Reza");
        user.setLastName("Heydarimehr");
        user.setEmail("rezacolonelup@gmail.com");
        user.setMobile(933991484);
        session.save(user);
        User userDB = (User) session.get(User.class, 1);
        session.commite();
        session.close();
        Assert.assertEquals(" ", userDB.getFirstName(), "Reza");
    }

    @Test
    public void update() {
    }

    @Test
    public void get() {
        session = new Session();
        session.beginTransaction();
        User userDB = (User) session.get(User.class, 3);
        Assert.assertEquals(" ", userDB.getFirstName(), "Reza");
        session.commite();
        session.close();
    }

    @Test
    public void delete() {
        //todo complete
        session = new Session();
        session.beginTransaction();
        User userDB = (User) session.get(User.class, 3);
        Assert.assertEquals(" ", userDB.getFirstName(), "Reza");
        session.delete(userDB);
        session.commite();
        session.close();
        Assert.assertNull(" ", userDB.getFirstName());
    }

    @Test
    public void findAll() {
    }
}