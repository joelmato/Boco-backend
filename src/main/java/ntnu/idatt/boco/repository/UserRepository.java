package ntnu.idatt.boco.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ntnu.idatt.boco.model.User;
import ntnu.idatt.boco.security.Encryption;

@Repository
public class UserRepository {
    @Autowired private JdbcTemplate jdbcTemplate;

    public int saveUserToDatabase(User user) {
        byte[] salt = Encryption.getNextSalt();
        byte[] hashedPassword = Encryption.hash(user.getPassword(), salt);
        return jdbcTemplate.update("INSERT INTO users (fname, lname, password, email, salt) VALUES (?,?,?,?,?);",
                            new Object[] { user.getFname(), user.getLname(), hashedPassword, user.getEmail(), salt });
    }

    public byte[] getHashedPasswordByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT password FROM users WHERE email ='"+email+"';", byte[].class);
    }

    public byte[] getSaltByEmail(String email) {
        return jdbcTemplate.queryForObject("SELECT salt FROM users WHERE email ='"+email+"';", byte[].class);
    }

    public boolean existsByEmail(String email) {
        String query = "SELECT EXISTS(SELECT * FROM users WHERE email='"+email+"');";
        return jdbcTemplate.queryForObject(query, Boolean.class);
    }

    public int changePasswordInDatabase(User user, String newPassword){
        boolean correctPass = Encryption.isExpectedPassword(user.getPassword(), user.getSalt().getBytes(), getHashedPasswordByEmail(user.getEmail()));
        if(correctPass){
            return jdbcTemplate.update("UPDATE users SET password = ? WHERE user_id = ?;", newPassword, user.getUserId());
        }else{
            return 0;
        }
    }

    public int deleteUser(User user){
        boolean correctPass = Encryption.isExpectedPassword(user.getPassword(), user.getSalt().getBytes(), getHashedPasswordByEmail(user.getEmail()));
        if(correctPass) {
            return jdbcTemplate.update("DELETE FROM users WHERE email = ?;", new Object[] { user.getUserId() });
        } else {
            return 0;
        }
    }
}
