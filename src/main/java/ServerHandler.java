import org.snf4j.core.handler.AbstractStreamHandler;
import org.snf4j.core.handler.SessionEvent;
import org.snf4j.core.session.IStreamSession;
import packets.Command;
import packets.ErrorPacket;
import packets.InformationPacket;
import packets.Packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ServerHandler extends AbstractStreamHandler {

    private int USERID = 0;
    private String YOUID = "[you]";
    private int minPlayer;
    private int maxPlayer;

    private final Map<Long, IStreamSession> sessions = new HashMap<>();

    public ServerHandler(int minPlayer, int maxPlayer) {
        super();
        this.minPlayer = minPlayer;
        this.maxPlayer = maxPlayer;
    }

    @Override
    public void read(Object msg) {
        String s = new String((byte[])msg);

        sendToAll(s);
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void event(SessionEvent event) {
        switch (event) {
            case OPENED:
                System.out.println(sessions.size());
                if (sessions.size() >= maxPlayer) {
                    System.out.println("COUIFEGCYDSHQFE");
                    sendToCurrent(new ErrorPacket(Command.TOO_MANY_PLAYERS, "Il y as déjà " + maxPlayer + " dans la partie !"));
                    getSession().close();
                    return;
                }
                sessions.put(getSession().getId(), getSession());
                getSession().getAttributes().put(USERID, "["+getSession().getRemoteAddress()+"]");
                sendToAll("{connected}");
                break;

            case CLOSED:
                sessions.remove(getSession().getId());
                sendToAll("{disconnected}");
                break;
        }
    }

    private void sendToAll(String message) {
        long youId = getSession().getId();
        String userId = (String) getSession().getAttributes().get(USERID);

        for (IStreamSession session: sessions.values()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = null;

            try {
                outputStream = new ObjectOutputStream(bos);
                outputStream.writeObject(new InformationPacket(Command.INFO, (session.getId() == youId ? YOUID : userId) + ' ' + message));
                outputStream.flush();
                byte[] data = bos.toByteArray();
                session.write(data);
                bos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToCurrent(Packet packet) {
        getSession().write(packet);
    }
}
