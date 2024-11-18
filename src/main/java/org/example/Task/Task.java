package org.example.Task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Task {
    private String task_id;
    private int frequency;
    private List<Device> devices;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public Bandwidth getBandwidth(){
        return null;
    }

    @Override
    public String toString() {
        return "Task.Task{" +
                "task_id='" + task_id + '\'' +
                ", frequency=" + frequency +
                ", devices=" + devices +
                '}';
    }

    public static List<Task> jsonReader(String filepath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filepath);
        Task[] tasks = objectMapper.readValue(jsonFile,Task[].class);

        List<Task> l = new ArrayList<>(Arrays.asList(tasks));

        return l;
    }

    public static List<Task> getTasksForDevice(String device_id, List<Task> list) {
        List<Task> tasksForDevice = new ArrayList<>();
        for (Task task : list) {
            for (Device device : task.getDevices()) {
                if (device.getDevice_id().equals(device_id)) {
                    tasksForDevice.add(task);  // Adiciona a tarefa se o device_id corresponder
                    break;  // Não precisa procurar mais dispositivos dentro dessa tarefa
                }
            }
        }
        return tasksForDevice;  // Retorna a lista de tarefas associadas ao device_id
    }

    public static String TasksToString(List<Task> tasks, String device_id) {
        return tasks.stream()
                .map(task -> TaskToString(task, device_id))
                .collect(Collectors.joining("#")); // as tarefas ficam divididas por #
    }

    public static List<Task> StringToTasks(String serializedTasks) {
        List<Task> tasks = new ArrayList<>();
        String[] taskStrings = serializedTasks.split("#");

        for (String taskString : taskStrings) {
            tasks.add(StringToTask(taskString));
        }

        return tasks;
    }

    public static String TaskToString(Task task,String device_id) {

        Device d = (Device) task.devices.stream().filter(device -> device != null && device.getDevice_id().equals(device_id));

        String device = Device.DeviceToString(d);

        return String.format("%s;%d;%s", task.task_id, task.frequency,device);
    }

    private static Task StringToTask(String taskString){
        String[] parts = taskString.split(";");
        if (parts.length < 3) return null;

        Task task = new Task();
        task.task_id = parts[0];
        task.frequency = Integer.parseInt(parts[1]);

        String s = parts[2];
        task.devices = Arrays.asList(Device.StringToDevice(s));

        return task;
    }
}

class Device {

    private String device_id;
    private DeviceMetrics device_metrics;
    private LinkMetrics link_metrics;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public DeviceMetrics getDevice_metrics() {
        return device_metrics;
    }

    public void setDevice_metrics(DeviceMetrics device_metrics) {
        this.device_metrics = device_metrics;
    }

    public LinkMetrics getLink_metrics() {
        return link_metrics;
    }

    public void setLink_metrics(LinkMetrics link_metrics) {
        this.link_metrics = link_metrics;
    }

    @Override
    public String toString() {
        return "Task.Device{" +
                "device_id='" + device_id + '\'' +
                ", device_metrics=" + device_metrics +
                ", link_metrics=" + link_metrics +
                '}';
    }

    public static String DeviceToString(Device device) {

        String deviceMetrics = DeviceMetrics.DeviceMetricsToString(device.device_metrics);
        String linkMetrics = LinkMetrics.LinkMetricsToString(device.link_metrics);
        return String.format("%s;%s;%s",device.device_id, deviceMetrics, linkMetrics);
    }

    public static Device StringToDevice(String deviceString) {
        String[] parts = deviceString.split(";");
        //if (parts.length < 3) return null;

        Device device = new Device();
        device.device_id = parts[0];
        device.device_metrics = DeviceMetrics.StringToDeviceMetrics(parts[1]);
        device.link_metrics = LinkMetrics.StringToLinkMetrics(parts[2]);

        return device;
    }
}

class DeviceMetrics {
    private boolean cpu_usage;
    private boolean ram_usage;
    private List<String> interface_stats;

    public boolean getCpu_usage() {
        return cpu_usage;
    }

    public void setCpu_usage(boolean cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    public boolean getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(boolean ram_usage) {
        this.ram_usage = ram_usage;
    }

    public List<String> getInterface_stats() {
        return interface_stats;
    }

    public void setInterface_stats(List<String> interface_stats) {
        this.interface_stats = interface_stats;
    }

    @Override
    public String toString() {
        return "Task.DeviceMetrics{" +
                "cpu_usage=" + cpu_usage +
                ", ram_usage=" + ram_usage +
                ", interface_stats=" + interface_stats +
                '}';
    }

    public static String DeviceMetricsToString(DeviceMetrics metrics) {
        String interfaceStats = String.join("|", metrics.interface_stats);
        return String.format("%b,%b,%s",
                metrics.cpu_usage, metrics.ram_usage, interfaceStats);
    }

    public static DeviceMetrics StringToDeviceMetrics(String metricsString) {
        String[] parts = metricsString.split(",");
        if (parts.length < 3) return null;

        DeviceMetrics metrics = new DeviceMetrics();
        metrics.cpu_usage = Boolean.parseBoolean(parts[0]);
        metrics.ram_usage = Boolean.parseBoolean(parts[1]);
        metrics.interface_stats = Arrays.asList(parts[2].split("\\|"));

        return metrics;
    }
}

// até aqui era tudo obrigatório estar no Json, daqui para baixo é opcional
class LinkMetrics {
    private Bandwidth bandwidth;
    private Jitter jitter;
    private PacketLoss packet_loss;
    private Latency latency;
    private AlertFlowConditions alertflow_conditions;

    public Bandwidth getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(Bandwidth bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Jitter getJitter() {
        return jitter;
    }

    public void setJitter(Jitter jitter) {
        this.jitter = jitter;
    }

    public PacketLoss getPacket_loss() {
        return packet_loss;
    }

    public void setPacket_loss(PacketLoss packet_loss) {
        this.packet_loss = packet_loss;
    }

    public Latency getLatency() {
        return latency;
    }

    public void setLatency(Latency latency) {
        this.latency = latency;
    }

    public AlertFlowConditions getAlertflow_conditions() {
        return alertflow_conditions;
    }

    public void setAlertflow_conditions(AlertFlowConditions alertflow_conditions) {
        this.alertflow_conditions = alertflow_conditions;
    }

    @Override
    public String toString() {
        return "Task.LinkMetrics{" +
                "bandwidth=" + bandwidth +
                ", jitter=" + jitter +
                ", packet_loss=" + packet_loss +
                ", latency=" + latency +
                ", alertflow_conditions=" + alertflow_conditions +
                '}';
    }

    public static String LinkMetricsToString(LinkMetrics metrics) {
        String bandwidth = Bandwidth.BandwidthToString(metrics.bandwidth);
        String jitter = Jitter.JitterToString(metrics.jitter);
        String packetLoss = PacketLoss.PacketLossToString(metrics.packet_loss);
        String latency = Latency.LatencyToString(metrics.latency);
        String alertFlow = AlertFlowConditions.AlertFlowConditionsToString(metrics.alertflow_conditions);
        return String.format("%s;%s;%s;%s;%s", bandwidth, jitter, packetLoss, latency, alertFlow);
    }

    public static LinkMetrics StringToLinkMetrics(String linkMetricsString) {
        String[] parts = linkMetricsString.split(";");
        if (parts.length < 5) return null;

        LinkMetrics linkMetrics = new LinkMetrics();
        linkMetrics.bandwidth = Bandwidth.StringToBandwidth(parts[0]);
        linkMetrics.jitter = Jitter.StringToJitter(parts[1]);
        linkMetrics.packet_loss = PacketLoss.StringToPacketLoss(parts[2]);
        linkMetrics.latency = Latency.StringToLatency(parts[3]);
        linkMetrics.alertflow_conditions = AlertFlowConditions.StringToAlertFlowConditions(parts[4]);

        return linkMetrics;
    }
}

class Bandwidth {
    String tool;
    String role;
    String server_address;
    int duration;
    String transport_type;
    int frequency;

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getServer_address() {
        return server_address;
    }

    public void setServer_address(String server_address) {
        this.server_address = server_address;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTransport_type() {
        return transport_type;
    }

    public void setTransport_type(String transport_type) {
        this.transport_type = transport_type;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Task.Bandwidth{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public static String BandwidthToString(Bandwidth bandwidth) {
        return String.format("%s,%s,%s,%d,%s,%d",
                bandwidth.tool, bandwidth.role, bandwidth.server_address,
                bandwidth.duration, bandwidth.transport_type, bandwidth.frequency);
    }

    public static Bandwidth StringToBandwidth(String bandwidthString) {
        String[] parts = bandwidthString.split(",");
        if (parts.length < 6) return null;

        if(parts[0].equals("*")) // se no Json as metricas forem * (string) ou -1 (int) é para ignorar
            return null;

        Bandwidth bandwidth = new Bandwidth();
        bandwidth.tool = parts[0];
        bandwidth.role = parts[1];
        bandwidth.server_address = parts[2];
        bandwidth.duration = Integer.parseInt(parts[3]);
        bandwidth.transport_type = parts[4];
        bandwidth.frequency = Integer.parseInt(parts[5]);

        return bandwidth;
    }
}

class Jitter extends Bandwidth{

    @Override
    public String toString() {
        return "Task.Jitter{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }
    public static String JitterToString(Jitter jitter) {
        return BandwidthToString(jitter);
    }

    public static Jitter StringToJitter(String jitterString) {
        return (Jitter) StringToBandwidth(jitterString);
    }
}

class PacketLoss extends Bandwidth{

    @Override
    public String toString() {
        return "Task.PacketLoss{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public static String PacketLossToString(PacketLoss packetLoss) {
        return BandwidthToString(packetLoss);
    }

    public static PacketLoss StringToPacketLoss(String packetLossString) {
        return (PacketLoss) StringToBandwidth(packetLossString);
    }
}

class Latency {
    private String tool;
    private String destination;
    private int count;
    private int frequency;

    public String getTool() {
        return tool;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Task.Latency{" +
                "tool='" + tool + '\'' +
                ", destination='" + destination + '\'' +
                ", count='" + count + '\'' +
                ", frequency=" + frequency +
                '}';
    }

    public static String LatencyToString(Latency latency) {
        if (latency == null) return "";
        return String.format("%s,%s,%d,%d",
                latency.getTool(), latency.getDestination(), latency.getCount(), latency.getFrequency());
    }

    public static Latency StringToLatency(String latencyString) {
        String[] parts = latencyString.split(",");
        if (parts.length < 4) return null;

        if(parts[0].equals("*")) // se no Json as metricas forem * (string) ou -1 (int) é para ignorar
            return null;

        Latency latency = new Latency();
        latency.tool = parts[0];
        latency.destination = parts[1];
        latency.count = Integer.parseInt(parts[2]);
        latency.frequency = Integer.parseInt(parts[3]);

        return latency;
    }

}

class AlertFlowConditions { // se no Json alguma metrica for -1 então é para ignorar
    private int cpu_usage;
    private int ram_usage;
    private int interface_stats;
    private int packet_loss;
    private int jitter;

    public int getCpu_usage() {
        return cpu_usage;
    }

    public void setCpu_usage(int cpu_usage) {
        this.cpu_usage = cpu_usage;
    }

    public int getRam_usage() {
        return ram_usage;
    }

    public void setRam_usage(int ram_usage) {
        this.ram_usage = ram_usage;
    }

    public int getInterface_stats() {
        return interface_stats;
    }

    public void setInterface_stats(int interface_stats) {
        this.interface_stats = interface_stats;
    }

    public int getPacket_loss() {
        return packet_loss;
    }

    public void setPacket_loss(int packet_loss) {
        this.packet_loss = packet_loss;
    }

    public int getJitter() {
        return jitter;
    }

    public void setJitter(int jitter) {
        this.jitter = jitter;
    }

    @Override
    public String toString() {
        return "Task.AlertFlowConditions{" +
                "cpu_usage=" + cpu_usage +
                ", ram_usage=" + ram_usage +
                ", interface_stats=" + interface_stats +
                ", packet_loss=" + packet_loss +
                ", jitter=" + jitter +
                '}';
    }

    public static String AlertFlowConditionsToString(AlertFlowConditions alertFlow) {
        return String.format("%d,%d,%d,%d,%d",
                alertFlow.cpu_usage, alertFlow.ram_usage, alertFlow.interface_stats,
                alertFlow.packet_loss, alertFlow.jitter);
    }

    public static AlertFlowConditions StringToAlertFlowConditions(String alertFlowString) {
        String[] parts = alertFlowString.split(",");
        if (parts.length < 5) return null;

        AlertFlowConditions alertFlow = new AlertFlowConditions();
        alertFlow.cpu_usage = Integer.parseInt(parts[0]);
        alertFlow.ram_usage = Integer.parseInt(parts[1]);
        alertFlow.interface_stats = Integer.parseInt(parts[2]);
        alertFlow.packet_loss = Integer.parseInt(parts[3]);
        alertFlow.jitter = Integer.parseInt(parts[4]);

        return alertFlow;
    }
}

