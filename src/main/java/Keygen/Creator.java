package Keygen;

import com.google.gson.Gson;
import configuration.Configuration;

import java.io.*;
import java.math.BigInteger;

public class Creator {
    public static void createKeyfile(BigInteger p, BigInteger q, String filename){
        var keygen = new Keygen(p, q);
        Gson gson = new Gson();

        Keyfile keyfile = new Keyfile(keygen.getD(), keygen.getN());
        String key = gson.toJson(keyfile);

        try {
            writeToFile(filename, key);
            Configuration.instance.textAreaLogger.info(String.format("factorize of %d in %d and %d successful. Private-Key file %s stored in %s", p.multiply(q), p, q, filename, Configuration.instance.keyFiles));
        } catch (IOException e) {
            Configuration.instance.textAreaLogger.info("Could not create new Keyfile");
        }
    }

    private static void writeToFile(String filename, String content) throws IOException {
        String keyfilePath = Configuration.instance.keyFiles;
        File file = new File(keyfilePath + filename);
        if (file.exists()) file.delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
        writer.write(content);
        writer.close();
    }
}
