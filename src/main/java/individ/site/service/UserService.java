package individ.site.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import individ.site.models.User;
import individ.site.repo.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;

    public User login(String username, String password) {
        User user = repo.findByUsernameAndPassword(username, password);
        return user;
    }

    public void register(User user) {
        // Data validation
        // if (user == null || user.getUsername() == null || user.getPassword() == null
        // || user.getFirstName() == null || user.getLastName() == null) {
        //     throw new IllegalArgumentException("Invalid user data");
        // }

        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Invalid user data");
        }

        // Check for duplicate user
        if (repo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Save user to the database
        repo.save(user);
    }
}
