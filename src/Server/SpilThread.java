package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SpilThread implements Runnable {

    protected Socket clientSocket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Socket socket = null;
    private SpilThread[] bruger = new SpilThread[2];
    private int playerid;
    private String modstander;
    boolean isOnline = true;


    public SpilThread(Socket socket,int playerid,DataOutputStream out, DataInputStream in){
        this.out = out;
        this.in = in;
        this.playerid = playerid;
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.writeUTF(playerid+"#id1");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* while (true){
            //System.out.println(Thread.currentThread().getId() + " is Running");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (isOnline = false){
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
}
