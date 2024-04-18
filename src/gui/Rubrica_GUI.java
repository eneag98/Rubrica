package gui;

import backend.Person;
import backend.Rubrica_DATA;

import javax.swing.*;
import java.util.List;

public class Rubrica_GUI {
    private MainWindow mainWindow;
    private UpdateWindow updateWindow;
    private Person oldPerson = null;
    public Rubrica_DATA data;

    public Rubrica_GUI(Rubrica_DATA data) {
        this.data = data;
        this.mainWindow = new MainWindow(this);
        this.mainWindow.setVisible(true);
    }

    public Rubrica_GUI(Rubrica_DATA data, List<Person> people){
        this.data = data;
        this.mainWindow = new MainWindow(this, people);
        this.mainWindow.setVisible(true);
    }

    public void onClickMainWindowNuovoButton() {
        oldPerson = null;
        this.updateWindow = new UpdateWindow(this, null);
        this.updateWindow.setVisible(true);
    }

    public void onClickMainWindowModificaButton(int personIndex) {
        oldPerson = data.getPeople().get(personIndex);
        this.updateWindow = new UpdateWindow(this, oldPerson);
        this.updateWindow.setVisible(true);
    }

    public void onClickMainWindowEliminaButton(int personIndex) {
        boolean res = false;
        oldPerson = data.getPeople().get(personIndex);
        if (data.deletePerson(oldPerson)) {
            System.out.println("OK!");
            res = true;
        } else
            System.out.println("Error!");

        if (res) {
            oldPerson = null;
            mainWindow.redrawTable(data.getPeople());
        }
    }

    public void onClickUpdateWindowSalvaButton(Person newPerson) {
        boolean res = false;
        if (oldPerson == null) { // Nuovo contatto
            if (data.addPerson(newPerson)) {
                System.out.println("OK!");
                res = true;
            }
            else
                System.out.println("Error!");
        } else { // Modifica contatto
            if (data.updatePerson(newPerson, oldPerson)) {
                System.out.println("OK!");
                res = true;
            } else {
                System.out.println("Error!");
            }
        }
        if (res) {
            oldPerson = null;
            mainWindow.redrawTable(data.getPeople());
        }

    }

    public void onClickUpdateWindowAnnullaButton() {
        oldPerson = null;
    }
}
