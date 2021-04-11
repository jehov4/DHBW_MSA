package Logging;

import configuration.Configuration;

import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public class LoggingHandler {
    Handler handler;
    Logger logger;
    Boolean enabled = false;

    public LoggingHandler(){
        logger = java.util.logging.Logger.getLogger("file");
    }

    public void newLogfile(String algorithmType, String direction){
        if (!enabled) return;
        if (handler != null)
            logger.removeHandler(handler);
        try {
            handler = new FileHandler(String.format("log/%s_%s_%d.txt", direction, algorithmType, new Date().getTime()/1000));
            handler.setFormatter(new SimpleFormatter());
            handler.setLevel(Level.FINE);
            logger.addHandler(handler);
        } catch (IOException e){
            Configuration.instance.textAreaLogger.info("Problems creating logfile");
        }
    }

    public void switchLogging(){
        if (enabled) {
            enabled = false;
            if (handler != null){
                logger.removeHandler(handler);
            }
            Configuration.instance.textAreaLogger.info("Logging disabled");
        } else {
            enabled = true;
            Configuration.instance.textAreaLogger.info("Logging enabled");
        }
    }

    public Logger getLogger(){
        return logger;
    }
}
