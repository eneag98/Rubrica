package gui;

import backend.Persona;
import javax.swing.*;
import java.awt.*;

public class UpdateWindow extends JFrame {
    private final JLabel labelFirst, labelLast, labelAddress, labelPhone, labelAge;
    private final JTextField textFieldFirst, textFieldLast, textFieldAddress, textFieldPhone, textFieldAge;

    public UpdateWindow(Rubrica_GUI gui, Persona person) {
        labelFirst = new JLabel("Nome:");
        labelLast = new JLabel("Cognome:");
        labelAddress = new JLabel("Indirizzo:");
        labelPhone = new JLabel("Telefono:");
        labelAge = new JLabel("Età:");
        if (person == null) {
            textFieldFirst = new JTextField(20);
            textFieldLast = new JTextField(20);
            textFieldAddress = new JTextField(20);
            textFieldPhone = new JTextField(10);
            textFieldAge = new JTextField(3);
        } else {
            textFieldFirst = new JTextField(person.getFirst(), 20);
            textFieldLast = new JTextField(person.getLast(), 20);
            textFieldAddress = new JTextField(person.getAddress(), 20);
            textFieldPhone = new JTextField(person.getPhone(), 10);
            textFieldAge = new JTextField(String.valueOf(person.getAge()), 3);
        }
        drawWindow(gui, "editor-persona");
    }

    public void drawWindow(Rubrica_GUI gui, String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 350));
        setSize(500, 350);

        ImageIcon salvaIcon = new ImageIcon("src/gui/icons/save.png");
        ImageIcon annullaIcon = new ImageIcon("src/gui/icons/cancel.png");
        ImageIcon frameIcon = new ImageIcon("src/gui/icons/frame_icon.png");
        setIconImage(frameIcon.getImage());

        Dimension inputSize = new Dimension(100, 30);
        labelFirst.setPreferredSize(inputSize);
        labelLast.setPreferredSize(inputSize);
        labelAddress.setPreferredSize(inputSize);
        labelPhone.setPreferredSize(inputSize);
        labelAge.setPreferredSize(inputSize);
        textFieldFirst.setPreferredSize(inputSize);
        textFieldLast.setPreferredSize(inputSize);
        textFieldAddress.setPreferredSize(inputSize);
        textFieldPhone.setPreferredSize(inputSize);
        textFieldAge.setPreferredSize(inputSize);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGBC = new GridBagConstraints();
        inputGBC.gridx = 0;
        inputGBC.gridy = 0;
        inputGBC.insets = new Insets(5, 5, 5, 5); // Margine di 5 pixel
        inputPanel.add(labelFirst, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldFirst, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(labelLast, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldLast, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(labelAddress, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldAddress, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(labelPhone, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldPhone, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(labelAge, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldAge, inputGBC);

        inputGBC.anchor = GridBagConstraints.WEST;

        JButton salvaButton = new JButton("Salva", salvaIcon);
        JButton annullaButton = new JButton("Annulla", annullaIcon);

        Dimension buttonSize = new Dimension(100, 30);
        salvaButton.setPreferredSize(buttonSize);
        annullaButton.setPreferredSize(buttonSize);

        salvaButton.addActionListener((e) -> {
            Persona p = getNewValues();
            if (p != null){
                gui.onClickUpdateWindowSalvaButton(p);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Uno o più campi sono vuoti o invalidi!");
            }
        });

        annullaButton.addActionListener((e) -> {
            gui.onClickUpdateWindowAnnullaButton();
            dispose();
        });

        JToolBar toolBar = new JToolBar();

        toolBar.add(salvaButton);
        toolBar.add(annullaButton);
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);

        setLocationRelativeTo(null);
    }

    public Persona getNewValues() {
        if (textFieldFirst.getText().trim().isEmpty())
            return null;
        if (textFieldLast.getText().trim().isEmpty())
            return null;
        if (textFieldAddress.getText().trim().isEmpty())
            return null;
        if (textFieldPhone.getText().trim().isEmpty() ||
            !textFieldPhone.getText().trim().matches("[0-9]+"))
            return null;
        if (textFieldAge.getText().trim().isEmpty() ||
            !textFieldAge.getText().trim().matches("[0-9]+"))
            return null;

        String[] newValues = {textFieldFirst.getText(), textFieldLast.getText(), textFieldAddress.getText(),textFieldPhone.getText(), textFieldAge.getText()};
        return new Persona(newValues);
    }
}
