package gui;

import backend.Person;

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
    private JLabel label;
    private JButton nuovoButton, modificaButton, eliminaButton;

    public MainWindow(Rubrica_GUI gui) {
        // Creazione del modello della tabella
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);

        drawWindow(gui);

        // Aggiunta della tabella in uno JScrollPane per la scrollabilità
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public MainWindow(Rubrica_GUI gui, List<Person> people) {
        // Crea il modello di tabella con le colonne desiderate
        String[] columnNames = {"Nome", "Cognome", "Telefono"};
        tableModel = new DefaultTableModel(columnNames, 0);

        // Aggiungi le righe al modello di tabella utilizzando i valori degli oggetti Person
        for (Person person : people) {
            String[] row = {person.getFirst(), person.getLast(), person.getPhone()};
            tableModel.addRow(row);
        }

        drawWindow(gui);

        // Crea la tabella JTable con il modello di tabella creato
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

        // Imposta le celle della tabella come non-editable
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

    public void drawWindow(Rubrica_GUI gui) {
        setTitle("Rubrica Turing - Enea Guarneri");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(350, 350));
        setSize(600, 400);

        // Carica le icone PNG
        ImageIcon nuovoIcon = new ImageIcon("src/gui/icons/person_add.png");
        ImageIcon modificaIcon = new ImageIcon("src/gui/icons/person_edit.png");
        ImageIcon eliminaIcon = new ImageIcon("src/gui/icons/person_cancel.png");

        // Creazione dei pulsanti
        nuovoButton = new JButton("Nuovo", nuovoIcon);
        modificaButton = new JButton("Modifica", modificaIcon);
        eliminaButton = new JButton("Elimina", eliminaIcon);
        modificaButton.setEnabled(false);
        eliminaButton.setEnabled(false);

        Dimension buttonSize = new Dimension(100, 30);
        nuovoButton.setPreferredSize(buttonSize);
        modificaButton.setPreferredSize(buttonSize);
        eliminaButton.setPreferredSize(buttonSize);

        nuovoButton.addActionListener(e -> { gui.onClickMainWindowNuovoButton(); });
        modificaButton.addActionListener(e -> { gui.onClickMainWindowModificaButton(table.getSelectedRow()); });
        eliminaButton.addActionListener(e -> { gui.onClickMainWindowEliminaButton(table.getSelectedRow()); });

        /*JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Margine di 5 pixel
        buttonPanel.add(nuovoButton, gbc);

        gbc.gridx++;
        buttonPanel.add(modificaButton, gbc);

        gbc.gridx++;
        buttonPanel.add(eliminaButton, gbc);

        gbc.anchor = GridBagConstraints.SOUTH;
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);*/

        JToolBar toolBar = new JToolBar();

        // Aggiungi i pulsanti alla toolbar
        toolBar.add(nuovoButton);
        toolBar.add(modificaButton);
        toolBar.add(eliminaButton);

        getContentPane().add(toolBar, BorderLayout.PAGE_START);
    }

    public void redrawTable(List<Person> people) {
        tableModel.setRowCount(0);
        for (Person person : people) {
            String[] row = {person.getFirst(), person.getLast(), person.getPhone()};
            tableModel.addRow(row);
        }
    }
}
