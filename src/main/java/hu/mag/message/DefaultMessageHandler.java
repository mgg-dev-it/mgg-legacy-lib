package hu.mag.message;

import java.util.Vector;

/**
 *
 * @author MaG
 */
public class DefaultMessageHandler implements MessageHandlerInterface {

    private Vector<MessageEventListener> vMessageEventListeners = new Vector<>();

    public DefaultMessageHandler() {
    }

    public void addMessageEventListener(MessageEventListener mel) {
        for (int i = 0; i < vMessageEventListeners.size(); i++) {
            if (vMessageEventListeners.elementAt(i).equals(mel)) {
                return;
            }
        }
        vMessageEventListeners.add(mel);
    }

    public void removeMessageEventListener(MessageEventListener mel) {
        for (int i = 0; i < vMessageEventListeners.size(); i++) {
            if (vMessageEventListeners.elementAt(i).equals(mel)) {
                vMessageEventListeners.remove(i);
                return;
            }
        }
    }

    @Override
    public void handleMessage(MessageEvent e) {
        for (int i = 0; i < vMessageEventListeners.size(); i++) {
            vMessageEventListeners.elementAt(i).messageEventPerformed(e);
        }
    }

}
