/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;

import Backend.Models.*;
import java.util.ArrayList;
import org.json.JSONObject;

/**
 *
 * @author pola-nasser13
 */
public class InstructorDatabase extends Database<Instructor> {

    public InstructorDatabase(String filename) {
        super(filename);
    }

    @Override
    public Instructor createRecordFrom(JSONObject j) {
        return new Instructor(j);
    }

    @Override
    public boolean insertRecord(JSONObject j) {
        try {
            int userId = j.getInt("userId");
            String email = j.getString("email");
            for (int i = 0; i < records.size(); i++) {
                Instructor ins = records.get(i);
                if (ins.getUserId() == userId || ins.getEmail().equalsIgnoreCase(email)) {
                    return false;
                }
            }
            Instructor newIns = createRecordFrom(j);
            records.add(newIns);
            saveToFile();
            return true;
        } catch (Exception e) {
            System.out.println("Failed to insert instructor: " + e.getMessage());
            return false;
        }
    }

    public Instructor getInstructorById(int instructorId) {
        for (int i = 0; i < records.size(); i++) {
            Instructor ins = records.get(i);
            if (ins.getUserId() == instructorId) {
                if ("instructor".equalsIgnoreCase(ins.getRole())) { 
                return ins;
            } else {
                return null; 
            }
        }
    }
    return null;
}

public Instructor getInstructorByEmail(String email) {
    for (int i = 0; i < records.size(); i++) {
        Instructor ins = records.get(i);
        if (ins.getEmail().equalsIgnoreCase(email)) {
            if ("instructor".equalsIgnoreCase(ins.getRole())) { 
                return ins;
            } else {
                return null; 
            }
        }
    }
    return null;
}


    public boolean contains(int instructorId) {
        return getInstructorById(instructorId) != null;
    }

    public boolean addCourseIdToInstructor(int instructorId, int courseId) {
        Instructor ins = getInstructorById(instructorId);
        if (ins == null) {
            return false;
        }
        boolean added = ins.addCourseId(courseId);
        if (added) {
            saveToFile();
        }
        return added;
    }

    public boolean removeCourseIdFromInstructor(int instructorId, int courseId) {
        Instructor ins = getInstructorById(instructorId);
        if (ins == null) {
            return false;
        }
        boolean removed = ins.removeCourseId(courseId);
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    public ArrayList<Integer> getInstructorCourseIds(int instructorId) {
        Instructor ins = getInstructorById(instructorId);
        if (ins == null) {
            return new ArrayList<Integer>();
        }
        return ins.getCreatedCourseIds();
    }
}
