package ntnu.idatt.boco.usrRepo;

import ntnu.idatt.boco.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}