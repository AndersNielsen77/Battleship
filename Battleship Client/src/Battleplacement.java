import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Battleplacement extends JFrame implements ActionListener, Runnable {
    private static final long serialVersionUID = 1L;
    public JButton[][] fields;
    public JPanel mainmenu;
    public Plade plade;
    public JPanel contentnorth, contentsouth, contentcenter, contentwest, contenteast;
    public JButton submit, clear, quit, rotate, returnmenu;
    public JButton carrier, battleship, cruiser, submarine, destroyer;
    public int kc = 0, kb = 0, ks = 0, kt = 0, kh = 0;
    public int rotation = 0;
    public Battleships Carrier;

    public Battleplacement() {
        getContentPane().setLayout(new BorderLayout());

        //Title
        contentnorth = new JPanel();
        JLabel startTitle = new JLabel("<html><font size='10' color=white>Ships placement</font></html>", SwingConstants.CENTER);
        contentnorth.setBackground(new Color(59, 89, 182));
        JLabel spaceL = new JLabel("                                                 ");
        JLabel spaceR = new JLabel("                                           ");
        rotate = new JButton("Rotate (Vertical)");
        rotate.addActionListener(this);

        contentnorth.add(spaceL);
        contentnorth.add(startTitle);
        contentnorth.add(spaceR);
        contentnorth.add(rotate);

        getContentPane().add(contentnorth, BorderLayout.NORTH);

        //Ships names - WEST
        contentwest = new JPanel();
        contentwest.setLayout(new BoxLayout(contentwest, BoxLayout.Y_AXIS));

        carrier = new JButton("    Carrier    ");
        battleship = new JButton(" Battleship ");
        cruiser = new JButton("   Cruiser    ");
        submarine = new JButton("Submarine ");
        destroyer = new JButton(" Destroyer ");

        carrier.setBackground(Color.GREEN);
        battleship.setBackground(Color.GREEN);
        cruiser.setBackground(Color.GREEN);
        submarine.setBackground(Color.GREEN);
        destroyer.setBackground(Color.GREEN);

        contentwest.add(carrier);
        contentwest.add(battleship);
        contentwest.add(cruiser);
        contentwest.add(submarine);
        contentwest.add(destroyer);

        carrier.addActionListener(this);
        battleship.addActionListener(this);
        cruiser.addActionListener(this);
        submarine.addActionListener(this);
        destroyer.addActionListener(this);

        getContentPane().add(contentwest, BorderLayout.WEST);

        //Plate
        plade = new Plade();
        contentsouth = new JPanel();
        contentsouth.setLayout(new BoxLayout(contentsouth, BoxLayout.PAGE_AXIS));

        //fields
        fields = new JButton[10][10];

        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                fields[j][i] = new JButton();
                fields[j][i].addActionListener(this);
                fields[j][i].setForeground(Color.WHITE);
                fields[j][i].setBackground(new Color(59, 89, 182));
                fields[j][i].setPreferredSize(new Dimension(10, 10));
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
        //Knapper
        mainmenu = new JPanel();
        returnmenu = new JButton("Return to menu");
        clear = new JButton("Clear");
        submit = new JButton("Submit");
        quit = new JButton("Quit");

        returnmenu.addActionListener(this);
        clear.addActionListener(this);
        submit.addActionListener(this);
        quit.addActionListener(this);

        mainmenu.add(quit);
        mainmenu.add(returnmenu);
        mainmenu.add(clear);
        mainmenu.add(submit);

        getContentPane().add(contentsouth, BorderLayout.SOUTH);
        contentsouth.add(mainmenu);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            Battlefight fight = null;
            fight = new Battlefight();

            Thread t2 = new Thread(fight);
            t2.start();
            setVisible(false);
        }
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (e.getSource() == fields[j][i]) {
                    if (kc == 0) {
                        Carrier = new Battleships(5);
                        if (plade.placeShip(j, i, Carrier, rotation)) {
                            System.out.println(Plade.getGrid(j, i));
                            kc++;
                            carrier.setBackground(Color.RED);
                            System.out.println(j + "" + i);
                        }
                    } else if (kb == 0) {
                        Battleships Battleship = new Battleships(4);
                        if (plade.placeShip(j, i, Battleship, rotation)) {
                            kb++;
                            battleship.setBackground(Color.RED);
                            System.out.println(j + "" + i);
                        }

                    } else if (ks == 0) {
                        Battleships Cruiser = new Battleships(3);
                        if (plade.placeShip(j, i, Cruiser, rotation)) {
                            ks++;
                            System.out.println(j + "" + i);
                            cruiser.setBackground(Color.RED);
                        }
                    } else if (kt == 0) {
                        Battleships Submarine = new Battleships(2);
                        if (plade.placeShip(j, i, Submarine, rotation)) {
                            kt++;
                            System.out.println(j + "" + i);
                            submarine.setBackground(Color.RED);
                        }
                    } else if (kh == 0) {
                        Battleships Destroyer = new Battleships(1);
                        if (plade.placeShip(j, i, Destroyer, rotation)) {
                            kh++;
                            System.out.println(j + "" + i);
                            destroyer.setBackground(Color.RED);
                        }
                    }
                }
            }
        }
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (Plade.getGrid(j, i) == 5) {
                    fields[j][i].setBackground(Color.BLACK);
                } else if (Plade.getGrid(j, i) == 4) {
                    fields[j][i].setBackground(Color.YELLOW);
                } else if (Plade.getGrid(j, i) == 3) {
                    fields[j][i].setBackground(Color.GREEN);
                } else if (Plade.getGrid(j, i) == 2) {
                    fields[j][i].setBackground(Color.ORANGE);
                } else if (Plade.getGrid(j,i) == 1) {
                    fields[j][i].setBackground(Color.MAGENTA);
                }
            }
        }
        if (e.getSource() == returnmenu) {
            Game game = new Game();
            Thread t = new Thread(game);
            t.start();
            setVisible(false);
        } else if (e.getSource() == clear) {
            kc = 0;
            ks = 0;
            kb = 0;
            kt = 0;
            kh = 0;
            for (int j = 0; j < 10; j++) {
                for (int i = 0; i < 10; i++) {
                    fields[j][i].setBackground(new Color(59, 89, 182));
                }
            }
            plade.resetGrid();
            carrier.setBackground(Color.GREEN);
            battleship.setBackground(Color.GREEN);
            cruiser.setBackground(Color.GREEN);
            submarine.setBackground(Color.GREEN);
            destroyer.setBackground(Color.GREEN);
        } else if (e.getSource() == rotate) {
            if (rotation == 0) {
                rotate.setText("Rotate (Horizontal)");
                rotate.setBackground(Color.LIGHT_GRAY);
                rotation++;
            } else if (rotation == 1) {
                rotate.setText("Rotate (Vertical)");
                rotate.setBackground(Color.LIGHT_GRAY);
                rotation--;
            }
        } else if (e.getSource() == quit) {
            System.exit(0);
        }
    }

    @Override
    public void run() {
        new Battleplacement();
        Battleplacement gui = new Battleplacement();
        gui.setTitle("Battleship: by Mikkel, Mads & Anders");
        gui.setSize(800, 500);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - gui.getWidth()) / 1000);
        int y = (int) ((dimension.getHeight() - gui.getHeight()) / 2);
        gui.setLocation(x, y);
    }
}
