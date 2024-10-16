import Server.ThreadServer;
public class Main {
    public static void main(String[] args) {
        ThreadServer threadServer = new ThreadServer();
        threadServer.serve();
    }
}