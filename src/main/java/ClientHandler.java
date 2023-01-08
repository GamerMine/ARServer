import org.snf4j.core.EndingAction;
import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.DefaultSessionConfig;
import org.snf4j.core.session.ISessionConfig;
import packets.Packet;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

public class ClientHandler extends AbstractStreamHandler {
    @Override
    public void read(Object msg) {
        ByteArrayInputStream bis = new ByteArrayInputStream((byte[]) msg);
        ObjectInput in = null;

        try {
            in = new ObjectInputStream(bis);
            Packet packet = (Packet) in.readObject();
            System.err.println(packet.getReason());
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void event(SessionEvent event) {
        if (event == SessionEvent.CLOSED) {

            // Notify if the closing initiated by the server
            if (!getSession().getAttributes().containsKey(Client.BYE_TYPED)) {
                System.err.println("Connection closed. Type \"bye\" to exit");
            }
        }
    }

    @Override
    public ISessionConfig getConfig() {

        // Gently stop the selector loop if session associated
        // with this handler ends
        return new DefaultSessionConfig()
                .setEndingAction(EndingAction.STOP);
    }
}
