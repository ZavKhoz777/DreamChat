package banderas.dreamchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedList;
import java.util.List;

public class Server {
    private static int PORT = 3000;
    private ServerSocket serverSocket;
    static List<ServerThread> serverThreads;

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();


    }

    private void resolveException(IOException exception){
        System.out.println(exception.getMessage());
        startServer();
    }

    private void startServer(){
        serverThreads = new LinkedList<>();
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Started! Waiting for clients...");

            while (true){
                Socket clientSocket = serverSocket.accept();
                System.out.printf("%s Connected now!\n%s",clientSocket.getInetAddress(),
                        LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
                serverThreads.add(new ServerThread(clientSocket));
            }

        }catch (IOException ioException){
            resolveException(ioException);
        }
    }


}
