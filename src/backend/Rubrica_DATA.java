package backend;

import gui.Rubrica_GUI;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Rubrica_DATA {
    private String fileName = "";
    private boolean isDB = false;
    private boolean isDirty = true;
    private HashMap<Integer,Person> people = new HashMap<>();
    public Rubrica_GUI gui;

    public Rubrica_DATA(String fileName){
        this.fileName= fileName;
        if (fileName.contains(".db"))
            isDB = true;

        if(isDB) {
            if(renewDataFromDB())
                gui = new Rubrica_GUI(this, new ArrayList<>(people.values()));
            else
                gui = new Rubrica_GUI(this);
        } else {
            if (renewDataFromFile())
                gui = new Rubrica_GUI(this, new ArrayList<>(people.values()));
            else
                gui = new Rubrica_GUI(this);
        }
    }

    public List<Person> getPeople() {
        if(isDB) {
            if(!renewDataFromDB()) {
                System.out.println("Some errors while renewing people info...");
                return new ArrayList<>();
            }
        } else {
            if (isDirty) {
                isDirty = false;
                if(!renewDataFromFile()) {
                    System.out.println("Some errors while renewing people info...");
                    return new ArrayList<>();
                }
            }
        }
        return new ArrayList<>(people.values());
    }

    public boolean renewDataFromFile() {
        try {
            if (people.size() != 0)
                people.clear();

            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";"); // Dividi la riga usando il punto e virgola come separatore
                people.put(people.size(), new Person(values));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean renewDataFromDB() {
        boolean result = true;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+fileName);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Person");

            if (people.size() != 0)
                people.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String first = rs.getString("first");
                String last = rs.getString("last");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");
                System.out.println("ID: " + id +
                        ", Nome: " + first + ", Cognome: " + last + ", Indirizzo: " + address + ", Telefono: " + phone + ", Età: " + age);
                people.put(id,new Person(first,last,address,phone,age));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            // Chiudi le risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }

        return result;
    }

    public boolean updateDataFile() {
        List<String> list = new ArrayList<>();
        for(Person person : people.values()) {
            String formattedPerson = person.getFirst()+";"
                    +person.getLast()+";"
                    +person.getAddress()+";"
                    +person.getPhone()+";"
                    +person.getAge()+"\n";
            list.add(formattedPerson);
        }
        // Prova a scrivere le righe sul file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
            if (list.size() == 0)
                writer.write("");
            else
                for (String info : list)
                    writer.write(info);
            writer.close();
            System.out.println("File aggiornato");
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura sul file: " + e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addPerson(Person newPerson) {
        if(isDB)
            return addPersonDB(newPerson);

        if (people.containsValue(newPerson))
            return false;

        people.put(people.size(), newPerson);
        isDirty = true;
        return updateDataFile();
    }

    private boolean addPersonDB(Person newPerson) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+fileName);
            String sql = "INSERT INTO Person (first, last, address, phone, age) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, newPerson.getFirst());
            pstmt.setString(2, newPerson.getLast());
            pstmt.setString(3, newPerson.getAddress());
            pstmt.setString(4, newPerson.getPhone());
            pstmt.setInt(5, newPerson.getAge());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            // Chiudi le risorse
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }

        return result;
    }

    public boolean updatePerson(Person newPerson, Person oldPerson) {
        if (!people.containsValue(oldPerson) || oldPerson.same(newPerson))
            return false;

        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        people.put(id, newPerson);
        isDirty = true;
        return updateDataFile();
    }

    public boolean deletePerson(Person oldPerson) {
        if (!people.containsValue(oldPerson))
            return false;

        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        people.remove(id);
        isDirty = true;
        return updateDataFile();
    }
}
