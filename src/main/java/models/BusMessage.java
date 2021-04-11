package models;

import models.enums.AlgorithmType;

public class BusMessage {

    private String message;
    private Participant sender;
    private Participant recipient;
    private AlgorithmType algorithm;
    private String keyfile;

    public BusMessage(String message, Participant sender, Participant recipient, AlgorithmType algorithm, String keyfile){
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    public String getMessage() {
        return message;
    }

    public Participant getSender() {
        return sender;
    }

    public Participant getRecipient() {
        return recipient;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }
}
