import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SkibServer extends JFrame {

    private Thread serverThread;
    private boolean serverRunning = false;


    public static void main(String[] args) {

        SkibServer gui = new SkibServer();
        gui.setTitle("Battleship server: by Mikkel, Mads & Anders");
        gui.setDefaultCloseOperation(gui.EXIT_ON_CLOSE);
        gui.setVisible(true);
    }

    public SkibServer() {
        //JFrame menu = new JFrame("Server");
        this.setPreferredSize(new Dimension(400, 250));
        this.setBackground(Color.darkGray);
        this.getContentPane().setBackground(Color.darkGray);
        //menu.setDefaultCloseOperation(menu.EXIT_ON_CLOSE);
        // menu.setTitle("Server");

        final JTextField tf = new JTextField("Server not running");
        tf.setBounds(115, 50, 150, 20);
        tf.setBackground(Color.RED);
        tf.setSelectionColor(Color.blue);
        tf.setEditable(false);
        tf.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        tf.setBorder(BorderFactory.createBevelBorder(1, Color.gray, Color.DARK_GRAY));
        tf.setForeground(Color.lightGray);

        final JTextField commands = new JTextField();
        commands.setBounds(10, 140, 370, 60);
        commands.setEditable(false);
        commands.setBackground(Color.gray);

        JButton connect = new JButton("Battleserver.Start Server");
        connect.setBounds(130, 100, 120, 30);

        this.add(connect);
        this.add(tf);
        this.add(commands);
        this.setSize(400, 250);
        this.setLayout(null);
        this.pack();
        this.setVisible(true);

        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (serverRunning == false) {
                    serverRunning = true;
                    serverThread.start();
                    tf.setText("Server is Running");
                    tf.setBackground(Color.GREEN);
                    connect.setText("Stop Server");
                    tf.setForeground(Color.black);
                }
            }
        });
        ServerRun server = new ServerRun();
        serverThread = new Thread(server);
    }
}
