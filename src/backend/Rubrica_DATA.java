package backend;

import gui.Rubrica_GUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Rubrica_DATA {
    private HashMap<Integer,Person> people = new HashMap<Integer, Person>();
    public Rubrica_GUI gui;

    public Rubrica_DATA(String fileName){
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(";"); // Dividi la riga usando il punto e virgola come separatore
                people.put(people.size(), new Person(values));
            }
            scanner.close();

            gui = new Rubrica_GUI(this, people.values().stream().toList());
            //gui = new Rubrica_GUI();

        } catch (FileNotFoundException e) {
            System.out.println("File non trovato.");
            e.printStackTrace();
        }
    }

    public boolean addPerson(Person newPerson) {
        if (people.containsValue(newPerson))
            return false;

        people.put(people.size(), newPerson);
        return true;
    }

    public boolean updatePerson(Person newPerson, Person oldPerson) {
        if (!people.containsValue(oldPerson) || oldPerson.same(newPerson))
            return false;

        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        people.put(id, newPerson);
        return true;
    }

    public boolean deletePerson(Person oldPerson) {
        if (!people.containsValue(oldPerson))
            return false;

        Integer id = -1;
        for (Integer key : people.keySet())
            if(people.get(key).equals(oldPerson))
                id = key;

        people.remove(id);
        return true;
    }

    public List<Person> getPeople() {
        return people.values().stream().toList();
    }
}
