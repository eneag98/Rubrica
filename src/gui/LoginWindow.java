package gui;

import backend.Utente;

import javax.swing.*;
import java.awt.*;

public class LoginWindow extends JFrame {
    private final JLabel labelUsername, labelPassword;
    private final JTextField textFieldUsername;
    private final JPasswordField textFieldPassword;

    public LoginWindow(Rubrica_GUI gui) {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(400, 325));
        setSize(400, 325);

        ImageIcon usernameIcon = new ImageIcon("icons/username.png");
        ImageIcon passwordIcon = new ImageIcon("icons/password.png");
        ImageIcon loginIcon = new ImageIcon("icons/login.png");
        ImageIcon frameIcon = new ImageIcon("icons/frame_icon.png");
        setIconImage(frameIcon.getImage());

        labelUsername = new JLabel("Username:", usernameIcon, JLabel.LEFT);
        labelPassword = new JLabel("Password:", passwordIcon, JLabel.LEFT);

        textFieldUsername = new JTextField(20);
        textFieldPassword = new JPasswordField(20);

        Dimension inputSize = new Dimension(100, 30);
        labelUsername.setPreferredSize(inputSize);
        labelPassword.setPreferredSize(inputSize);
        textFieldUsername.setPreferredSize(inputSize);
        textFieldPassword.setPreferredSize(inputSize);

        JButton loginButton = new JButton("LOGIN", loginIcon);
        loginButton.setPreferredSize(new Dimension(100, 30));

        loginButton.addActionListener((e) -> {
            Utente u = getLoginValues();
            if (u != null){
                gui.OnClickLoginWindowLoginButton(u);
            } else {
                JOptionPane.showMessageDialog(this, "Uno o più campi sono vuoti o invalidi!","Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints inputGBC = new GridBagConstraints();
        inputGBC.gridx = 0;
        inputGBC.gridy = 0;
        inputGBC.insets = new Insets(5, 5, 5, 5); // Margine di 5 pixel
        inputPanel.add(labelUsername, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldUsername, inputGBC);

        inputGBC.gridy++;
        inputGBC.gridx = 0;
        inputPanel.add(labelPassword, inputGBC);

        inputGBC.gridx++;
        inputPanel.add(textFieldPassword, inputGBC);

        inputGBC.gridy += 2;
        inputGBC.gridx = 1;
        inputPanel.add(loginButton, inputGBC);

        inputGBC.anchor = GridBagConstraints.WEST;

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);

        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(loginButton);
        requestFocus();
    }

    public Utente getLoginValues() {
        if (textFieldUsername.getText().trim().isEmpty())
            return null;
        String pass = new String(textFieldPassword.getPassword());
        if (pass.trim().isEmpty())
            return null;

        return new Utente(textFieldUsername.getText(), new String(textFieldPassword.getPassword()));
    }
}
