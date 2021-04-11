package execute.Tasks;

import configuration.Configuration;
import execute.JarLoader;
import execute.JarVerifier;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.Callable;

public class SieveKeyTask implements Callable<BigInteger> {
    BigInteger n;

    public SieveKeyTask(BigInteger n) {
        this.n = n;
    }

    @Override
    public BigInteger call() throws Exception {

        String jarName = "quadratic_sieve.jar";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = JarLoader.getPort(Configuration.instance.jarPath + jarName, "Sieve");
            var method = port.getClass().getDeclaredMethod("sieve", BigInteger.class);
            var answer = method.invoke(port, n);
            if (answer == null){
                Configuration.instance.textAreaLogger.info("Problems Sieving " + n);
                return null;
            }
            return new BigInteger(answer.toString());
        } catch (IOException e){
            Configuration.instance.textAreaLogger.info("Keyfile could not be Processed");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
