package connectiontest;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class CMDServer {

    public static void main(String args[]) {
        try {
            ServerSocket serverSocket = new ServerSocket(5432);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);

                System.out.println("Connected to client. Starting chat...");

                pw.println("Welcome to the chat!");

                Thread receiveThread = new Thread(new ReceiveMessages(br));
                Thread sendThread = new Thread(new SendMessages(pw));

                receiveThread.start();
                sendThread.start();

                sendThread.join();
                receiveThread.interrupt();

                br.close();
                pw.close();
                clientSocket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class ReceiveMessages implements Runnable {
        private BufferedReader br;

        public ReceiveMessages(BufferedReader br) {
            this.br = br;
        }

        @Override
        public void run() {
            try {
                String clientMessage;
                while ((clientMessage = br.readLine()) != null) {
                    if (clientMessage.equalsIgnoreCase("exit")) {
                        System.out.println("Client has exited the chat.");
                        break;
                    }
                    System.out.println("\rClient: " + clientMessage);
                    System.out.print("Server: ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class SendMessages implements Runnable {
        private PrintWriter pw;

        public SendMessages(PrintWriter pw) {
            this.pw = pw;
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                String serverMessage;
                while (true) {
                    System.out.print("Server: ");
                    serverMessage = scanner.nextLine();
                    pw.println(serverMessage);
                    if (serverMessage.equalsIgnoreCase("exit")) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
