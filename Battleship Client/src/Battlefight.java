/*
1. Forminsk chat i Ship placement samt skibe tekst.
2. Fjerne to ekstra ships placement når man har supmit.
3. Skriv i Battlefield om man har ramt.
4. Auto scroll både battlefield og ships placement. (Semi DONE - Resize mangler)
5. Ny din tur knap (Skrifter farve). (DONE)
6. Skibe bliver rød når de er døde i venstre side.
7. Skal sætte alle skipe i ships placement.
8. Tilføje sidste skip nr 4. (Submarine) size 3. (Semi Done - Mangler at lave det 3 langt.)
9. Quit og Return to menu på Ship placement fix. (DONE)
10. Quit og Return to menu lukker alle threds og JFrame's
11. Overveje preformance, inf loop, måske lave nogle bake/timeout på serveren for undgå overbelastning.
12. Automatisk IP, eller indtastning af ip. Efter x antal tid automatisk connect til "localhost".
13. Lave battlefight.java battleplacement knapper utrykkelige. (Done)
14. Besked når man har ødelagt et helt skib.

Skips:
No.	Class of ship	Size
1	Carrier	    5
2	Battleship	4
3	Cruiser	    3
4	Submarine	3
5	Destroyer	2
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class Battlefight extends JFrame implements ActionListener, Runnable {
    private static final long serialVersionUID = 1L;
    public JFrame defend;
    public JButton[][] fields, fields2;
    public JPanel mainmenu;
    public JPanel contentnorth, contentcenter, contentsouth, contentwest;
    public JPanel mainmenu2;
    public JPanel contentnorth2, contentcenter2, contentsouth2, contentwest2;
    public static JButton quit, returnmenu, btnSend, connect, turn;
    public static JButton quit2, returnmenu2;
    public JButton carrier, battleship, cruiser, submarine, destroyer;
    public JTextArea beskeder, send;
    public JTextArea beskeder2;
    private int kcCheck = 5, kbCheck = 4, ksCheck = 3, khCheck = 2;
    //Server
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    private String playerid, enemyPlayer;
    private boolean playerJoined = false;
    //På en enhed
    //private final static String host = "localhost";
    //Ude for samme enhed
    private final static String host = "localhost";
    private final static int portNumber = 9991;
    private boolean joinedServer = false;
    private boolean yourturn = true;

    public Battlefight() {
        //Attack frame (Højre side)
        getContentPane().setLayout(new BorderLayout());

        //Title
        contentnorth = new JPanel();
        JLabel startTitle2 = new JLabel("<html><font size='10' color=white>Battlefield</font></html>", SwingConstants.CENTER);
        contentnorth.add(startTitle2);
        contentnorth.setBackground(new Color(59, 89, 182));

        getContentPane().add(contentnorth, BorderLayout.NORTH);

        //fields
        fields = new JButton[10][10];
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                fields[j][i] = new JButton();
                fields[j][i].addActionListener(this);
                fields[j][i].setForeground(Color.WHITE);
                fields[j][i].setBackground(new Color(59, 89, 182));
            }
        }
        contentcenter = new JPanel();
        contentcenter.setLayout(new GridLayout(10, 10));
        getContentPane().add(contentcenter, BorderLayout.CENTER);
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                contentcenter.add(fields[j][i]);
            }
        }

        //Chat mm. - WEST
        contentwest = new JPanel();
        contentwest.setLayout(new BorderLayout());

        beskeder = new JTextArea();
        beskeder.setEditable(false);
        beskeder.setLineWrap(true);
        // .setPreferredSize laver problemer med scroll.
        //beskeder.setPreferredSize(new Dimension(150, 300));
        //beskeder.setFont(new Font("Serif", Font.PLAIN, 15));

        //Scroll
        contentwest.add(new JScrollPane(beskeder), BorderLayout.CENTER);

        send = new JTextArea();
        send.setPreferredSize(new Dimension(60, 30));

        btnSend = new JButton("Send");
        btnSend.addActionListener(this);
        //contentwest.add(beskeder);

        contentwest.setSize(400, 400);

        getContentPane().add(contentwest, BorderLayout.WEST);

        //Knapper
        mainmenu = new JPanel();
        returnmenu = new JButton("Return to menu");
        quit = new JButton("Quit");
        connect = new JButton("Connect");
        turn = new JButton("Your turn");

        connect.setBackground(Color.RED);
        turn.setBackground(Color.RED);

        //Kan godt være vi skal lave hit button om til en Label eller andet tekst.
        returnmenu.addActionListener(this);
        quit.addActionListener(this);
        connect.addActionListener(this);
        //turn.addActionListener(this); - Ingen grund til at kunne trykkes.

        mainmenu.add(quit);
        mainmenu.add(returnmenu);
        mainmenu.add(connect);
        mainmenu.add(turn);

        contentsouth = new JPanel();

        //Chat måske skift til mainmenu istedet for contentsou
        contentsouth.add(send);
        contentsouth.add(btnSend);

        contentsouth.setLayout(new BoxLayout(contentsouth, BoxLayout.PAGE_AXIS));
        getContentPane().add(contentsouth, BorderLayout.SOUTH);
        contentsouth.add(mainmenu);

        //Defend frame (Venstre side)
        defend = new JFrame();
        defend.setTitle("Battleship: by Mikkel, Mads & Anders");

        defend.getContentPane().setLayout(new BorderLayout());

        //Title
        contentnorth2 = new JPanel();
        startTitle2 = new JLabel("<html><font size='10' color=white>Ships</font></html>", SwingConstants.CENTER);
        contentnorth2.setBackground(new Color(59, 89, 182));

        contentnorth2.add(startTitle2);

        defend.getContentPane().add(contentnorth2, BorderLayout.NORTH);

        //Ships names - WEST
        contentwest2 = new JPanel();
        contentwest2.setLayout(new BoxLayout(contentwest2, BoxLayout.Y_AXIS));

        beskeder2 = new JTextArea();
        //beskeder2.setPreferredSize(new Dimension(100, 250));
        beskeder2.setEditable(false);
        beskeder2.setLineWrap(true);
        //Scroll
        contentwest2.add(new JScrollPane(beskeder2), BorderLayout.CENTER);

        carrier = new JButton("  Carrier  ");
        battleship = new JButton("Battleship");
        cruiser = new JButton("  Cruiser  ");
        submarine = new JButton("Submarine");
        destroyer = new JButton("Destroyer");

        carrier.setBackground(Color.GREEN);
        battleship.setBackground(Color.GREEN);
        cruiser.setBackground(Color.GREEN);
        submarine.setBackground(Color.GREEN);
        destroyer.setBackground(Color.GREEN);

        carrier.setEnabled(false);
        battleship.setEnabled(false);
        cruiser.setEnabled(false);
        submarine.setEnabled(false);
        destroyer.setEnabled(false);

        contentwest2.add(carrier);
        contentwest2.add(battleship);
        contentwest2.add(cruiser);
        contentwest2.add(submarine);
        contentwest2.add(destroyer);

        //Laver problemer med spacing
        contentwest2.add(beskeder2);

        //Mulig løsning - Virker ikke
        contentwest2.add(Box.createHorizontalGlue());
        contentwest2.add(Box.createVerticalGlue());

        /*
        carrier.addActionListener(this);
        battleship.addActionListener(this);
        cruiser.addActionListener(this);
        submarine.addActionListener(this);
        destroyer.addActionListener(this);
        */

        defend.getContentPane().add(contentwest2, BorderLayout.WEST);

        //Plate
        contentsouth2 = new JPanel();
        contentsouth2.setLayout(new BoxLayout(contentsouth2, BoxLayout.PAGE_AXIS));

        //Fields
        fields2 = new JButton[10][10];
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                fields2[j][i] = new JButton();
                fields2[j][i].addActionListener(this);
                fields2[j][i].setForeground(Color.WHITE);
                fields2[j][i].setBackground(new Color(59, 89, 182));
                fields2[j][i].setPreferredSize(new Dimension(10, 10));
                fields2[j][i].setEnabled(false);
            }
        }
        contentcenter2 = new JPanel();
        contentcenter2.setLayout(new GridLayout(10, 10));
        defend.getContentPane().add(contentcenter2, BorderLayout.CENTER);
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                contentcenter2.add(fields2[j][i]);
            }
        }
        redDraw();

        //Knapper
        mainmenu2 = new JPanel();
        returnmenu2 = new JButton("Return to menu");
        quit2 = new JButton("Quit");

        returnmenu2.addActionListener(this);
        quit2.addActionListener(this);

        mainmenu2.add(quit2);
        mainmenu2.add(returnmenu2);

        defend.getContentPane().add(contentsouth2, BorderLayout.SOUTH);
        contentsouth2.add(mainmenu2);
        defend.setVisible(true);
        defend.setSize(800, 500);
        defend.setResizable(false);
        defend.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Placering
        Dimension dimension2 = Toolkit.getDefaultToolkit().getScreenSize();
        int a = (int) ((dimension2.getWidth() - defend.getWidth()) / 1000);
        int b = (int) ((dimension2.getHeight() - defend.getHeight()) / 2);
        defend.setLocation(a, b);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == connect) {
            if (joinedServer == false) {
                try {
                    socket = new Socket(host, portNumber);
                    connect.setBackground(Color.YELLOW);
                    connect.setText("Connecting...");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    connect.setBackground(Color.RED);
                }
                System.out.println("Creating socket to '" + host + "' on port " + portNumber);
                try {
                    in = new DataInputStream(socket.getInputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    out = new DataOutputStream(socket.getOutputStream());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                joinedServer = true;
            }

            Thread ModtagMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String msg = in.readUTF();
                            StringTokenizer stToken = new StringTokenizer(msg, "#");
                            String content = stToken.nextToken();
                            String id = stToken.nextToken();

                            System.out.print("id is; " + id);
                            System.out.println(" : Content is; " + content);
                            if (id.equals("id1")) {
                                beskeder.append("You are Player: " + content + "\n");
                                playerid = "Player" + content;
                                System.out.println("In #id1");
                            } else if (id.equals("id2")) {
                                if (!playerJoined) {
                                    enemyPlayer = content;
                                    System.out.println("In #id2");
                                    connect.setBackground(Color.GREEN);
                                    connect.setText("Connected");
                                    turn.setBackground(Color.GREEN);
                                    out.writeUTF(playerid + "#id2$" + enemyPlayer);
                                    out.flush();
                                    playerJoined = true;
                                }
                            } else if (id.equals("id3")) {
                                StringTokenizer hitToken = new StringTokenizer(content, ",");
                                String stringJ = hitToken.nextToken();
                                String stringI = hitToken.nextToken();

                                int j = Integer.parseInt(stringJ);
                                int i = Integer.parseInt(stringI);
                                ownHit(j, i);

                                out.writeUTF(j + "," + i + "," + hitReq(j, i) + "#id4$" + enemyPlayer);
                                out.flush();
                                yourturn = true;
                                turn.setBackground(Color.GREEN);
                                beskeder.append("Det er din tur!" + "\n");

                            } else if (id.equals("id4")) {
                                StringTokenizer token = new StringTokenizer(content, ",");
                                String stringJ = token.nextToken();
                                String stringI = token.nextToken();
                                String stringboolean = token.nextToken();

                                int i = Integer.parseInt(stringI);
                                int j = Integer.parseInt(stringJ);
                                boolean bo = Boolean.parseBoolean(stringboolean);

                                enemyHit(bo, j, i);

                            } else {
                                beskeder.append(enemyPlayer + ": " + content + "\n");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            Thread SendMsg = new Thread(new Runnable() {
                @Override
                public void run() {
                    btnSend.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            String msg = send.getText() + "#id0$" + enemyPlayer;
                            System.out.println(enemyPlayer);
                            try {
                                out.writeUTF(msg);
                                System.out.println("This is out msg: " + msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            SendMsg.start();
            ModtagMsg.start();
        }

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (e.getSource() == fields[j][i]) {
                    System.out.println("pknap");
                    try {
                        if (yourturn) {
                            out.writeUTF(j + "," + i + "#id3$" + enemyPlayer);
                            yourturn = false;
                            turn.setBackground(Color.RED);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        if (e.getSource() == quit) {
            System.exit(0);
        } else if (e.getSource() == returnmenu) {
            Game game = new Game();
            Thread t = new Thread(game);
            t.start();
            setVisible(false);
        } else if (e.getSource() == quit2) {
            System.exit(0);
        } else if (e.getSource() == returnmenu2) {
            Game game = new Game();
            Thread t = new Thread(game);
            t.start();
            setVisible(false);
        }
        Plade.gridCheckWin();
        if (Plade.win == 100) {
            beskeder.append("Du har sunket alle dine fjenders skibe og vundet spillet" + "\n");

        } else if (e.getSource() == btnSend) {
            beskeder.append(playerid + ": " + send.getText() + "\n");
            send.setText("");
        }
    }

    @Override
    public void run() {
        new Battlefight();
        Battlefight gui = null;
        gui = new Battlefight();

        gui.setTitle("Battleship: by Mikkel, Mads & Anders");
        gui.setSize(800, 500);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        //Sætter vinduet i højre side
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - gui.getWidth()));
        int y = (int) ((dimension.getHeight() - gui.getHeight()) / 2);
        gui.setLocation(x, y);
    }

    public boolean hitReq(int j, int i) {
        System.out.println(Plade.getGrid(j, i));
        if (Plade.getGrid(j, i) == 5) {
            return true;
        } else if (Plade.getGrid(j, i) == 4) {
            return true;
        } else if (Plade.getGrid(j, i) == 3) {
            return true;
        } else if (Plade.getGrid(j, i) == 2) {
            return true;
        }  else if(Plade.getGrid(j,i) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void enemyHit(boolean hit, int j, int i) {
        if (hit) {
            fields[j][i].setBackground(Color.RED);
            fields[j][i].setEnabled(false);
            // beskeder.append("Du har ramt et skib." + "\n");
        } else {
            // beskeder.append("Du ramte ikke noget skib" + "\n");
            fields[j][i].setText("X");
            fields[j][i].setEnabled(false);
        }
    }

    public void ownHit(int j, int i) {
        if (Plade.getGrid(j, i) != 0) {
            fields2[j][i].setBackground(Color.RED);
            beskeder2.append("Du er blevet ramt." + "\n");
        } else {
            fields2[j][i].setText("X");
        }
    }

    public void redDraw() {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (Plade.getGrid(j, i) == 5) {
                    fields2[j][i].setBackground(Color.BLACK);
                } else if (Plade.getGrid(j, i) == 4) {
                    fields2[j][i].setBackground(Color.YELLOW);
                } else if (Plade.getGrid(j, i) == 3) {
                    fields2[j][i].setBackground(Color.GREEN);
                } else if (Plade.getGrid(j, i) == 2) {
                    fields2[j][i].setBackground(Color.ORANGE);
                } else if (Plade.getGrid(j,i) == 1) {
                    fields2[j][i].setBackground(Color.MAGENTA);
                }
            }
        }
    }
}