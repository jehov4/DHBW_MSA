package execute;

import configuration.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JarVerifier {
    public static boolean verifie(String jarFile) {
        boolean isComponentAccepted = false;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(Configuration.instance.jdkPath + "jarsigner", "-verify", Configuration.instance.jarPath + jarFile);
            Process process = processBuilder.start();
            process.waitFor();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("verified")) {
                    isComponentAccepted = true;
                }
            }

        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info("a problem occurred while verifying " + jarFile);
        }
        return isComponentAccepted;
    }
}
