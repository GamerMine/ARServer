public class Test {
    public static void main(String[] args) {
        Server server = new Server(8002, 1, 2);
        Thread serverThread = new Thread(server);

        serverThread.start();

        System.out.println("Coucou");
    }
}
