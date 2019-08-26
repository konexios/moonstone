package moonstone.selene;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.Validate;

import moonstone.acs.AcsUtils;
import moonstone.acs.Loggable;
import moonstone.selene.model.ProcessStatusModel;

public class SysUtils {
    private static Loggable LOGGER = new Loggable() {
    };

    public static ProcessStatusModel executeCommandLine(String... commands) {
        String method = "doExecuteCommand";
        Validate.notEmpty(commands, "commands are empty");
        Process process = null;
        try {
            if (LOGGER.isDebugEnabled()) {
                for (String command : commands) {
                    LOGGER.logDebug(method, "command: %s", command);
                }
            }
            process = Runtime.getRuntime().exec(commands);
            String output = AcsUtils.streamToString(process.getInputStream(), StandardCharsets.UTF_8);
            String error = AcsUtils.streamToString(process.getErrorStream(), StandardCharsets.UTF_8);
            int exit = process.waitFor();
            LOGGER.logDebug(method, "exit value: %d", exit);
            if (exit != 0) {
                LOGGER.logError(method, "output: %s", output);
                LOGGER.logError(method, "error: %s", error);
            }
            return new ProcessStatusModel().withExitCode(exit).withOutput(output).withError(error);
        } catch (Throwable t) {
            throw new SeleneException("unable to execute command line", t);
        } finally {
            if (process != null && process.isAlive()) {
                try {
                    process.destroyForcibly();
                } catch (Throwable e) {
                }
            }
        }
    }

    public static List<String> getMacAddresses() {
        String method = "getMacAddresses";
        List<String> result = new ArrayList<>();
        try {
            if (SystemUtils.IS_OS_WINDOWS) {
                LOGGER.logDebug(method, "IS_OS_WINDOWS");
                for (String line : AcsUtils.streamToLines(
                        new ByteArrayInputStream(executeCommandLine("getmac").getOutput().getBytes()),
                        StandardCharsets.UTF_8)) {
                    String[] tokens = line.trim().split(" ", -1);
                    String firstToken = tokens[0].trim();
                    String[] digits = firstToken.split("-", -1);
                    if (digits.length == 6) {
                        String macAddress = firstToken.replace("-", ":").toUpperCase();
                        LOGGER.logDebug(method, "found: %s", macAddress);
                        result.add(macAddress);
                    }
                }
            } else if (SystemUtils.IS_OS_UNIX) {
                LOGGER.logDebug(method, "IS_OS_UNIX");
                List<String> lines = AcsUtils.streamToLines(new ByteArrayInputStream(
                        executeCommandLine("/bin/sh", "-c", "/bin/cat /sys/class/net/*/address").getOutput()
                                .getBytes()),
                        StandardCharsets.UTF_8);
                Collections.reverse(lines);
                for (String line : lines) {
                    line = line.trim().replace("-", ":").toUpperCase();
                    if (line.length() == 17 && !StringUtils.equals(line, "00:00:00:00:00:00")) {
                        LOGGER.logDebug(method, "found: %s", line);
                        result.add(line);
                    }
                }
            }
        } catch (Throwable t) {
            LOGGER.logError(method, "error getting mac addresses", t);
        }
        return result;
    }
}
