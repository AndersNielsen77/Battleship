import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class PlayerHandler implements Runnable {

    private String playerId;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private Socket socket;
    private boolean userOnline;
    private boolean notIngame;
    private ServerRun p;

    public PlayerHandler(Socket socket, String playerId, DataOutputStream out, DataInputStream in, boolean notIngame, ServerRun parent) {
        this.socket = socket;
        this.playerId = playerId;
        this.out = out;
        this.in = in;
        this.userOnline = true;
        this.notIngame = notIngame;
        this.p = parent;
    }

    private void connectingPlayers() throws InterruptedException{
        while (notIngame) {
            System.out.println("in loop");
                    for (PlayerHandler ma : p.playerH) {
                        System.out.println(ma);
                        if (!this.playerId.equals(ma.playerId) && ma.notIngame) {
                            try {
                                ma.out.writeUTF(playerId+"#id2");
                                ma.out.flush();
                                this.notIngame = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Thread.sleep(500);
                    }
                }
    }
    @Override
    public void run() {
        String modtaget = null;

        if (notIngame){
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connectingPlayers();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t1.start();
        }

        while (true) {

                try {
                    modtaget = in.readUTF();
                    System.out.println("Modtaget besked: "+modtaget);

                    if (modtaget.equals("Logout")) {
                        this.userOnline = false;
                        this.socket.close();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert modtaget != null;

                StringTokenizer stToken = new StringTokenizer(modtaget, "$");
                String tokenSend = stToken.nextToken();
                String tokenmodtag = stToken.nextToken();

                try {
                    for (PlayerHandler mc : p.playerH) {
                        if (mc.playerId.equals(tokenmodtag) && mc.userOnline) {
                            mc.out.writeUTF(tokenSend);
                            mc.out.flush();
                            System.out.println("Besked sendt: "+tokenSend);
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*try {
                System.out.println("Closing DataStreams");
                this.in.close();
                this.out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }