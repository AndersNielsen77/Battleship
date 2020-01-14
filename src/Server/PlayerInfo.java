package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class PlayerInfo {
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private InetAddress inetaddress;
    private int playerId;

    public PlayerInfo(Socket socket, int playerId, DataOutputStream out, DataInputStream in,InetAddress inetaddress){
        this.socket = socket;
        this.playerId = playerId;
        this.out = out;
        this.in = in;
        this.inetaddress = inetaddress;
    }

    public Socket getSocket(){
        return socket;
    }

    public int getPlayerId(){
        return playerId;
    }

    public DataOutputStream getOut(){
        return out;
    }

    public DataInputStream getIn(){
        return in;
    }

    public InetAddress getInetaddress(){
        return inetaddress;
    }

}
