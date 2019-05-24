package mod.upcraftlp.telegramintegration.telegramapi;

import com.google.gson.annotations.SerializedName;

public class UpdateObject {
    @SerializedName("update_id")
    private Integer updateId = 0;
    @SerializedName("message")
    private MessageObject message = null;
    @SerializedName("edited_message")
    private MessageObject editedMessage = null;

    public Integer getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Integer updateId) {
        this.updateId = updateId;
    }

    public MessageObject getMessage() {
        return message;
    }

    public void setMessage(MessageObject message) {
        this.message = message;
    }

    public MessageObject getEditedMessage() {
        return editedMessage;
    }

    public void setEditedMessage(MessageObject editedMessage) {
        this.editedMessage = editedMessage;
    }
}
