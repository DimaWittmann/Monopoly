/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.data.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
public class Reactor implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverChannel;
    public ArrayList<SocketChannel> acceptedConnections;
    public BlockingQueue<Message> messageQueue = new LinkedBlockingQueue();

    public Reactor(String addr, int port) throws IOException {
        selector = Selector.open();

        serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(addr, port));
        serverChannel.configureBlocking(false);

        SelectionKey sk = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());

        acceptedConnections = new ArrayList<>();
    }

    @Override
    public void run() {
        while (true) {
            try {
                int connection = selector.select(1000);

                if (connection > 0) {
                    Iterator it = selector.selectedKeys().iterator();
                    while (it.hasNext()) {
                        dispatch((SelectionKey) it.next());
                        it.remove();
                    }
                    selector.selectedKeys().clear();
                } else {
                    checkForMessages();
                }

            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "1");
            }
        }

    }

    private void checkForMessages() throws IOException {

        while (!messageQueue.isEmpty()) {

            Message message = messageQueue.poll();

            for (SocketChannel channel : acceptedConnections) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = null;
                out = new ObjectOutputStream(bos);
                out.writeObject(message);
                ByteBuffer buf = ByteBuffer.wrap(bos.toByteArray());
                try {
                    channel.write(buf);
                } catch (IOException ex) {
                    System.out.println(channel.getRemoteAddress() + "is disconnected");
                    acceptedConnections.remove(channel);
                }
                bos.close();
                out.close();
            }
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            new Thread(r).start();
        }
    }

    private class Acceptor implements Runnable {

        @Override
        public void run() {
            try {
                SocketChannel channel = serverChannel.accept();

                if (channel != null) {
                    acceptedConnections.add(channel);

                    new Handler(selector, channel, messageQueue);
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "3");
            }
        }
    }
}
