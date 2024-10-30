import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class UDPClient {
    public static void main(String args){
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost"); //o stor falou sobre colocar também o 10.0.0...
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your user ID: ");
            String userId = scanner.nextLine();

            while(true){
                System.out.println("Enter your message: ");
                String message = scanner.nextLine();

                //Prepare the protocol message(userid, timestamp, message)
                String timestamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
                String clientMessage = userId + ":" + timestamp + ":" + message;

                //Send message to server
                byte[] sendData = clientMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9876);

                //Receive response from server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                //Print the server's response
                String serverResponse = new String(receivePacket.getData(),0,receivePacket.getLength());
                System.out.println("Server response: " + serverResponse);

                //Option to quit
                System.out.print("Do you want to send another message? Y/N");
                String answer = scanner.nextLine();
                if(answer.equals("Y")){
                    break;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            if(socket != null && socket.isClosed()){}
        }
    }
}
