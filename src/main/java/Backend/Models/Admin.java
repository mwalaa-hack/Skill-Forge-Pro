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

    public void approveCourse(Course course) {
        course.setApprovalStatus(ApprovalStatus.APPROVED);
        course.addAudit("APPROVED",this.getUserId(),Instant.now().toString());
    }
    
    
    
    public void rejectCourse(Course course, String reason) {
        course.setApprovalStatus(ApprovalStatus.REJECTED);
        course.addAudit("REJECTED: " + reason,this.getUserId(),Instant.now().toString());
    }
    
    
    @Override
    public JSONObject toJSON() {
        return super.toJSON();
    }
    
}