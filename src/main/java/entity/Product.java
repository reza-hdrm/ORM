package entity;

import annotation.Column;
import annotation.Entity;
import annotation.Id;
import annotation.Table;
import emum.DataType;

@Entity
@Table(name = "product_tbl")
public class Product {
    @Id
    @Column(name = "product_id", dataType = DataType.INT, size = 11)
    private int id;

    @Column(name = "name", dataType = DataType.VARCHAR)
    private String name;

    @Column(name = "price", dataType = DataType.INT, size = 11)
    private int price;

    @Column(name = "unitInStock", dataType = DataType.INT, size = 11)
    private int unitInStock;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unitInStock=" + unitInStock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (id != product.id) return false;
        if (price != product.price) return false;
        if (unitInStock != product.unitInStock) return false;
        return name != null ? name.equals(product.name) : product.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + unitInStock;
        return result;
    }
}
