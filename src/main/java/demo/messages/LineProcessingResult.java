package demo.messages;

public class LineProcessingResult {

    private String ipAddress;

    public LineProcessingResult(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }
}
