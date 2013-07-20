/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.view;

import static java.awt.Component.CENTER_ALIGNMENT;
import javax.swing.*;

/**
 *
 * @author Wittmann
 */
public class StartView extends JPanel {

    public JTextField nickname;
    public JTextField ip;
    public JTextField port;
    public JButton connectButton;
    public JButton createButton;

    public StartView() {
        nickname = new JTextField(20);
        ip = new JTextField(15);
        port = new JTextField(5);

        connectButton = new JButton("Connect");
        createButton = new JButton("Create");

        nickname.setToolTipText("Enter nickname");
        nickname.setText("Dima");
        ip.setToolTipText("Enter ip");
        ip.setText("localhost");
        port.setToolTipText("Enter port number");
        port.setText("8888");

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        nickname.setAlignmentX(CENTER_ALIGNMENT);
        ip.setAlignmentX(CENTER_ALIGNMENT);
        port.setAlignmentX(CENTER_ALIGNMENT);
        connectButton.setAlignmentX(CENTER_ALIGNMENT);
        createButton.setAlignmentX(CENTER_ALIGNMENT);

        this.add(nickname);
        this.add(ip);
        this.add(port);
        this.add(connectButton);
        this.add(createButton);

    }
}
