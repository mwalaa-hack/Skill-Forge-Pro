/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package Backend.Models;

import Backend.Database.Info;
import org.json.JSONObject;
import java.security.MessageDigest;

/**
 *
 * @author Mohamed Walaa
 */

public abstract class User implements Info {

    protected int userId;
    protected String username;
    protected String email;
    protected String passwordHash;
    protected String role;

    public User(int userId, String username, String email, String password, String role) {
        if (userId > 0) {
            this.userId = userId;
        } else {
            this.userId = 0;
        }
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setRole(role);
    }

    public User(JSONObject j) {
        this.userId = j.getInt("userId");
        this.username = j.getString("username");
        this.email = j.getString("email");
        this.passwordHash = j.getString("passwordHash");
        this.role = j.getString("role");
    }

    protected String hashPassword(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String h = Integer.toHexString(0xff & hash[i]);
                if (h.length() == 1) {
                    hex.append('0');
                }
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean verifyPassword(String password) {
        String h = hashPassword(password);
        if (h == null) {
            return false;
        }
        return h.equals(passwordHash);
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new Exception("Username empty");
            }
            username = username.trim();
            for (int i = 0; i < username.length(); i++) {
                char ch = username.charAt(i);
                if (!Character.isLetterOrDigit(ch) && ch != '_' && ch != '.') {
                    throw new Exception("Invalid character");
                }
            }
            this.username = username;
        } catch (Exception e) {
            System.out.println("Invalid username: " + e.getMessage() + ". Username not changed.");
        }
    }

    public void setEmail(String email) {
        try {
            if (email == null) {
                throw new Exception("Email is null");
            }
            email = email.trim();
            int atPos = email.indexOf("@");
            int dotPos = email.lastIndexOf(".");
            if (atPos <= 0 || dotPos <= atPos + 1 || dotPos == email.length() - 1) {
                throw new Exception("Invalid email format");
            }
            this.email = email;
        } catch (Exception e) {
            System.out.println("Invalid email: " + e.getMessage() + ". Email not changed.");
        }
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 4) {
            this.passwordHash = hashPassword(password);
        } else {
            System.out.println("Password too short. Password not changed.");
        }
    }

    public void setRole(String role) {
        if (role != null && (role.equalsIgnoreCase("student") || role.equalsIgnoreCase("instructor"))) {
            this.role = role.toLowerCase();
        } else {
            System.out.println("Invalid role. Role not changed.");
        }
    }

    public int getid() {
        return userId;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        j.put("userId", userId);
        j.put("username", username);
        j.put("email", email);
        j.put("passwordHash", passwordHash);
        j.put("role", role);
        return j;
    }
}
