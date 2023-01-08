package packets;

public class ErrorPacket extends Packet {

    public ErrorPacket(Command command, String reason) {
        super(command, reason);
    }
}
