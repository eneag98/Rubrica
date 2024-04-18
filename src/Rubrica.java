import backend.Person;
import backend.Rubrica_DATA;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Rubrica {


    public static void main(String args[]) {
        Rubrica_DATA data = new Rubrica_DATA("src/backend/informazioni.txt");

        for (Person p : data.getPeople())
            System.out.println(p.toString());

    }
}