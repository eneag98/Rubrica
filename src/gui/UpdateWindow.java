package gui;

import backend.Persona;
import javax.swing.*;
import java.awt.*;

public class UpdateWindow extends JFrame {
    private final JLabel firstLabel, lastLabel, addressLabel, phoneLabel, ageLabel;
    private final JTextField firstTextField, lastTextField, addressTextField, phoneTextField, ageTextField;

    public UpdateWindow(Rubrica_GUI gui, Persona person) {
        firstLabel = new JLabel("Nome:");
        lastLabel = new JLabel("Cognome:");
        addressLabel = new JLabel("Indirizzo:");
        phoneLabel = new JLabel("Telefono:");
        ageLabel = new JLabel("Età:");
        if (person == null) {
            firstTextField = new JTextField(20);
            lastTextField = new JTextField(20);
            addressTextField = new JTextField(20);
            phoneTextField = new JTextField(10);
            ageTextField = new JTextField(3);
        } else {
            firstTextField = new JTextField(person.getFirst(), 20);
            lastTextField = new JTextField(person.getLast(), 20);
            addressTextField = new JTextField(person.getAddress(), 20);
            phoneTextField = new JTextField(person.getPhone(), 10);
            ageTextField = new JTextField(String.valueOf(person.getAge()), 3);
        }
        drawWindow(gui, "editor-persona");
    }

    public void drawWindow(Rubrica_GUI gui, String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 350));
        setSize(500, 350);

        ImageIcon salvaIcon = new ImageIcon("icons/save.png");
        ImageIcon annullaIcon = new ImageIcon("icons/cancel.png");
        ImageIcon frameIcon = new ImageIcon("icons/frame_icon.png");
        setIconImage(frameIcon.getImage());

        Dimension inputSize = new Dimension(100, 30);
        firstLabel.setPreferredSize(inputSize);
        lastLabel.setPreferredSize(inputSize);
        addressLabel.setPreferredSize(inputSize);
        phoneLabel.setPreferredSize(inputSize);
        ageLabel.setPreferredSize(inputSize);
        firstTextField.setPreferredSize(inputSize);
        lastTextField.setPreferredSize(inputSize);
        addressTextField.setPreferredSize(inputSize);
        phoneTextField.setPreferredSize(inputSize);
        ageTextField.setPreferredSize(inputSize);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGBC = new GridBagConstraints();
        inputGBC.gridx = 0;
        inputGBC.gridy = 0;
        inputGBC.insets = new Insets(5, 5, 5, 5); // Margine di 5 pixel
        inputPanel.add(firstLabel, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(firstTextField, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(lastLabel, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(lastTextField, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(addressLabel, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(addressTextField, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(phoneLabel, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(phoneTextField, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(ageLabel, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(ageTextField, inputGBC);

        inputGBC.anchor = GridBagConstraints.WEST;

        JButton saveButton = new JButton("Salva", salvaIcon);
        JButton cancelButton = new JButton("Annulla", annullaIcon);

        Dimension buttonSize = new Dimension(100, 30);
        saveButton.setPreferredSize(buttonSize);
        cancelButton.setPreferredSize(buttonSize);

        saveButton.addActionListener((e) -> {
            Persona p = getNewValues();
            if (p != null){
                gui.onClickUpdateWindowSalvaButton(p);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Uno o più campi sono vuoti o invalidi!");
            }
        });

        cancelButton.addActionListener((e) -> {
            gui.onClickUpdateWindowAnnullaButton();
            dispose();
        });

        JToolBar toolBar = new JToolBar();

        toolBar.add(saveButton);
        toolBar.add(cancelButton);
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);

        setLocationRelativeTo(null);
    }

    public Persona getNewValues() {
        if (firstTextField.getText().trim().isEmpty())
            return null;
        if (lastTextField.getText().trim().isEmpty())
            return null;
        if (addressTextField.getText().trim().isEmpty())
            return null;
        if (phoneTextField.getText().trim().isEmpty() ||
            !phoneTextField.getText().trim().matches("[0-9]+"))
            return null;
        if (ageTextField.getText().trim().isEmpty() ||
            !ageTextField.getText().trim().matches("[0-9]+"))
            return null;

        String[] newValues = {firstTextField.getText(), lastTextField.getText(), addressTextField.getText(), phoneTextField.getText(), ageTextField.getText()};
        return new Persona(newValues);
    }
}
