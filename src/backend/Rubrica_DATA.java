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
    private boolean isDirty = true;
    private HashMap<Integer,Person> people = new HashMap<Integer, Person>();
    public Rubrica_GUI gui;

    public Rubrica_DATA(String fileName){
        this.fileName= fileName;

        if (renewDataFromFile())
            gui = new Rubrica_GUI(this, people.values().stream().collect(Collectors.toList()));
        else
            gui = new Rubrica_GUI(this);
    }

    public List<Person> getPeople() {
        if (isDirty)
           if(!renewDataFromFile()) {
               System.out.println("Some errors while renewing people info...");
               return new ArrayList<>();
           }
        return people.values().stream().collect(Collectors.toList());
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

    public boolean addPerson(Person newPerson) {
        if (people.containsValue(newPerson))
            return false;

        people.put(people.size(), newPerson);
        return updateSourceFile();
    }

    public boolean updatePerson(Person newPerson, Person oldPerson) {
        if (!people.containsValue(oldPerson) || oldPerson.same(newPerson))
            return false;

        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        people.put(id, newPerson);
        return updateSourceFile();
    }

    public boolean deletePerson(Person oldPerson) {
        if (!people.containsValue(oldPerson))
            return false;

        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        people.remove(id);
        return updateSourceFile();
    }

    public boolean updateSourceFile() {
        List<String> list = new ArrayList<String>();
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

    public static void renewDataFromDB() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Carica il driver JDBC SQLite (è necessario includere il driver sqlite-jdbc nel classpath)
            Class.forName("org.sqlite.JDBC");

            // Connessione al database SQLite
            conn = DriverManager.getConnection("jdbc:sqlite:../informazioni.db");

            // Crea un'istruzione SQL
            stmt = conn.createStatement();

            // Esegui una query per ottenere l'elenco delle tabelle
            rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");

            // Stampa l'elenco delle tabelle
            System.out.println("Elenco delle tabelle nel database:");
            while (rs.next()) {
                String tableName = rs.getString("name");
                System.out.println(tableName);
            }
            /*
            // Esegui una query SQL per selezionare i dati
            rs = stmt.executeQuery("SELECT * FROM Person");

            // Elabora i risultati della query
            while (rs.next()) {
                // Leggi i dati dalle colonne
                int id = rs.getInt("id");
                String first = rs.getString("first");
                String last = rs.getString("last");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                int age = rs.getInt("age");

                // Esegui l'operazione desiderata con i dati letti
                System.out.println("ID: " + id +
                        ", Nome: " + first + ", Cognome: " + last + ", Phone: " + phone);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Chiudi le risorse
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        renewDataFromDB();
    }
}
