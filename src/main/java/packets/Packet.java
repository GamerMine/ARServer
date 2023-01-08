package packets;

import java.io.Serializable;

public class Packet implements Serializable {

    Command command;
    String reason;

    public Packet(Command command, String reason) {
        this.command = command;
        this.reason = reason;
    }

    public Command getCommand() {
        return this.command;
    }

    public String getReason() {
        return this.reason;
    }
}
