package banderas.dreamchat.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static String address;
    private static int port;
    private static Socket socket;
    private static BufferedReader reader;
    private static BufferedWriter writer;
    private static Scanner scanner;

    public static void main(String[] args) {
        if(startMessaging() == 0)
            System.out.printf("Welcome to DreamChatClient\nNow you are connected to %s\n\n", socket.getInetAddress());
        else System.out.println("Error!");
    }

    private static int startMessaging(){
        initClient();

        if(socket.isConnected()){
            SendingMessage sendingMessage = new SendingMessage();
            ReadingMessages readingMessages = new ReadingMessages();
        }

        return 0;
    }

    private static boolean initClient(){
        address = "localhost";
        port = 3000;
        try {
            socket = new Socket(address, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            scanner = new Scanner(System.in);
        }catch (IOException ioException){
            System.out.println(ioException.getMessage());
            return false;
        }
        return true;
    }

    private static void closeClient() {
        try {
            socket.close();
            reader.close();
            writer.close();
            scanner.close();
        } catch (IOException ioe) {
        }
    }

    static class SendingMessage extends Thread {
        public SendingMessage() {
            start();
        }

        @Override
        public void run() {
            String userInput;
            while (true){
                userInput = scanner.nextLine();
                if(userInput.equals("exit".toLowerCase())) {
                    closeClient();
                    break;
                }
                send(userInput);

            }
        }

        private void send(String message){
            try {
                writer.write(message + "\n");
                writer.flush();
            }catch (IOException ioe){}
        }

    }

        static class ReadingMessages extends Thread {
        public ReadingMessages() {
            start();

        }

        @Override
        public void run() {
            try{
                listeningMessages();
            }catch (IOException io){}
        }

        private void listeningMessages() throws IOException{
            String message;
            while ((message = reader.readLine()) != null){
                System.out.println(message);
            }
            System.out.println("Disconnected");
            closeClient();
        }

    }
}