package orm;

import entity.User;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class SessionTest {
    Session session;
    @Test
    public void save() {
        session=new Session();
        User user=new User();
        user.setFirstName("Reza");
        user.setLastName("Heydarimehr");
        user.setEmail("rezacolonelup@gmail.com");
        user.setMobile(933991484);
        session.save(user);
        User userDB=(User)session.get(User.class,1);
        Assert.assertEquals(" ",userDB.getFirstName(),"Reza");
    }

    @Test
    public void update() {
    }

    @Test
    public void get() {
        session=new Session();
        User userDB=(User)session.get(User.class,1);
        Assert.assertEquals(" ",userDB.getFirstName(),"Reza");
    }

    @Test
    public void delete() {
    }

    @Test
    public void findAll() {
    }
}