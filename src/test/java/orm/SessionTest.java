package orm;

import entity.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionTest {
    private Session session;

    @Before
    public  void beforeClass() {
        session = new Session();
    }

    @After
    public void afterClass() {

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
        User userDB = (User) session.get(User.class, 8);
        session.commit();
        session.close();
        Assert.assertEquals(" ", userDB.getFirstName(), "Reza");
    }

    @Test
    public void update() {
        session.beginTransaction();
        User userDB = (User) session.get(User.class, 7);

    }

    @Test
    public void get() {
        session = new Session();
        session.beginTransaction();
        User userDB = (User) session.get(User.class, 7);
        Assert.assertEquals(" ", userDB.getFirstName(), "Reza");
        session.commit();
        session.close();
    }

    @Test
    public void delete() {
        //todo be test better
        session = new Session();
        session.beginTransaction();
        int id = 6;
        User userDB = (User) session.get(User.class, id);
        Assert.assertEquals("this user isn't found :", userDB.getFirstName(), "Reza");
        session.commit();
        session.delete(userDB);
        session.commit();
        userDB = (User) session.get(User.class, id);
        Assert.assertNotEquals("this user is deleted :", userDB.getFirstName(), "Reza");
        session.commit();
        session.close();
    }

    @Test
    public void findAll() {
    }
}