package chat;

import chat.view.ChatView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wittmann
 */
public class ChatController {

    Chat chat;

    public ChatController(Chat chat) {
        this.chat = chat;
        chat.view.submitButton.addActionListener(new ButtonListener());
        chat.startView.connectButton.addActionListener(new ButtonListener());
        chat.startView.createButton.addActionListener(new ButtonListener());
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals(chat.view.submitButton.getActionCommand())) {
                String str = chat.view.enterText.getText();

                if (!str.equals("")) {
                    chat.data.addOwn(str);
                    chat.view.enterText.setText("");
                }
            }
            if (e.getActionCommand().equals(chat.startView.connectButton.getActionCommand())) {

                if (!(chat.startView.nickname.getText().equals("")
                        || chat.startView.ip.getText().equals("")
                        || chat.startView.port.getText().equals(""))) {
                    saveInput();
                    try {
                        startClient();
                    } catch (IOException | InterruptedException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
            if (e.getActionCommand().equals(chat.startView.createButton.getActionCommand())) {

                if (!(chat.startView.nickname.getText().equals("")
                        || chat.startView.ip.getText().equals("")
                        || chat.startView.port.getText().equals(""))) {
                    saveInput();
                    startServer();
                    //startClient();
                }

            }

        }

        private void saveInput() {
            chat.data.addr = chat.startView.ip.getText();
            chat.data.port = Integer.parseInt(chat.startView.port.getText());
            chat.data.nickname = chat.startView.nickname.getText();

        }

        private void startServer() {
            try {
                Reactor r = new Reactor(chat.data.addr, chat.data.port);
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.start();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        private void startClient() throws IOException, InterruptedException {
            Client client;

            client = new Client(chat.data.addr, chat.data.port);
            chat.data.client = client;
            chat.data.observer.start();
            new Thread(client).start();

            chat.showChat();
        }
    }
}
