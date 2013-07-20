package chat;

import chat.data.ChatData;
import chat.view.ChatView;
import chat.view.StartView;
import java.awt.*;
import java.io.*;
import javax.swing.JFrame;

/**
 *
 * @author Wittmann
 */
public class Chat extends JFrame {

    public ChatController controller;
    public ChatView view;
    public ChatData data;
    public StartView startView;

    public Chat(String name) {
        super(name);
        view = new ChatView();
        startView = new StartView();
        controller = new ChatController(this);
        data = new ChatData(this);
        this.add(startView);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300, 150));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 150,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 75);
        this.setVisible(true);
    }

    public void showChat() {
        this.setVisible(false);
        remove(startView);
        add(view);
        this.setMinimumSize(new Dimension(500, 300));
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 150,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 75);
        this.setVisible(true);
    }

    public static void main(String args[]) {

        Chat chat = new Chat("Chat");



    }
}
