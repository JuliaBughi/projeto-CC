package Server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NMS_Server {

    private static ConcurrentHashMap<String, List<MetricNT>> mapMetrics = new ConcurrentHashMap<>();

    public static void addMetric(String deviceId, MetricNT newMetric) {
        mapMetrics.compute(deviceId, (key, existingList) -> {
            if (existingList == null) {
                existingList = new ArrayList<>();
            }
            existingList.add(newMetric);
            // Sort the list by date in ascending order
            Collections.sort(existingList, Comparator.comparing(MetricNT::getDate));
            return existingList;
        });
    }

    public static List<MetricNT> getMetricsForDevice(String deviceId) {
        return mapMetrics.getOrDefault(deviceId, Collections.emptyList());
    }

    public static void printAllMetrics() {
        for (Map.Entry<String, List<MetricNT>> entry : mapMetrics.entrySet()) {
            String key = entry.getKey();
            List<MetricNT> metrics = entry.getValue();

            System.out.println(key +" metrics");
            for (MetricNT metric : metrics) {
                System.out.println(metric.toString());
            }
        }
    }

    public static void main(String[] args) throws IOException {

        String path = args[0]; //json passado como argumento

        NetTaskServer netTaskServer = new NetTaskServer(path);
        AlertFlowServer alertFlowServer = new AlertFlowServer();
        UserInterface userInterface = new UserInterface();

        new Thread(netTaskServer).start();
        new Thread(alertFlowServer).start();
        new Thread(userInterface).start();


        // as metricas têm de ter uma data associada para depois e conseguir pedir as metricas dos ultimos x minutos/horas
        // definir também o numero de pontos que se apresenta por unidade de tempo (ex: 20 por hora)
    }
}
