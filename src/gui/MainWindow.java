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
    private JButton nuovoButton, modificaButton, eliminaButton;

    public MainWindow(Rubrica_GUI gui) {
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        drawWindow(gui, false);

        JLabel errorLabel1 = new JLabel("Errore: Informazioni su Database e File di testo MANCANTI o CORROTTE.");
        JLabel errorLabel2 = new JLabel("Le funzionalità previste sono disabilitate.");
        JLabel errorLabel3 = new JLabel("Terminare l'applicazione. Controllare se i file esistono e/o se i loro nomi sono corretti ");
        JLabel errorLabel4 = new JLabel("(Database: 'informazioni.db', File_testo: 'informazioni.txt')");

        JPanel labelsPanel = new JPanel();
        labelsPanel.add(errorLabel1, BorderLayout.CENTER);
        labelsPanel.add(errorLabel2, BorderLayout.CENTER);
        labelsPanel.add(errorLabel3, BorderLayout.CENTER);
        labelsPanel.add(errorLabel4, BorderLayout.CENTER);

        getContentPane().add(labelsPanel, BorderLayout.CENTER);

        nuovoButton.setEnabled(false);
        modificaButton.setEnabled(false);
        eliminaButton.setEnabled(false);
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

            if (selectedRowCount == 0) {
                nuovoButton.setEnabled(true);
                modificaButton.setEnabled(false);
                eliminaButton.setEnabled(false);
            } else {
                nuovoButton.setEnabled(false);
                modificaButton.setEnabled(true);
                eliminaButton.setEnabled(true);
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

        ImageIcon nuovoIcon = new ImageIcon("src/gui/icons/person_add.png");
        ImageIcon modificaIcon = new ImageIcon("src/gui/icons/person_edit.png");
        ImageIcon eliminaIcon = new ImageIcon("src/gui/icons/person_cancel.png");
        ImageIcon frameIcon = new ImageIcon("src/gui/icons/frame_icon.png");
        setIconImage(frameIcon.getImage());

        nuovoButton = new JButton("Nuovo", nuovoIcon);
        modificaButton = new JButton("Modifica", modificaIcon);
        eliminaButton = new JButton("Elimina", eliminaIcon);
        modificaButton.setEnabled(false);
        eliminaButton.setEnabled(false);

        Dimension buttonSize = new Dimension(100, 30);
        nuovoButton.setPreferredSize(buttonSize);
        modificaButton.setPreferredSize(buttonSize);
        eliminaButton.setPreferredSize(buttonSize);

        if(isAdmin) {
            nuovoButton.addActionListener(e -> gui.onClickMainWindowNuovoButton() );
            modificaButton.addActionListener(e -> gui.onClickMainWindowModificaButton(table.getSelectedRow()) );
            eliminaButton.addActionListener(e -> gui.onClickMainWindowEliminaButton(table.getSelectedRow()) );
        } else {
            nuovoButton.setEnabled(false);
            modificaButton.setEnabled(false);
            eliminaButton.setEnabled(false);
        }

        JToolBar toolbar = new JToolBar();

        toolbar.add(nuovoButton);
        toolbar.add(modificaButton);
        toolbar.add(eliminaButton);

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
