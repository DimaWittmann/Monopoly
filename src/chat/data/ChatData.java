package chat.data;

import chat.Chat;
import chat.Client;
import chat.data.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wittmann
 */
public class ChatData {

    private ArrayList<Message> own;
    private ArrayList<Message> all;
    private Chat chat;
    public String nickname;
    public String addr;
    public int port;
    public Client client;
    public Thread observer;

    public ChatData(Chat chat) {
        this.chat = chat;
        own = new ArrayList<>();
        all = new ArrayList<>();
        observer = new Thread(new Observer());
    }

    public synchronized void addOwn(String str) {
        Message newMess = new Message(str, nickname, new Date());
        own.add(newMess);
        client.ownMessages.add(newMess);

    }

    public synchronized void addCommon(Message mess) {
        all.add(mess);
        chat.view.displayText.setText(chat.view.displayText.getText() + mess.author + ": " + mess.mess + '\n');
    }

    private class Observer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Message mess = client.commonMessages.take();
                    addCommon(mess);

                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }

            }
        }
    }
}
