package bteamdevelopment.qrapplication;

/**
 * Created by wkohusjr on 11/19/2015.
 */

import java.util.Date;
import java.util.ArrayList;

/* Created by wkohusjr on 9/23/2015.
 * Contact Class provides all of the getters and setters for the attributes
 */
public class Message extends ArrayList<Message> {

    String id;
    String sender = null;
    String receiver = null;
    String message = null;
    Date date = null;

    public Message() {

    }

    public Message(String id, String sender, String receiver, String message, Date date) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return sender + " " + receiver + " " + message;
    }
}


