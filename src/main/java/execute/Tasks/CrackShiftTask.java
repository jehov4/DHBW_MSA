package execute.Tasks;

import configuration.Configuration;
import execute.JarLoader;
import execute.JarVerifier;

import java.util.concurrent.Callable;

public class CrackShiftTask implements Callable<String> {
    String message;

    public CrackShiftTask(String message) {
        this.message = message;
    }

    @Override
    public String call() throws Exception {
        String jarName = "shift_cracker.jar";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = JarLoader.getPort(Configuration.instance.jarPath + jarName, "ShiftCracker");
            var method = port.getClass().getDeclaredMethod("decrypt", String.class);
            return method.invoke(port, message).toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
