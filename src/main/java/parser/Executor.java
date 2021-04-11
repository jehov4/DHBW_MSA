package parser;

import models.enums.AlgorithmType;
import configuration.Configuration;

public class Executor {
    
    execute.Executor executor = new execute.Executor();
    
    public Executor(String[] parsed){
        switch (parsed[0]) {
            case "encrypt message":
                this.encryptMessage(parsed);
                break;
            case "decrypt message":
                this.decryptMessage(parsed);
                break;
            case "crack encrypted message":
                this.crackMessage(parsed);
                break;
            case "register participant":
                this.registerParticipant(parsed);
                break;
            case "create channel":
                this.createChannel(parsed);
                break;
            case "show channel":
                this.showChannel(parsed);
                break;
            case "drop channel":
                this.dropChannel(parsed);
                break;
            case "intrude channel":
                this.intrudeChannel(parsed);
                break;
            case "send message":
                this.sendMessage(parsed);
                break;
            case "crack n using rsa and keyfile":
                this.crackN(parsed);
                break;
        }
    }
    
    public void encryptMessage(String[] parsed){
        String encrypted = executor.encrypt(parsed[1], parsed[2].equals("rsa") ? AlgorithmType.RSA : AlgorithmType.SHIFT, parsed[3]);
        if (encrypted != null) Configuration.instance.textAreaLogger.info(encrypted);
    }
    public void decryptMessage(String[] parsed){
        String decrypted = executor.decrypt(parsed[1], parsed[2].equals("rsa") ? AlgorithmType.RSA : AlgorithmType.SHIFT, parsed[3]);
        if (decrypted != null)Configuration.instance.textAreaLogger.info(decrypted);
    }
    public void crackMessage(String[] parsed){
        String cracked = null;
        if (parsed[2].equals("shift")){
            cracked = executor.crackShift(parsed[1]);
            if (cracked != null) Configuration.instance.textAreaLogger.info(cracked);
        } else {
            cracked = executor.crackRSA(parsed[1], parsed[3]);
            if (cracked != null) Configuration.instance.textAreaLogger.info(cracked);
        }
    }
    public void registerParticipant(String[] parsed){
        executor.registerParticipant(parsed[1], parsed[2]);
    }
    public void createChannel(String[] parsed){
        executor.createChannel(parsed[1], parsed[2], parsed[3]);
    }
    public void showChannel(String[] parsed){
        executor.showChannel();
    }
    public void dropChannel(String[] parsed){
        executor.dropChannel(parsed[1]);
    }
    public void intrudeChannel(String[] parsed){
        executor.intrudeChannelCommand(parsed[1], parsed[2]);
    }
    public void sendMessage(String[] parsed){
        executor.sendMessage(parsed[1], parsed[2], parsed[3], parsed[4].equals("rsa") ? AlgorithmType.RSA : AlgorithmType.SHIFT, parsed[5]);
    }
    public void crackN(String[] parsed){ executor.sieveKey(parsed[1]);}
}
