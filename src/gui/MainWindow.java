package gui;

import backend.Persona;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainWindow extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton newButton, editButton, deleteButton;

    public MainWindow(Rubrica_GUI gui) {
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        drawWindow(gui, false);

        JLabel errorLabel1 = new JLabel("Errore: Informazioni sul Databse CORROTTE.");
        JLabel errorLabel2 = new JLabel("Le funzionalità previste sono disabilitate.");
        JLabel errorLabel3 = new JLabel("Terminare l'applicazione. Controllare se i file per la configurazione esistono e/o se il loro contenuto è corretto.");

        JPanel labelsPanel = new JPanel();
        labelsPanel.add(errorLabel1, BorderLayout.CENTER);
        labelsPanel.add(errorLabel2, BorderLayout.CENTER);
        labelsPanel.add(errorLabel3, BorderLayout.CENTER);

        getContentPane().add(labelsPanel, BorderLayout.CENTER);

        newButton.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    public MainWindow(Rubrica_GUI gui, List<Persona> people, boolean isAdmin) {
        String[] columnNames = {"Nome", "Cognome", "Telefono"};
        tableModel = new DefaultTableModel(columnNames, 0);

        for (Persona person : people) {
            String[] row = {person.getFirst(), person.getLast(), person.getPhone()};
            tableModel.addRow(row);
        }

        drawWindow(gui, isAdmin);

        table = new JTable(tableModel);

        table.getSelectionModel().addListSelectionListener((e) -> {
            int selectedRowCount = table.getSelectedRowCount();

            if(isAdmin) {
                if (selectedRowCount == 0) {
                    newButton.setEnabled(true);
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    newButton.setEnabled(false);
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        for (int i = 0; i < table.getColumnCount(); i++) {
            Class<?> columnClass = tableModel.getColumnClass(i);
            table.setDefaultEditor(columnClass, null);
        }

        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    table.clearSelection();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() != table)
                    table.clearSelection();
            }
        });

        this.requestFocus();
    }

    public void drawWindow(Rubrica_GUI gui, boolean isAdmin) {
        String title = "Rubrica - " + gui.data.getUserInfo();
        if(isAdmin)
            title = title + " (admin)";
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(350, 250));
        setSize(600, 400);

        ImageIcon newIcon = new ImageIcon("icons/person_add.png");
        ImageIcon editIcon = new ImageIcon("icons/person_edit.png");
        ImageIcon deleteIcon = new ImageIcon("icons/person_cancel.png");
        ImageIcon frameIcon = new ImageIcon("icons/frame_icon.png");
        setIconImage(frameIcon.getImage());

        newButton = new JButton("Nuovo", newIcon);
        editButton = new JButton("Modifica", editIcon);
        deleteButton = new JButton("Elimina", deleteIcon);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);

        Dimension buttonSize = new Dimension(100, 30);
        newButton.setPreferredSize(buttonSize);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);

        if(isAdmin) {
            newButton.addActionListener(e -> gui.onClickMainWindowNuovoButton() );
            editButton.addActionListener(e -> gui.onClickMainWindowModificaButton(table.getSelectedRow()) );
            deleteButton.addActionListener(e -> gui.onClickMainWindowEliminaButton(table.getSelectedRow()) );
        } else {
            newButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }

        JToolBar toolbar = new JToolBar();

        toolbar.add(newButton);
        toolbar.add(editButton);
        toolbar.add(deleteButton);

        getContentPane().add(toolbar, BorderLayout.PAGE_START);
    }

    public void redrawTable(List<Persona> people) {
        tableModel.setRowCount(0);
        for (Persona person : people) {
            String[] row = {person.getFirst(), person.getLast(), person.getPhone()};
            tableModel.addRow(row);
        }
    }
}
