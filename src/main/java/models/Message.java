package models;

public class Message {
    private final Participant participantSender;
    private final Participant participantReceiver;
    private final String algorithm;
    private final String keyfile;
    private final String timestamp;
    private final String plainMessage;
    private final String encryptedMessage;

    public Message(Participant participantSender, Participant participantReceiver, String algorithm, String keyfile, String timestamp, String plainMessage, String encryptedMessage){
        this.participantSender = participantSender;
        this.participantReceiver = participantReceiver;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
        this.timestamp = timestamp;
        this.plainMessage = plainMessage;
        this.encryptedMessage = encryptedMessage;
    }

    public Participant getParticipantSender(){
        return this.participantSender;
    }

    public Participant getParticipantReceiver(){
        return this.participantReceiver;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public String getKeyfile() {
        return this.keyfile;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getPlainMessage() {
        return this.plainMessage;
    }

    public String getEncryptedMessage() {
        return this.encryptedMessage;
    }
}
