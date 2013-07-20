package chat.data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Wittmann
 */
public class Message implements Serializable {

    public String mess;
    public String author;
    public Date date;

    public Message(String mess, String author, Date date) {
        this.mess = mess;
        this.author = author;
        this.date = date;
    }

    public String toString() {
        return date + ": " + author + ": " + mess;
    }
}
