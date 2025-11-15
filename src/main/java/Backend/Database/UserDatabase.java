/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;
import org.json.JSONObject;

/**
 *
 * @author pola-nasser13
 */
public class UserDatabase extends Database {

    public UserDatabase(String filename) {
        super(filename);
    }

    @Override
    public boolean insertRecord(JSONObject obj) {
        try {
            int newId = obj.getInt("userId");
            String newEmail = obj.getString("email");
            for (int i = 0; i < records.length(); i++) {
                JSONObject existing = records.getJSONObject(i);
                if (existing.getInt("userId") == newId
                        || existing.getString("email").equalsIgnoreCase(newEmail)) {
                    return false;
                }
            }
            records.put(obj);
            saveToFile();
            return true;

        } catch (Exception e) {
            System.out.println("Failed to insert user: " + e.getMessage());
            return false;
        }
    }

    public boolean contains(int userId) {
        return getUserById(userId) != null;
    }

    public JSONObject getUserById(int userId) {
        for (int i = 0; i < records.length(); i++) {
            JSONObject j = records.getJSONObject(i);
            if (j.getInt("userId") == userId) {
                return j;
            }
        }
        return null;
    }

    public JSONObject getUserByEmail(String email) {
        for (int i = 0; i < records.length(); i++) {
            JSONObject j = records.getJSONObject(i);
            if (j.getString("email").equalsIgnoreCase(email)) {
                return j;
            }
        }
        return null;
    }
}
