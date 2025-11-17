/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend.Database;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author pola-nasser13
 */
public abstract class Database<D extends Info> {
    protected ArrayList<D> records;
    protected String filename;

    public Database(String filename) {
        this.filename = filename;
        this.records = new ArrayList<>();
        readFromFile();
    }

    public abstract D createRecordFrom(JSONObject j);

    public abstract boolean insertRecord(JSONObject j);

    public void readFromFile() {
        ArrayList<D> tempRecords = new ArrayList<>();
        try {
            File file = new File(filename);
            if (!file.exists()) {
                saveToFile();
                return;
            }
            StringBuilder content = new StringBuilder();
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                content.append(sc.nextLine());
            }
            sc.close();
            if (content.length() == 0) {
                return;
            }
            JSONArray arr = new JSONArray(content.toString());
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                D rec = createRecordFrom(obj);
                if (rec != null) {
                    tempRecords.add(rec);
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to read JSON: " + filename + " - " + e.getMessage());
            return;
        }
        records = tempRecords;
    }

    public ArrayList<D> getRecords() {
        return records;
    }

    public void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            JSONArray arr = new JSONArray();
            for (int i = 0; i < records.size(); i++) {
                arr.put(records.get(i).toJSON());
            }
            pw.write(arr.toString(4));
        } catch (Exception e) {
            System.out.println("Failed to save: " + filename);
        }
    }
}