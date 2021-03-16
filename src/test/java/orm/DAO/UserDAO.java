package orm.DAO;

import orm.entity.User;
import orm.Session;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Session session;

    public UserDAO() {
        session = new Session();
    }

    public User findById(int id) {
        session.beginTransaction();
        User user = (User) session.find(User.class, id);
        session.close();
        return user;
    }

    public List<User> loadAll() {
        session.beginTransaction();
        List<Object> objectList = session.findAll(User.class);
        session.close();
        List<User> users = new ArrayList<>();
        for (Object o : objectList) {
            users.add((User) o);
        }
        return users;
    }

    public void update(User user) {
        session.beginTransaction();
        session.update(user);
        session.commit();
        session.close();
    }

    public void save(User user) {
        session.beginTransaction();
        session.save(user);
        session.commit();
        session.close();
    }

    public void delete(User user) {
        session.beginTransaction();
        session.delete(user);
        session.commit();
        session.close();
    }
}
