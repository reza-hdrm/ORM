package entity;

import annotation.Column;
import annotation.Id;
import annotation.Table;
import emum.DataType;

@Table(name = "user_tbl")
public class User {

    @Id
    @Column(name = "user_id", dataType = DataType.INT, size = 11)
    private int id;

    @Column(name = "first_name", dataType = DataType.VARCHAR)
    private String firstName;

    @Column(name = "last_name", dataType = DataType.VARCHAR)
    private String lastName;

    @Column(name = "email", dataType = DataType.VARCHAR)
    private String email;

    @Column(name = "mobile", dataType = DataType.INT)
    private int mobile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", mobile=" + mobile +
                '}';
    }
}

