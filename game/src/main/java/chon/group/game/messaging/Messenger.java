package chon.group.game.messaging;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Messenger {

    /** List of messages currently being displayed. */
    private List<Message> messages;

    public Messenger() {
        this.messages = new ArrayList<Message>();
    }

    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Updates and removes expired messages from the system (environment).
     */
    public void update() {
        Iterator<Message> iterator = this.messages.iterator();
        while (iterator.hasNext()) {
            Message message = iterator.next();
            if (!message.update()) {
                iterator.remove();
            }
        }
    }

}