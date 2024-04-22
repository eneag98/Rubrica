package backend;

import gui.Rubrica_GUI;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Rubrica_DATA {
    private final String dbName;
    private final String fileName;
    private boolean isDB = false;
    private boolean isDirty = true;
    private HashMap<Integer,Person> people = new HashMap<>();
    public Rubrica_GUI gui;

    public Rubrica_DATA(String databaseName, String fileName){
        this.dbName = databaseName;
        this.fileName = fileName;
        if (dbName != null)
            isDB = true;

        if(isDB) {
            if(renewDataFromDB())
                gui = new Rubrica_GUI(this, getPeople());
            else {
                isDB = false;
                if (renewDataFromFile())
                    gui = new Rubrica_GUI(this, getPeople());
                else
                    gui = new Rubrica_GUI(this);
            }
        } else {
            if (renewDataFromFile())
                gui = new Rubrica_GUI(this, getPeople());
            else
                gui = new Rubrica_GUI(this);
        }
    }

    public List<Person> getPeople() {
        if(!isDB && isDirty) {
            isDirty = false;
            if(!renewDataFromFile()) {
                System.out.println("Some errors while renewing people info...");
                return new ArrayList<>();
            }
        }

        if(isDB && (people.isEmpty() || isDirty))
            if(!renewDataFromDB()) {
                System.out.println("Some errors while renewing people info...");
                return new ArrayList<>();
            }

        List<Person> list = new ArrayList<>(people.values());
        list.sort(Comparator.comparing(Person::getSortingInfo));
        return list;
    }

    public boolean renewDataFromFile() {
        try {
            if (people.size() != 0)
                people.clear();

            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";");
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
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
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

                people.put(id,new Person(first,last,address,phone,age));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
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
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
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
        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        if(id == -1)
            return false;

        if (isDB)
            return updatePersonDB(id, newPerson);

        if (!people.containsValue(oldPerson) || oldPerson.same(newPerson))
            return false;

        people.put(id, newPerson);
        isDirty = true;
        return updateDataFile();
    }

    public boolean updatePersonDB(int id, Person newPerson) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            String sql = "UPDATE Person SET first = ?, last = ?, address = ?, phone = ?, age = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, newPerson.getFirst());
            pstmt.setString(2, newPerson.getLast());
            pstmt.setString(3, newPerson.getAddress());
            pstmt.setString(4, newPerson.getPhone());
            pstmt.setInt(5, newPerson.getAge());
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
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

    public boolean deletePerson(Person oldPerson) {
        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        if (id == -1)
            return false;

        if (isDB)
            if(deletePersonDB(id)) {
                isDirty = true;
                return true;
            } else
                return false;

        if (!people.containsValue(oldPerson))
            return false;

        people.remove(id);
        isDirty = true;
        return updateDataFile();
    }

    private boolean deletePersonDB(int id) {
        boolean result = true;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            String sql = "DELETE FROM Person WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
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
}
