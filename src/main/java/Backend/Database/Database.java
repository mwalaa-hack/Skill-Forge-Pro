/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author pola-nasser13
 */
public abstract class Database {

    protected JSONArray records;
    protected String filename;

    public Database(String filename) {
        this.filename = filename;
        this.records = new JSONArray();
        readFromFile();
    }

    public void readFromFile() {
        records = new JSONArray();

        try {
            File file = new File(filename);
            if (!file.exists()) {
                saveToFile();
                return;
            }

            String text = "";
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                text += scanner.nextLine();
            }
            scanner.close();

            if (text.isEmpty()) {
                return;
            }

            JSONArray arr = new JSONArray(text);

            for (int i = 0; i < arr.length(); i++) {
                records.put(arr.getJSONObject(i));
            }

        } catch (Exception e) {
            System.out.println("Failed to read JSON file: " + filename);
        }
    }

    public abstract boolean insertRecord(JSONObject obj);


    public void deleteRecord(int key) {
        JSONArray newArr = new JSONArray();
        boolean deleted = false;

        for (int i = 0; i < records.length(); i++) {
            JSONObject j = records.getJSONObject(i);
            if (j.getInt("userId") != key) {
                newArr.put(j);
            } else {
                deleted = true;
            }
        }

        records = newArr;
        if (deleted) {
            saveToFile();
        }
    }

    public JSONArray getRecords() {
        return records;
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.write(records.toString(4));
        } catch (Exception e) {  
            System.out.println("Failed to save: " + filename);
        }
    }
}