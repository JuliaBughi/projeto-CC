import java.io.*;
import java.net.*;

public class AlertFlowServer implements Runnable {
    private static final int SERVER_PORT = 9877;
    private ServerSocket serverSocket;

    public AlertFlowServer() throws IOException {
        this.serverSocket = new ServerSocket(SERVER_PORT); // Criar o socket TCP
    }

    @Override
    public void run() {
        System.out.println("AlertFlow Server (TCP) is listening on port " + SERVER_PORT);

        while (true) {
            try {
                // Aceitar uma conexão de um NMS_Agent
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress());

                // Ler a mensagem de alerta do cliente
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String alertMessage = in.readLine();
                System.out.println("Received alert: " + alertMessage);

                // Responder ao alerta
                out.println("Alert received and logged");

                // Fechar a conexão com o cliente
                clientSocket.close();
                System.out.println("Connection closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Iniciar o servidor AlertFlow (TCP)
            Thread alertFlowServerThread = new Thread(new AlertFlowServer());
            alertFlowServerThread.start(); // Rodar o servidor TCP em uma nova thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

