package frame;

import org.eclipse.jdt.internal.compiler.batch.Main;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

import static java.util.prefs.Preferences.*;

public class loginframe extends JFrame {
    private JTextField usernametextfield;

    private JButton loginButton;
    private JPasswordField passwordtextfield;
    private JPanel mainPanel;

    public loginframe () {
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        pack();
        loginButton.addActionListener(e -> {
            if(usernametextfield.getText().equals("admin")){
                if(new String(passwordtextfield.getPassword()).equals("admin")){
                    Preferences pref = userRoot().node(Main.class.getName());
                    pref.put("USER_ID","1");
                    DataMahasiswaFrame mf = new DataMahasiswaFrame();
                    mf.setVisible(true);
                    dispose();
                }
            }
        });
    }
}
