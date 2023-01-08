package packets;

public class InformationPacket extends Packet {
    public InformationPacket(Command command, String reason) {
        super(command, reason);
    }
}
