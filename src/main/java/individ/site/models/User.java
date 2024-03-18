package individ.site.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
// import lombok.Data;

@Entity
@Table(name = "Users")
public class User extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    // private boolean active;

    // private String roles;

    public User() {
        // Default constructor
    }

    // public User(String username, String password) {
    //     this.username = username;
    //     this.password = password;
    // }

    public User(String username, String password, String firstName, String lastName) {
        super(firstName, lastName);
        this.username = username;
        this.password = password;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // public boolean isActive() {
    //     return active;
    // }

    // public void setActive(boolean active) {
    //     this.active = active;
    // }

    // public String getRoles() {
    //     return roles;
    // }

    // public void setRoles(String roles) {
    //     this.roles = roles;
    // }
    
}