package com.arrow.selene.engine.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;

import com.arrow.acn.client.model.SshTunnelModel;
import com.arrow.acs.AcsUtils;
import com.arrow.acs.JsonUtils;
import com.arrow.acs.Loggable;
import com.arrow.selene.SeleneException;
import com.arrow.selene.SysUtils;
import com.arrow.selene.model.ProcessStatusModel;

public class SshTunnelService extends Loggable {
    private final static String TEMP_FILE = "/tmp/selene-tunnel";

    private static class SingletonHolder {
        static final SshTunnelService SINGLETON = new SshTunnelService();
    }

    public static SshTunnelService getInstance() {
        return SingletonHolder.SINGLETON;
    }

    private SshTunnelService() {
    }

    public void openTunnel(Map<String, String> parameters) {
        String method = "createTunnel";

        SshTunnelModel model = JsonUtils.fromJson(parameters.get("payload"), SshTunnelModel.class);
        Validate.notEmpty(model.getRemoteHost(), "remoteHost is missing");
        Validate.notEmpty(model.getRemoteUser(), "remoteUser is missing");
        Validate.notEmpty(model.getRemotePassword(), "remotePassword is missing");
        Validate.isTrue(model.getRemotePort() > 0, "remotePort is missing");
        Validate.notEmpty(model.getLocalUser(), "localUser is missing");
        Validate.notEmpty(model.getLocalPassword(), "localPassword is missing");
        Validate.isTrue(model.getLocalPort() > 0, "localPort is missing");

        String processIds = StringUtils.join(getRunningProcessIds(model.getRemoteHost()), ", ");
        logInfo(method, "processIds: %s", processIds);
        if (StringUtils.isNotEmpty(processIds)) {
            String error = String.format("ERROR: SSH tunnel is already open, processIds: %s", processIds);
            logError(method, error);
            throw new SeleneException(error);
        } else {
            logInfo(method, "creating local user: %s", model.getLocalUser());
            createLocalUser(model.getLocalUser(), model.getLocalPassword());

            Properties props = new Properties();
            props.setProperty("remoteHost", model.getRemoteHost());
            props.setProperty("localUser", model.getLocalUser());
            saveTempFile(props);

            logInfo(method, "connecting to %s ...", model.getRemoteHost());
            connect(model);
            try {
                logInfo(method, "waiting for a few seconds ...");
                Thread.sleep(10000);
            } catch (Exception e) {
            }
            processIds = StringUtils.join(getRunningProcessIds(model.getRemoteHost()), ", ");
            logInfo(method, "processIds: %s", processIds);
        }
    }

    public void closeTunnel(Map<String, String> parameters) {
        String method = "closeTunnel";

        File file = new File(TEMP_FILE);
        if (!file.exists()) {
            logWarn(method, "WARNING - file not found: %s", TEMP_FILE);
        } else {
            try {
                Properties props = new Properties();
                props.load(new FileReader(file));
                String username = props.getProperty("localUser");
                if (StringUtils.isNotEmpty(username)) {
                    logInfo(method, "deleting local user: %s", username);
                    deleteLocalUser(username);
                } else {
                    logError(method, "localUser property not found in file: %s", TEMP_FILE);
                }
                String remoteHost = props.getProperty("remoteHost");
                if (StringUtils.isNotEmpty(remoteHost)) {
                    Set<Integer> processIds = getRunningProcessIds(remoteHost);
                    if (processIds.size() > 0) {
                        for (Integer processId : processIds) {
                            logInfo(method, "killing process: %d", processId);
                            executeCommand("kill " + processId, false);
                        }
                        logInfo(method, "deleting file: %s", TEMP_FILE);
                        file.delete();
                    } else {
                        String error = "SSH tunnel is not open";
                        logError(method, error);
                        throw new SeleneException(error);
                    }
                } else {
                    logError(method, "remoteHost property not found in file: %s", TEMP_FILE);
                }
            } catch (Exception e) {
                logError(method, "error closing tunnel", e);
            }
        }
    }

    private void connect(SshTunnelModel model) {
        String method = "connect";

        if (!checkSshpass(true)) {
            throw new SeleneException("ERROR: sshpass tool is not installed");
        }
        new Thread(() -> {
            try {
                String command = String.format(
                        "sshpass -p %s /usr/bin/ssh -o StrictHostKeyChecking=no -R%d:localhost:%d %s -l %s",
                        model.getRemotePassword(), model.getRemotePort(), model.getLocalPort(), model.getRemoteHost(),
                        model.getRemoteUser());

                logInfo(method, "running command: %s", command);
                Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
                logInfo(method, "waiting for process to terminate ...");
                String output = AcsUtils.streamToString(process.getInputStream(), StandardCharsets.UTF_8);
                String error = AcsUtils.streamToString(process.getErrorStream(), StandardCharsets.UTF_8);
                int code = process.waitFor();
                logInfo(method, "WARNING: tunnel is closed, code: %d\noutput: %s\nerror: %s", code, output, error);
            } catch (Throwable e) {
                logError(method, e);
            }
        }).start();
    }

    private Set<Integer> getRunningProcessIds(String host) {
        String method = "getRunningProcessId";
        ProcessStatusModel status = executeCommand(
                String.format("ps -ef  |  grep -i %s  | grep -v grep | awk '{print $2}'", host), false);
        if (status.getExitCode() != 0) {
            return Collections.emptySet();
        } else {
            logInfo(method, "output: %s", status.getOutput());
            Set<Integer> result = new HashSet<>();
            for (String token : status.getOutput().split("\\n")) {
                int processId = NumberUtils.toInt(token.trim(), 0);
                if (processId > 0) {
                    result.add(processId);
                }
            }
            return result;
        }
    }

    private boolean checkSshpass(boolean tryToInstall) {
        String method = "checkSshpass";
        ProcessStatusModel status = executeCommand("which sshpass", false);
        boolean result = status.getExitCode() == 0 && StringUtils.isNotEmpty(status.getOutput());
        if (!result && tryToInstall) {
            logWarn(method, "sshpass is not installed, attempting to download and install ...");
            executeCommand("apt-get install -y sshpass", false);
            result = checkSshpass(false);
        }
        return result;
    }

    private void createLocalUser(String username, String password) {
        String method = "createLocalUser";

        // step 0 - delete old user
        logInfo(method, "deleting existing user %s ...", username);
        deleteLocalUser(username);

        // step 1 - create new user
        logInfo(method, "creating user %s ...", username);
        // executeCommand(String.format("useradd -m -p $(echo \"%s\" | openssl
        // passwd -1 -stdin) %s", password, username));
        executeCommand(String.format("useradd -m %s", username), true);

        // step 2 - change password
        logInfo(method, "changing password ...", username);
        executeCommand(String.format("echo \"%s:%s\" | chpasswd", username, password), true);

        // step 3 grant sudo access
        logInfo(method, "granting sudo access ...");
        executeCommand(String.format("usermod -aG sudo %s", username), true);
    }

    private void deleteLocalUser(String username) {
        String method = "deleteLocalUser";
        try {
            logInfo(method, "deleting user: %s ...", username);
            executeCommand(String.format("userdel -r %s", username), false);
        } catch (Exception e) {
            logWarn(method, "unable to delete user - %s", e.getMessage());
        }
    }

    private ProcessStatusModel executeCommand(String command, boolean throwException) {
        String method = "executeCommand";
        logInfo(method, "command: %s", command);
        ProcessStatusModel status = SysUtils.executeCommandLine("/bin/sh", "-c", command);
        if (status.getExitCode() != 0) {
            logError(method, "command failed! exitCode: %d, error: %s, output: %s", status.getExitCode(),
                    status.getError(), status.getOutput());
            if (throwException)
                throw new SeleneException(status.getError());
        }
        logInfo(method, "output: %s", status.getOutput());
        return status;
    }

    private void saveTempFile(Properties props) {
        String method = "saveTempFile";
        try {
            File file = new File(TEMP_FILE);
            if (file.exists()) {
                logInfo(method, "deleting existing tmp file ...");
                file.delete();
            }
            props.store(new FileWriter(file), null);
        } catch (Exception e) {
            String error = "Unable to write to file: " + TEMP_FILE;
            logError(method, error, e);
            throw new SeleneException(error, e);
        }
    }
}
