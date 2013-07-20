/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.data.Message;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Wittmann
 */
public class Client implements Runnable {

    private Selector selector;
    private SocketChannel channel;
    public BlockingQueue<Message> ownMessages;
    public BlockingQueue<Message> commonMessages;

    public Client(String addr, int port) throws IOException, InterruptedException {
        selector = Selector.open();

        channel = SocketChannel.open();

        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress(addr, port));

        try {
            while (!channel.finishConnect()) {
                Thread.sleep(500);
            }
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());

        }
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);

        ownMessages = new LinkedBlockingQueue();
        commonMessages = new LinkedBlockingQueue();
    }

    @Override
    public void run() {
        while (true) {
            try {
                int connection = selector.select(250);
                if (connection > 0) {

                    for (SelectionKey newKey : selector.selectedKeys()) {
                        if (newKey.isReadable()) {
                            readMessages((SocketChannel) newKey.channel());
                        }
                    }
                    selector.selectedKeys().clear();
                }
                if (channel.isConnected()) {
                    writeMessages();
                }

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void writeMessages() throws IOException {
        while (!ownMessages.isEmpty()) {
            Message message = ownMessages.poll();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(message);
            out.close();
            bos.close();
            ByteBuffer buf = ByteBuffer.allocate(8196);

            buf.put(ByteBuffer.wrap(bos.toByteArray()));
            buf.flip();

            channel.write(buf);

        }
    }

    private void readMessages(SocketChannel newChannel) throws IOException, ClassNotFoundException {

        ByteBuffer buf = ByteBuffer.allocate(8192);
        channel.read(buf);
        buf.flip();
        ByteArrayInputStream bias =
                new ByteArrayInputStream(buf.array(), 0, buf.limit());
        ObjectInputStream ois = new ObjectInputStream(bias);
        Message message = (Message) ois.readObject();
        commonMessages.add(message);
        System.out.println(commonMessages);
        bias.close();
        ois.close();
    }
}
