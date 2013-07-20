/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.data.Message;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.windows.ThemeReader;

/**
 *
 * @author Wittmann
 */
public class Handler implements Runnable {

    private SocketChannel channel;
    private SelectionKey key;
    private BlockingQueue queue;

    Handler(Selector selector, SocketChannel channel, BlockingQueue queue) throws ClosedChannelException, IOException {
        this.channel = channel;
        channel.configureBlocking(false);

        this.queue = queue;

        key = this.channel.register(selector, 0);
        key.attach(this);
        key.interestOps(SelectionKey.OP_READ);

        selector.wakeup();
    }

    @Override
    public void run() {
        ByteBuffer buf = ByteBuffer.allocate(8196);
        try {
            int bytesRead = channel.read(buf);
            while (bytesRead > 0) {
                bytesRead = channel.read(buf);
                System.out.println(bytesRead);
            }
        } catch (IOException ex) {

            System.out.println(ex.getMessage() + this.toString());
        }

        try {
            buf.flip();
            ObjectInputStream ois;
            Message message = null;
            try (ByteArrayInputStream bias = new ByteArrayInputStream(buf.array(), 0, buf.limit())) {
                ois = new ObjectInputStream(bias);
                message = (Message) ois.readObject();
                ois.close();
            }

            queue.add(message);
        } catch (IOException | ClassNotFoundException ex) {
        }
        Thread.currentThread().interrupt();
    }
}
