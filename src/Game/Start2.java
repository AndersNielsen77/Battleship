package Game;

public class Start2 {
    public static void main(String[] args){
        Game game = new Game();
        Thread t = new Thread(game);
        t.start();
    }
}
