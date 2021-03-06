package models;


import com.google.common.eventbus.EventBus;
import configuration.Configuration;
import database.DBService;
import models.enums.ParticipantType;

public class Channel {

    private final String name;
    private final Participant participantA;
    private final Participant participantB;
    private final EventBus eventBus = new EventBus();

    public Channel(String name, Participant participantA, Participant participantB){
        this.name = name;
        this.participantA = participantA;
        this.participantB = participantB;
        eventBus.register(participantA);
        eventBus.register(participantB);
        if (Configuration.instance.intrudedChannels.containsKey(this.name)){
            eventBus.register(DBService.instance.getOneParticipant(Configuration.instance.intrudedChannels.get(this.name)));
        }
    }

    public String getName(){
        return this.name;
    }

    public Participant getParticipantA(){
        return this.participantA;
    }

    public Participant getParticipantB(){
        return this.participantB;
    }

    public void send(BusMessage message){
        eventBus.post(message);
    }

    public void intrude(Participant intruder){
        if (intruder.getType().equals(ParticipantType.INTRUDER)){
            eventBus.register(intruder);
            Configuration.instance.intrudedChannels.put(this.name, intruder.getName());
            Configuration.instance.textAreaLogger.info(String.format("Channel %s got intruded by %s", name, intruder.getName()));
        } else {
            Configuration.instance.textAreaLogger.info("Intruder should be of type intruder");
        }

    }

}
