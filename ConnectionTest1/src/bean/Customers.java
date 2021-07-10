package bean;


import java.sql.Date;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-15 22:53
 */
public class Customers {

    private int id;
    private String name;
    private String email;
    private Date birth;

    public Customers() {
    }

    public Customers(int id, String name, String email, Date date) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = date;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return birth;
    }

    public void setDate(Date date) {
        this.birth = date;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }
}
