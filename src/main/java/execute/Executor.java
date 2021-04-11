package execute;

import Keygen.Creator;
import Keygen.Keyfile;
import com.google.gson.Gson;
import execute.Tasks.SieveKeyTask;
import models.enums.AlgorithmType;
import configuration.Configuration;
import database.DBService;
import execute.Tasks.CrackRSATask;
import execute.Tasks.CrackShiftTask;
import models.BusMessage;
import models.Channel;
import models.Message;
import models.Participant;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class Executor {

    public Executor(){}

    public String encrypt(String message, AlgorithmType algorithmType, String keyfile){
        File keyfileFile = new File(Configuration.instance.keyFiles + keyfile);
        if (!keyfileFile.exists()){
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + " not existing");
            return null;
        }
        Configuration.instance.loggingHandler.newLogfile(algorithmType.toString(), "encrypt");
        String jarName = (algorithmType.equals(AlgorithmType.RSA) ? "rsa" : "shift") + ".jar";
        String className = algorithmType.equals(AlgorithmType.RSA) ? "RSA" : "Shift";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = JarLoader.getPort(Configuration.instance.jarPath + jarName, className);
            var method = port.getClass().getDeclaredMethod("encrypt", File.class, String.class, Logger.class);
            var answer = method.invoke(port, keyfileFile, message, Configuration.instance.loggingHandler.getLogger());
            if (answer == null){
                Configuration.instance.textAreaLogger.info("Problems encrypting message, keyfile might be invalid");
                return null;
            }
            return answer.toString();
        } catch (IOException e) {
            Configuration.instance.textAreaLogger.info("Invalid Keyfile Provided");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String message, AlgorithmType algorithmType, String keyfile){
        File keyfileFile = new File(Configuration.instance.keyFiles  + keyfile);
        if (!keyfileFile.exists()){
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + " not existing");
            return null;
        }
        Configuration.instance.loggingHandler.newLogfile(algorithmType.toString(), "decrypt");
        String jarName = (algorithmType.equals(AlgorithmType.RSA) ? "rsa" : "shift") + ".jar";
        String className = algorithmType.equals(AlgorithmType.RSA) ? "RSA" : "Shift";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = JarLoader.getPort(Configuration.instance.jarPath + jarName, className);
            var method = port.getClass().getDeclaredMethod("decrypt", File.class, String.class, Logger.class);
            var answer = method.invoke(port, keyfileFile, message, Configuration.instance.loggingHandler.getLogger());
            if (answer == null){
                Configuration.instance.textAreaLogger.info("Problems decrypting message, keyfile might be invalid");
                return null;
            }
            return answer.toString();
        } catch (IOException e) {
            Configuration.instance.textAreaLogger.info("Invalid Keyfile Provided");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String crackRSA(String message, String keyfile){
        String cracked;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new CrackRSATask(keyfile, message));
        try {
            cracked = future.get(30, TimeUnit.SECONDS);
            return cracked;
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\" failed", message));
        }
        return null;
    }

    public String crackShift(String message){
        String cracked;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new CrackShiftTask(message));
        try {
            cracked = future.get(30, TimeUnit.SECONDS);
            return cracked;
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\" failed", message));
        }
        return null;
    }

    public void registerParticipant(String name, String type){
        if (DBService.instance.participantExists(name)){
            Configuration.instance.textAreaLogger.info(String.format("participant %s already exists, using existing postbox_%s", name, name));
        }

        Participant participant = new Participant(name, type);
        DBService.instance.insertParticipant(participant);
    }

    public  void createChannel(String channelName, String part1name, String part2name){
        if (DBService.instance.channelExists(channelName)){
            Configuration.instance.textAreaLogger.info(String.format("channel %s already exists", channelName));
            return;
        }

        if (DBService.instance.getChannel(part1name, part2name) != null){
            Configuration.instance.textAreaLogger.info(String.format("communication channel between %s and %s already exists", part1name, part2name));
            return;
        }

        if (part1name.equals(part2name)){
            Configuration.instance.textAreaLogger.info(String.format("%s and %s identical - cannot create channel on itself", part1name, part2name));
            return;
        }

        var part1 = DBService.instance.getOneParticipant(part1name);
        var part2 = DBService.instance.getOneParticipant(part2name);

        Channel channel = new Channel(channelName, part1, part2);

        DBService.instance.insertChannel(channel);
    }

    public void showChannel(){
        var channelList = DBService.instance.getChannels();

        StringBuilder returnString = new StringBuilder();

        for (var channel : channelList) {
            Configuration.instance.textAreaLogger.info(String.format("%s | %s and %s", channel.getName(), channel.getParticipantA().getName(), channel.getParticipantB().getName()));
        }

        if(channelList.isEmpty()){
            Configuration.instance.textAreaLogger.info("channel list is empty!");
        }
    }

    public void dropChannel(String channelName){
        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("unknown channel %s", channelName));
            return;
        }

        if (!DBService.instance.removeChannel(channelName)){
            Configuration.instance.textAreaLogger.info("Something went wrong");
        }
    }

    public void intrudeChannelCommand(String channelName, String participant){
        var intruder = DBService.instance.getOneParticipant(participant);

        if (intruder == null){
            Configuration.instance.textAreaLogger.info(String.format("intruder %s could not be found", participant));
            return;
        }

        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("channel %s could not be found", channelName));
            return;
        }

        channel.intrude(intruder);
    }

    public void sendMessage(String message, String sender, String recipient, AlgorithmType algorithmType, String keyfile){
        var channel = DBService.instance.getChannel(sender, recipient);
        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("no valid channel from %s to %s", sender, recipient));
            return;
        }

        var senderPart = DBService.instance.getOneParticipant(sender);
        var recieverPart = DBService.instance.getOneParticipant(recipient);

        Date now = new Date();
        long timestampLong = now.getTime()/1000;
        String timestamp = String.valueOf(timestampLong);

        String encrypted = encrypt(message, algorithmType, keyfile);

        Message dbMessage = new Message(senderPart, recieverPart, algorithmType.toString().toLowerCase(Locale.ROOT), keyfile, timestamp, message, encrypted);
        channel.send(new BusMessage(encrypted, senderPart, recieverPart, algorithmType, keyfile));

        DBService.instance.insertMessage(dbMessage);
    }

    public void sieveKey(String keyfile){
        BigInteger sieved = null;
        File keyfileFile = new File(Configuration.instance.keyFiles + keyfile);

        if(!keyfileFile.exists()) {
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + "not existing");
            return;
        }

        Keyfile keyfileObj;

        try {
            keyfileObj = parseKey(keyfileFile);
        } catch (IOException e) {
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + "could not be parsed");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<BigInteger> future = executor.submit(new SieveKeyTask(keyfileObj.getN()));
        try {
            sieved = future.get(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info(String.format("unable to factorize %d in 60 seconds", keyfileObj.getN()));
        }
        if (sieved != null) {
            var split = keyfile.split("\\.");
            String joined = String.join("",Arrays.copyOf(split, split.length-1));
            Creator.createKeyfile(sieved, keyfileObj.getN().divide(sieved), joined+"_private.json");
        }
    }

    private Keyfile parseKey(File keyfile) throws IOException {
        Gson gson = new Gson();
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(keyfile.toPath());
        } catch (Exception e) { }
        Keyfile parsed = gson.fromJson(reader, Keyfile.class);

        if (parsed.getE() == null || parsed.getN() == null) {
            throw new IOException();
        }
        return parsed;
    }


}
