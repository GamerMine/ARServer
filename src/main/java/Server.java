import org.snf4j.core.SelectorLoop;
import org.snf4j.core.factory.AbstractSessionFactory;
import org.snf4j.core.handler.IStreamHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server implements Runnable{

    private final int PORT;
    private final int MIN_PLAYER;
    private final int MAX_PLAYER;

    public Server(int port, int nbMinPlayers, int nbMaxPlayers) {
        this.PORT = port;
        this.MIN_PLAYER = nbMinPlayers;
        this.MAX_PLAYER = nbMaxPlayers;
    }

    @Override
    public void run() {

        SelectorLoop loop;
        try {
            loop = new SelectorLoop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            loop.start();

            // Initialize the listener
            ServerSocketChannel channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(8002));

            ServerHandler serverHandler = new ServerHandler(MIN_PLAYER, MAX_PLAYER);

            // Register the listener
            loop.register(channel, new AbstractSessionFactory() {

                @Override
                protected IStreamHandler createHandler(SocketChannel channel) {
                    System.out.println("copucoe");
                    return serverHandler;
                }
            }).sync();

            // Wait till the loop ends
            loop.join();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {

            // Gently stop the loop
            loop.stop();
        }
    }
}
