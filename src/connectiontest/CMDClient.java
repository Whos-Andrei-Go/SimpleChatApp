package connectiontest;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class CMDClient {

    public static void main(String args[]) {

        try {
            Socket s1 = new Socket("127.0.0.1", 5432);
            InputStream is = s1.getInputStream();
            OutputStream os = s1.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(os), true);

            Scanner scanner = new Scanner(System.in);

            System.out.println(br.readLine());

            Thread receiveThread = new Thread(new ReceiveMessages(br));
            Thread sendThread = new Thread(new SendMessages(scanner, pw));

            receiveThread.start();
            sendThread.start();

            sendThread.join();
            receiveThread.interrupt();

            br.close();
            pw.close();
            scanner.close();
            s1.close();

        } catch (ConnectException connExc) {
            System.err.println("Could not connect.");
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
                String serverMessage;
                while ((serverMessage = br.readLine()) != null) {
                    System.out.println("\rServer: " + serverMessage);
                    System.out.print("You: ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class SendMessages implements Runnable {
        private Scanner scanner;
        private PrintWriter pw;

        public SendMessages(Scanner scanner, PrintWriter pw) {
            this.scanner = scanner;
            this.pw = pw;
        }

        @Override
        public void run() {
            try {
                String userInput;
                while (true) {
                    System.out.print("You: ");
                    userInput = scanner.nextLine();
                    pw.println(userInput);
                    if (userInput.equalsIgnoreCase("exit")) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
