package models;

import com.google.common.eventbus.Subscribe;
import configuration.Configuration;
import database.DBService;
import execute.Executor;
import models.enums.AlgorithmType;
import models.enums.ParticipantType;

import java.util.Date;

public class Participant {
    private final String name;
    private final ParticipantType type;

    public Participant(String name, String type){
        this.name = name;
        this.type = type.equals("intruder") ? ParticipantType.INTRUDER : ParticipantType.NORMAL;
    }

    @Subscribe
    public void receiveMessage(BusMessage message){
        if (type.equals(ParticipantType.INTRUDER)) {
            receiveMessageIntruder(message);
        } else {
            receiveMessageParticipant(message);
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.toString();
    }

    public void receiveMessageIntruder(BusMessage message) {
        Executor utils = new Executor();

        String timestamp = String.valueOf(new Date().getTime()/1000);
        DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), "unknown", timestamp));
        try{
            String cracked = message.getAlgorithm().equals(AlgorithmType.RSA) ? utils.crackRSA(message.getMessage(), message.getKeyfile()) : utils.crackShift(message.getMessage());
            Configuration.instance.textAreaLogger.info(String.format("cracked message: %s", cracked));
        } catch (Exception e){
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\"", message.getMessage()));
        }

    }

    public void receiveMessageParticipant(BusMessage message) {
        if (message.getSender().getName().equals(this.name)) return;

        Executor utils = new Executor();

        try {
            String decrypted = utils.decrypt(message.getMessage(), message.getAlgorithm(), message.getKeyfile());
            String timestamp = String.valueOf(new Date().getTime()/1000);
            DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), decrypted, timestamp));
            Configuration.instance.textAreaLogger.info(String.format("%s received new message", this.name));
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info("Decryption timed out!");
        }

    }
}
