/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package Backend.Models;

import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.*;

/**
 *
 * @author Mohamed Walaa
 */






public abstract class User implements Info {

protected int userId;
protected String username;
protected String email;
protected String passwordHash;
protected String role; //student or instructor


public User(int userId, String username, String email, String password, String role) {
    
    if (userId > 0)
        this.userId = userId;
    else{
        System.out.println("userId must be positive. Defaulting to 0");
        this.userId = 0;
    }

    setUsername(username);
    setEmail(email);
    setPassword(password);
    setRole(role);
}



public User(JSONObject j) {
    this(
        j.optInt("userId", 0),
        j.optString("username", "User"),
        j.optString("email", "user@example.com"),
        j.optString("passwordHash", "1234"),
        j.optString("role", "student")
    );
}



    //Hash password using SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }



    public boolean verifyPassword(String password) {
        return hashPassword(password).equals(passwordHash);
    }



    //Getters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }

    //Setters
    public void setUsername(String username) {
        try {
            if(username == null || username.trim().isEmpty()) throw new Exception("Username empty");
            username = username.trim();
            for(int i=0; i<username.length(); i++) {
                if(!Character.isLetter(username.charAt(i))) throw new Exception("Invalid character");
            }
            this.username = username;
        } catch(Exception e) {
            System.out.println("Invalid username: " + e.getMessage() + ". Username not changed.");
        }
    }

    public void setEmail(String email) {
        try {
            if(email == null) throw new Exception("Email is null");
            email = email.trim();
            int atPos = email.indexOf("@");
            int dotPos = email.lastIndexOf(".");
            if(atPos <= 0 || dotPos <= atPos + 1 || dotPos == email.length() - 1) {
                throw new Exception("Invalid email format");
            }
            this.email = email;
        } catch(Exception e) {
            System.out.println("Invalid email: " + e.getMessage() + ". Email not changed.");
        }
    }

    public void setPassword(String password) {
        if(password != null && password.length() >= 4)
            this.passwordHash = hashPassword(password);
        else
            System.out.println("Password too short. Password not changed.");
    }

    public void setRole(String role) {
        if(role != null && (role.equalsIgnoreCase("student") || role.equalsIgnoreCase("instructor"))) {
            this.role = role.toLowerCase();
        } else {
            System.out.println("Invalid role. Role not changed.");
        }
    }


    @Override
    public int getSearchKey() { return userId; }

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

    //for polymorphism
    public abstract void openDashboard(JFrame parentFrame);
}

