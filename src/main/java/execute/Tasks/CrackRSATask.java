package execute.Tasks;

import configuration.Configuration;
import execute.JarLoader;
import execute.JarVerifier;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

public class CrackRSATask implements Callable<String> {
    String keyfile;
    String message;

    public CrackRSATask(String keyfile, String message) {
        this.keyfile = keyfile;
        this.message = message;
    }

    @Override
    public String call() throws Exception {
        File keyfileFile = new File(Configuration.instance.keyFiles + keyfile);
        if (!keyfileFile.exists()){
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + " not existing");
            return null;
        }
        String jarName = "rsa_cracker.jar";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = JarLoader.getPort(Configuration.instance.jarPath + jarName, "RSACracker");
            var method = port.getClass().getDeclaredMethod("decrypt", String.class, File.class);
            var answer = method.invoke(port, message, keyfileFile);
            if (answer == null){
                Configuration.instance.textAreaLogger.info("Problems Cracking Message probably invalid keyfile");
                return null;
            }
            return answer.toString();
        } catch (IOException e){
            Configuration.instance.textAreaLogger.info("Keyfile could not be Processed");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
