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
}
