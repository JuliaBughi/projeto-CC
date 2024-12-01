package Agent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class NMS_Agent {

    private static final BlockingQueue<String> taskQueue = new LinkedBlockingQueue<>(10);


    public static void main(String[] args) throws Exception {
        // Iniciar comunicação UDP e TCP
        String server_ip = args[0];
        String device_id = args[1];

        NetTaskAgent netTaskAgent = new NetTaskAgent(server_ip,device_id, taskQueue);
        AlertFlowAgent alertFlowAgent = new AlertFlowAgent(server_ip, device_id, taskQueue);

        new Thread(netTaskAgent).start();
        new Thread(alertFlowAgent).start();

    }

}
