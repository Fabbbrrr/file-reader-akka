package demo.messages;

import java.util.Map;

public class FileProcessedMessage {

    private Map<String, Long> data;

    public FileProcessedMessage(Map<String, Long> data) {
        this.data = data;
    }

    public Map<String, Long> getData() {
        return data;
    }
}
