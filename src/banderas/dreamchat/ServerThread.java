package banderas.dreamchat;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class ServerThread extends Thread{
    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String name;


    public ServerThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        name = clientSocket.getInetAddress().toString();

        this.start();
    }

    @Override
    public void run() {
        String message;
        while (true){
            try{
                message = reader.readLine();
                System.out.printf("\n%s at %s said: %s\n", name, LocalDateTime.now(), message);

                for (ServerThread st : Server.serverThreads){
                    st.sendMessage(message);
                }
                System.out.println("Message was sent to clients");

            }catch (IOException exception){
                System.out.println(exception.getMessage());
            }

            }
    }

    private void sendMessage(String message) throws IOException{
        LocalDateTime time = LocalDateTime.now();
        try{
            writer.write( String.format("%s at %s say: %s\n",name ,time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), message));
            writer.flush();
        }catch (IOException io){
            sendMessage(message);
        }
    }



}
