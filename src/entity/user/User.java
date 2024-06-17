package entity.user;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.db.AIMSDB;
import entity.media.Media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {

    private int id;
    private String name;
    private String email;
    private String address;
    private String phone;

    public User(int id, String name, String email, String address, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public User() {}

    // override toString method
    @Override
    public String toString() {
        return "{" +
                "  username='" + name + "'" +
                ", email='" + email + "'" +
                ", address='" + address + "'" +
                ", phone='" + phone + "'" +
                "}";
    }

    // getter and setter
    public  int getId() {return this.id;}

    public String getName() {
        return this.name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    static public User checkValidPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM User WHERE name = ?;";
        System.out.println(sql);
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, username);
        ResultSet res = stm.executeQuery();
        if (res.next()) {
            try {
                String queriedPassword = res.getString("password");
                if (password.equals(queriedPassword)) {
                    return new User(res.getInt("id"),
                            res.getString("name"),
                            res.getString("email"),
                            res.getString("address"),
                            res.getString("phone"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

        return null;
    }
}
