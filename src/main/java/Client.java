import org.snf4j.core.SelectorLoop;
import org.snf4j.core.session.IStreamSession;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    static final Integer BYE_TYPED = 0;

    public static void main(String[] args) throws Exception {
        SelectorLoop loop = new SelectorLoop();

        try {
            loop.start();

            // Initialize the connection
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress("127.0.0.1", 8002));

            // Register the channel
            IStreamSession session = (IStreamSession) loop.register(channel, new ClientHandler()).sync().getSession();

            // Confirm that the connection was successful
            session.getReadyFuture().sync();

            // Read commands from the standard input
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = in.readLine()) != null) {
                if (session.isOpen()) {
                    session.write((line).getBytes());
                }
            }
        }
        finally {

            // Gently stop the loop
            loop.stop();
        }
    }
}
