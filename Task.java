import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Task {
    @SerializedName("task_id")
    String task_id;
    int frequency;
    List<Device> devices;

    @Override
    public String toString() {
        return "Task{" +
                "task_id='" + task_id + '\'' +
                ", frequency=" + frequency +
                ", devices=" + devices +
                '}';
    }
}

