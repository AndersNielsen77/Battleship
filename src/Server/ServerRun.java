package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerRun implements Runnable {

    private DataOutputStream out = null;
    private DataInputStream in = null;
    private Socket socket = null;
    private ServerSocket connect = null;
    private int playerId = 0;
    private PlayerInfo player;
    public Vector<PlayerHandler> playerH = null;
    private boolean notIngame = true;

    public ServerRun() {
        playerH = new Vector<>();
    }


    @Override
    public void run() {
        try {
            connect = new ServerSocket(9991);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            socket = null;
            try {
                socket = connect.accept();
                playerId ++;
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                player = new PlayerInfo(socket,playerId,out,in,socket.getInetAddress());
                System.out.println("P" +
                        "" +
                        "layer "+player.getPlayerId()+" Connecting | "+socket);
            } catch (IOException e) {
                System.out.println("Server Stopped");
            }

            PlayerHandler newPlayerH = new PlayerHandler(socket,"Player"+playerId,out,in,notIngame, this);
            playerH.add(newPlayerH);

            Thread playerHThread = new Thread(newPlayerH);
            playerHThread.start();

            SpilThread spil = new SpilThread(socket,playerId,out,in);
            new Thread(spil).start();

        }
    }
}
