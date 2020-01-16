import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JFrame implements ActionListener, Runnable {
    private static final long serialVersionUID = 1L;
    public Game gui;
    public JPanel mainmenu;
    public JPanel contentcenter;
    public static JButton play, quit;

    public Game() {
        getContentPane().setLayout(new BorderLayout());

        //Startmenu
        //Title
        contentcenter = new JPanel();
        JLabel startTitle = new JLabel("<html><font size='40' color=blue>Battleship</font></html>", SwingConstants.CENTER);
        contentcenter.add(startTitle);
        contentcenter.setBackground(Color.BLACK);
        contentcenter.setLayout(new GridLayout(2, 0));

        //Knapper
        mainmenu = new JPanel();
        mainmenu.setLayout(new GridLayout(2, 0));

        //Play
        play = new JButton("Play");
        play.setBackground(new Color(0, 182, 0));
        play.setForeground(Color.WHITE);
        play.setFocusPainted(false);
        play.setFont(new Font("Tahoma", Font.BOLD, 20));
        mainmenu.add(play);
        play.addActionListener(this);

        //Quit
        quit = new JButton("Quit");
        quit.setBackground(new Color(182, 0, 0));
        quit.setForeground(Color.WHITE);
        quit.setFocusPainted(false);
        quit.setFont(new Font("Tahoma", Font.BOLD, 20));
        quit.addActionListener(this);
        mainmenu.add(quit);

        getContentPane().add(contentcenter, BorderLayout.CENTER);
        contentcenter.add(mainmenu);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            //play.setText("Connecting to server");
            Battleplacement battle = new Battleplacement();
            Thread t = new Thread(battle);
            t.start();
            setVisible(false);

            //Bruger ikke mere, virker ikke da man ikke kan overføre connection thread til et andet frame.
            //play.setText("Waiting for players...");
            //play.setBackground(new Color(255, 103, 0));
        } else if (e.getSource() == quit) {
            System.exit(0);
        }
    }

    public void run() {
        new Game();
        gui = new Game();
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
