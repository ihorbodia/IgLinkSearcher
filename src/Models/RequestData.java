package Models;

public class RequestData {
    public String requestURL;
    public int attemptsCount;
    public int requestDelay;

    public RequestData(String requestURL, int attemptsCount, int requestDelay) {
        this.requestURL = requestURL;
        this.attemptsCount = attemptsCount;
        this.requestDelay = requestDelay;
    }
}