package backend;

import gui.Rubrica_GUI;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Rubrica_DATA {
    private Connection connection;
    private Utente loggedInUser;
    private boolean isDirty;
    private HashMap<Integer, Persona> people;
    public Rubrica_GUI gui;

    public Rubrica_DATA(){
        String username = "";
        String password = "";
        String host = "";
        int port = -1;

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./credenziali_database.properties")) {
            properties.load(fis);

            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            host = properties.getProperty("db.host");
            port = Integer.parseInt(properties.getProperty("db.port"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/informazioni";
        try{
            this.connection = DriverManager.getConnection(url, username, password);
            this.isDirty = true;
            this.people = new HashMap<>();
            gui = new Rubrica_GUI(this);
        } catch (SQLException e) {
            e.printStackTrace();
            gui = new Rubrica_GUI();
        }
    }

    public List<Persona> getPeople() {
        if(isDirty)
            if(!renewDataFromDB()) {
                System.out.println("Some errors while renewing people info...");
                return new ArrayList<>();
            }

        List<Persona> list = new ArrayList<>(people.values());
        list.sort(Comparator.comparing(Persona::getSortingInfo));
        return list;
    }

    public int submitLoginInputs(Utente utente) {
        int result = -1;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String query = "SELECT first, last, admin FROM user WHERE username = ? AND password = ?";
            stmt = this.connection.prepareStatement(query);
            stmt.setString(1, utente.getUsername());
            stmt.setString(2, utente.getPassword());
            rs = stmt.executeQuery();

            while (rs.next()) {
                result = rs.getInt("admin");
                boolean admin = result == 1;
                this.loggedInUser = new Utente(rs.getString("first"), rs.getString("last"), admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = -1;
            }
        }
        return result;
    }

    public String getUserInfo() {
        String info = loggedInUser.getFirst();
        if(this.loggedInUser.getLast() != null)
            info = info + " " + this.loggedInUser.getLast();
        return info;
    }

    public boolean renewDataFromDB() {
        boolean result = true;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = this.connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM person");

            if (people.size() != 0)
                people.clear();

            while (rs.next()) {
                int id = rs.getInt("id");
                String first = rs.getString("first");
                String last = rs.getString("last");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");

                people.put(id,new Persona(first,last,address,phone,age));
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }

        return result;
    }

    public boolean addPerson(Persona newPerson) {
        boolean result = true;
        PreparedStatement pstmt = null;

        try {
            String sql = "INSERT INTO person (first, last, address, phone, age) VALUES (?, ?, ?, ?, ?)";
            pstmt = this.connection.prepareStatement(sql);

            pstmt.setString(1, newPerson.getFirst());
            pstmt.setString(2, newPerson.getLast());
            pstmt.setString(3, newPerson.getAddress());
            pstmt.setString(4, newPerson.getPhone());
            pstmt.setInt(5, newPerson.getAge());

            pstmt.executeUpdate();
            isDirty = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    public boolean updatePerson(Persona newPerson, Persona oldPerson) {
        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        boolean result = true;
        PreparedStatement pstmt = null;

        try {
            String sql = "UPDATE person SET first = ?, last = ?, address = ?, phone = ?, age = ? WHERE id = ?";
            pstmt = this.connection.prepareStatement(sql);

            pstmt.setString(1, newPerson.getFirst());
            pstmt.setString(2, newPerson.getLast());
            pstmt.setString(3, newPerson.getAddress());
            pstmt.setString(4, newPerson.getPhone());
            pstmt.setInt(5, newPerson.getAge());
            pstmt.setInt(6, id);

            pstmt.executeUpdate();
            isDirty = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }

    public boolean deletePerson(Persona oldPerson) {
        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        if (id == -1)
            return false;

        boolean result = true;
        PreparedStatement pstmt = null;

        try {
            String sql = "DELETE FROM person WHERE id = ?";
            pstmt = this.connection.prepareStatement(sql);

            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            isDirty = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }
}
