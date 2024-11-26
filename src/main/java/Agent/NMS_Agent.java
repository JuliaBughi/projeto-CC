package Agent;

public class NMS_Agent {

    public static void main(String[] args) throws Exception {
        // Iniciar comunicação UDP e TCP
        String server_ip = args[0];
        String device_id = args[1];

        NetTaskAgent netTaskAgent = new NetTaskAgent(server_ip,device_id);
        AlertFlowAgent alertFlowAgent = new AlertFlowAgent();

        new Thread(netTaskAgent).start();
        new Thread(alertFlowAgent).start();
    }

}
