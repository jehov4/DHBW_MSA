package parser;

import configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    public static void evaluateCommand(String command){
        String[] patterns = new String[]{
                "(encrypt message) \"(.+)\" using (rsa|shift) and keyfile ([A-Za-z0-9\\. ]*)",
                "(decrypt message) \"(.+)\" using (rsa|shift) and keyfile ([A-Za-z0-9\\. ]*)",
                "(crack encrypted message) \"(.+)\" using (shift)",
                "(crack encrypted message) \"(.+)\" using (rsa) and keyfile ([A-Za-z0-9\\. ]*)",
                "(register participant) ([A-Za-z0-9_ ]*) with type (normal|intruder)",
                "(create channel) ([A-Za-z0-9_ ]*) from ([A-Za-z0-9_ ]*) to ([A-Za-z0-9_ ]*)",
                "(show channel)",
                "(drop channel) ([A-Za-z0-9_ ]*)",
                "(intrude channel) ([A-Za-z0-9_ ]*) by ([A-Za-z0-9_ ]*)",
                "(send message) \"(.+)\" from ([A-Za-z0-9_ ]*) to ([A-Za-z0-9_ ]*) using ([A-Za-z0-9_ ]*) and keyfile ([A-Za-z0-9_\\. ]*)",
                "(crack n using rsa and keyfile) ([A-Za-z0-9_\\. ]*)"
        };

        String[] extracted = null;
        for (String pattern : patterns) {
            extracted = getRegexGroups(pattern, command);
            if (extracted != null) break;
        }
        if (extracted == null) {
            Configuration.instance.textAreaLogger.info(String.format("Command \"%s\" could not be processed", command));
            return;
        }

        new Executor(extracted);
    }

    private static String[] getRegexGroups(String regex, String command) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(command);
        if (matcher.find()) {
            List<String> groups = new ArrayList<>();
            for (int i = 1; i < matcher.groupCount() + 1; i++) {
                groups.add(matcher.group(i));
            }
            return groups.toArray(new String[0]);
        }
        return null;
    }
}
