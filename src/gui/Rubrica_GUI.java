package gui;

import backend.Persona;
import backend.Rubrica_DATA;
import backend.Utente;

import javax.swing.*;
import java.util.List;

public class Rubrica_GUI {
    private final LoginWindow loginWindow;
    private MainWindow mainWindow;
    private UpdateWindow updateWindow;
    private Persona oldPerson = null;
    public Rubrica_DATA data;

    public Rubrica_GUI(Rubrica_DATA data) {
        this.data = data;
        //this.mainWindow = new MainWindow(this);
        //this.mainWindow.setVisible(true);
        this.loginWindow = new LoginWindow(this);
        this.loginWindow.setVisible(true);
    }

    public Rubrica_GUI(Rubrica_DATA data, List<Persona> people){
        this.data = data;
        //this.mainWindow = new MainWindow(this, people);
        //this.mainWindow.setVisible(true);
        this.loginWindow = new LoginWindow(this);
        this.loginWindow.setVisible(true);
    }

    public void OnClickLoginWindowLoginButton(Utente utente) {
        int res = data.submitLoginInputs(utente);
        if(res == 1) {
            this.mainWindow = new MainWindow(this, data.getPeople(), true);
            this.mainWindow.setVisible(true);
            loginWindow.dispose();
        } else if (res == 0) {
            this.mainWindow = new MainWindow(this, data.getPeople(), false);
            this.mainWindow.setVisible(true);
            loginWindow.dispose();
        } else {
            JOptionPane.showMessageDialog(loginWindow, "Username e/o password errati.", "Warning", JOptionPane.INFORMATION_MESSAGE);
        }
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
        String message = "Eliminare la persona "+oldPerson.getFirst().toUpperCase()+" "+oldPerson.getLast().toUpperCase()+"?";

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

    public void onClickUpdateWindowSalvaButton(Persona newPerson) {
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
