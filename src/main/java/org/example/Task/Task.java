package org.example.Task;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    @Override
    public String toString() {
        return "Task.Task{" +
                "task_id='" + task_id + '\'' +
                ", frequency=" + frequency +
                ", devices=" + devices +
                '}';
    }

    public void jsonReader(String filepath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filepath);
        Task[] tasks = objectMapper.readValue(jsonFile,Task[].class);
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
}

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
}

class AlertFlowConditions {
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
}

