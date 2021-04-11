package configuration;

import Logging.LoggingHandler;

import java.util.HashMap;
import java.util.Map;

public enum Configuration {
    instance;

    public final String jarPath = "jars/";

    // Paths
    public final String keyFiles = "keyfiles/";

    //JDK Path
    public final String jdkPath = "/home/liam/.local/java/jdk-15.0.2/bin/";
    // Log-Directory
    public final String logDir =  "log";

    //Text Field Logger
    public final java.util.logging.Logger textAreaLogger = java.util.logging.Logger.getLogger("textarea");

    // Intruded Channels
    public final Map<String, String> intrudedChannels = new HashMap<>();

    //File Logger
    public final LoggingHandler loggingHandler = new LoggingHandler();


}
