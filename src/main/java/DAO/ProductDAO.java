package DAO;

import entity.Product;
import entity.Product;
import orm.Session;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    Session session;

    public ProductDAO() {
        session = new Session();
    }

    public Product findById(int id) {
        session.beginTransaction();
        Product product = (Product) session.find(Product.class, id);
        session.close();
        return product;
    }

    public List<Product> loadAll() {
        session.beginTransaction();
        List<Object> objectList = session.findAll(Product.class);
        session.close();
        List<Product> products = new ArrayList<>();
        for (Object o : objectList) {
            products.add((Product) o);
        }
        return products;
    }

    public void update(Product product) {
        session.beginTransaction();
        session.update(product);
        session.commit();
        session.close();
    }

    public void save(Product product) {
        session.beginTransaction();
        session.save(product);
        session.commit();
        session.close();
    }

    public void delete(Product product) {
        session.beginTransaction();
        session.delete(product);
        session.commit();
        session.close();
    }
}
