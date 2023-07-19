package algonquin.cst2335.moul0084;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;

    protected boolean isSentButton;

    @ColumnInfo(name="sendOrReceive")
    protected int sendOrReceive;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
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
