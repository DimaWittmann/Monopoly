package chat.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;

/**
 *
 * @author Wittmann
 */
public class ChatView extends JPanel {

    public JTextArea displayText = new JTextArea();
    public JTextField enterText = new JTextField();
    public JButton submitButton = new JButton();

    public ChatView() {
        displayText = new JTextArea();
        submitButton = new JButton("Enter");

        this.setLayout(new BorderLayout());

        displayText.setAutoscrolls(true);
        displayText.setEditable(false);
        JScrollPane scroll = new JScrollPane(displayText);

        this.add(scroll, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(enterText, BorderLayout.PAGE_START);
        panel.add(submitButton, BorderLayout.PAGE_END);

        this.add(panel, BorderLayout.PAGE_END);

    }
}
