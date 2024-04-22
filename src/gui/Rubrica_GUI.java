package gui;

import backend.Person;
import backend.Rubrica_DATA;

import javax.swing.*;
import java.util.List;

public class Rubrica_GUI {
    private final MainWindow mainWindow;
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

        Object[] options = {"Conferma", "Annulla"};
        String title = "Azione richiesta";
        String message = "Vuoi davvero eliminare "+oldPerson.getFirst()+" "+oldPerson.getLast()+"?";

        int choice = JOptionPane.showOptionDialog(mainWindow,message,title,JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,null,options,options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            if (data.deletePerson(oldPerson)) {
                System.out.println("OK!");
                JOptionPane.showMessageDialog(mainWindow, "Contatto eliminato con successo!");
                res = true;
            } else
                JOptionPane.showMessageDialog(mainWindow, "Non è stato possibile eliminare il contatto.");
        }

        if (res) {
            oldPerson = null;
            mainWindow.redrawTable(data.getPeople());
        }
    }

    public void onClickUpdateWindowSalvaButton(Person newPerson) {
        boolean res = false;
        if (oldPerson == null) { // Nuovo contatto
            if (data.addPerson(newPerson)) {
                JOptionPane.showMessageDialog(updateWindow, "Nuovo contatto aggiunto con successo!");
                System.out.println("OK!");
                res = true;
            }
            else
                JOptionPane.showMessageDialog(updateWindow, "Non è stato possibile aggiungere un nuovo contatto.");
        } else { // Modifica contatto
            if (data.updatePerson(newPerson, oldPerson)) {
                JOptionPane.showMessageDialog(updateWindow, "Contatto modificato con successo!");
                System.out.println("OK!");
                res = true;
            } else {
                JOptionPane.showMessageDialog(updateWindow, "Non è stato possibile modificare il contatto.");
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
