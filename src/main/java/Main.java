import enties.User;
import orm.Session;
import orm.TableCreator;

public class Main {
    public static void main(String[] args) {
        TableCreator tableCreator = new TableCreator();
        tableCreator.create(new User());
        Session session = new Session();
        User user = new User();

        for (int i = 2; i <= 10; i++) {
            user.setId(i);
            user.setMobile(4454554 + i);
            user.setFirstName("reza" + i);
            user.setLastName("heydariMehr" + i);
            user.setEmail("reza@gamil.com" + i);
            session.save(user);
        }
    }
}
