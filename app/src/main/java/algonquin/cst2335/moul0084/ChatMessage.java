package algonquin.cst2335.moul0084;

public class ChatMessage {
    private String message;
    private String timeSent;
    private boolean isSentButton;


    public ChatMessage(String m, String t, boolean sent)
    {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }

    public String getMessage() {
        return message;
    }


}
