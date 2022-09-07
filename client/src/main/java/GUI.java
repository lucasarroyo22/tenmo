import services.AuthenticationService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements Runnable{

    static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    public AuthenticationService authenticationService = new AuthenticationService();

    private String[] loginInfo = new String[2];

    public void run(){


        //Creating the Frame
        JFrame frame = new JFrame("Tenmo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 200);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Username");
        JTextField tf = new JTextField(20); // accepts up to 20 characters
        JButton submit = new JButton("Submit");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(submit);

        //When Submit is pressed
        submit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String usernameTextField = tf.getText();
                //System.out.println("Submitted!");
                //JOptionPane.showMessageDialog(frame, "Submitted! Welcome " + usernameTextField + "!");
                panel.setVisible(false);

                JPanel panel2 = new JPanel(); // the panel is not visible in output
                JLabel label2 = new JLabel("Enter Password");
                JTextField tf2 = new JTextField(20); // accepts up to 20 characters
                JButton submit2 = new JButton("Submit");
                panel2.add(label2); // Components Added using Flow Layout
                panel2.add(tf2);
                panel2.add(submit2);
                //When Submit is pressed for Password
                submit2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String passwordTextField = tf2.getText();
                        loginInfo[0] = usernameTextField;
                        loginInfo[1] = authenticationService.login(usernameTextField, passwordTextField);
                        if(loginInfo[1] == null){
                            JOptionPane.showMessageDialog(frame, "Login Information Invalid, Try Again.");
                        }else{
                            System.out.println("Login info = " + loginInfo[1]);
                            JOptionPane.showMessageDialog(frame, "Login Information Verified!");

                        }
                        panel2.setVisible(false);
                    }
                });
                frame.getContentPane().add(BorderLayout.CENTER, panel2);    //show panel2
            }
        });

        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
        System.out.println(loginInfo[1]);
        //return loginInfo;
    }
}
