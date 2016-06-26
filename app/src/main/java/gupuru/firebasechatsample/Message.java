package gupuru.firebasechatsample;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Message {

    public String device = "Android";
    public String data;

    public Message() {
    }

    public Message(String data) {
        this.data = data;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("device", device);
        result.put("data", data);

        return result;
    }

}