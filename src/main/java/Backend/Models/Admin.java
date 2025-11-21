package Backend.Models;

import java.time.Instant;
import org.json.JSONObject;

/**
 *
 * @author Mohamed Walaa
 */
public class Admin extends User {

    public Admin(int userId, String username, String email, String password) {
        super(userId, username, email, password, "admin");
    }

    public Admin(JSONObject j) {
        super(j);
    }
    
    
    @Override
    public String getRole() {
        return "admin";
    }
   
    @Override
    public JSONObject toJSON() {
        return super.toJSON();
    }
    
}