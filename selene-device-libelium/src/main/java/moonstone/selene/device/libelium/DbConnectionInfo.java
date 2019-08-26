package moonstone.selene.device.libelium;

import java.io.Serializable;
import java.util.Map;

public class DbConnectionInfo implements Serializable {
    private static final long serialVersionUID = 323958803029608926L;

    private String name;
    private String host;
    private int port;
    private String user;
    private String pass;
    private String parser_table;

    public DbConnectionInfo populateFrom(Map<String, String> map) {
        name = map.getOrDefault("name", name);
        host = map.getOrDefault("host", host);
        port = Integer.parseInt(map.getOrDefault("port", Integer.toString(port)));
        user = map.getOrDefault("user", user);
        pass = map.getOrDefault("pass", pass);
        parser_table = map.getOrDefault("parser_table", parser_table);
        return this;
    }

    public String buildConnectionString(String connectionString) {
        return String.format(connectionString, getHost(), "" + getPort(), getName(), getUser(), getPass());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getParser_table() {
        return parser_table;
    }

    public void setParser_table(String parser_table) {
        this.parser_table = parser_table;
    }
}
