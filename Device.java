import java.util.List;

public class Device {
    String device_id;
    DeviceMetrics device_metrics;
    private LinkMetrics link_metrics;

    @Override
    public String toString() {
        return "Device{" +
                "device_id='" + device_id + '\'' +
                ", device_metrics=" + device_metrics +
                ", link_metrics=" + link_metrics +
                '}';
    }
}

class DeviceMetrics {
    boolean cpu_usage;
    private boolean ram_usage;
    private List<String> interface_stats;

    @Override
    public String toString() {
        return "DeviceMetrics{" +
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

    @Override
    public String toString() {
        return "LinkMetrics{" +
                "bandwidth=" + bandwidth +
                ", jitter=" + jitter +
                ", packet_loss=" + packet_loss +
                ", latency=" + latency +
                ", alertflow_conditions=" + alertflow_conditions +
                '}';
    }
}

class Bandwidth {
    private String tool;
    private String role;
    private String server_address;
    private int duration;
    private String transport_type;
    private int frequency;

    @Override
    public String toString() {
        return "Bandwidth{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}

class Jitter {
    private String tool;
    private String role;
    private String server_address;
    private int duration;
    private String transport_type;
    private int frequency;

    @Override
    public String toString() {
        return "Jitter{" +
                "tool='" + tool + '\'' +
                ", role='" + role + '\'' +
                ", server_address='" + server_address + '\'' +
                ", duration=" + duration +
                ", transport_type='" + transport_type + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}

class PacketLoss {
    private String tool;
    private String role;
    private String server_address;
    private int duration;
    private String transport_type;
    private int frequency;

    @Override
    public String toString() {
        return "PacketLoss{" +
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

    @Override
    public String toString() {
        return "Latency{" +
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

    @Override
    public String toString() {
        return "AlertFlowConditions{" +
                "cpu_usage=" + cpu_usage +
                ", ram_usage=" + ram_usage +
                ", interface_stats=" + interface_stats +
                ", packet_loss=" + packet_loss +
                ", jitter=" + jitter +
                '}';
    }
}