/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;
import Backend.Models.*;

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

                if (ins.getUserId() == userId ||
                    ins.getEmail().equalsIgnoreCase(email)) {
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

//    public void deleteInstructor(int instructorId) {
//        boolean deleted = false;
//
//        for (int i = 0; i < records.size(); i++) {
//            Instructor ins = records.get(i);
//            if (ins.getId() == instructorId) {
//                records.remove(i);
//                deleted = true;
//                i--;
//            }
//        }
//
//        if (deleted) {
//            System.out.println("Instructor deleted: " + instructorId);
//            saveToFile();
//        } else {
//            System.out.println("No instructor found with ID: " + instructorId);
//        }
//    }
    
        public Instructor getInstructorById(int instructorId) {
        for (int i = 0; i < records.size(); i++) {
            Instructor ins = records.get(i);
            if (ins.getId() == instructorId) {
                return ins;
            }
        }
        return null;
    }

    public Instructor getInstructorByEmail(String email) {
        for (int i = 0; i < records.size(); i++) {
            Instructor ins = records.get(i);
            if (ins.getEmail().equalsIgnoreCase(email)) {
                return ins;
            }
        }
        return null;
    }
        public boolean contains(int instructorId) {
        return getInstructorById(instructorId) != null;
    }
}