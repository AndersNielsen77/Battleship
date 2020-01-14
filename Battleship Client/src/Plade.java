public class Plade {
    protected static int win;
    protected int r;
    protected int c;
    protected static int k;
    protected static int[][] grid;

    public Plade() {
        r = 10;
        c = 10;
        grid = new int[r][c];
    }

    public boolean placeShip(int r, int c, Battleships ship, int rotation) {
        if(rotation == 0) {
            if(verify(r,c, ship, rotation)) {
                return false;
            }
            else {
                for(int x = 0; x < ship.getId(); x++) {
                    grid[r + x][c] = ship.getId();
                }
                return true;
            }
        }
        else {
            if(verify(r, c, ship, rotation)) {
                return false;
            }
            else {
                for(int y = 0; y < ship.getId(); y++) {
                    grid[r][c+y] = ship.getId();
                }
                return true;
            }
        }
    }

    public static int getGrid(int r, int c) {
        k = grid[r][c];
        return k;
    }
    public void resetGrid() {
        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                grid[i][j] = 0;
            }
        }
    }
    public static void gridRemove(int r, int c) {
        grid[r][c] = 0;
    }
    public static void gridCheckWin() {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                if (grid[j][i] == 0) {
                    win++;
                }
            }
        }
        if(win != 100) {
            win = 0;
        }
    }
    public boolean verify(int r, int c, Battleships ship, int rotation) {
        if (rotation == 0) {
            for (int x = 0; x < ship.getId(); x++) {
                if (grid[r + x][c] != 0) {
                    return true;
                }
            }
            return r + ship.getId() - 1 > 9;
        } else {
            for (int y = 0; y < ship.getId(); y++) {
                if (grid[r][c + y] != 0) {
                    return true;
                }
            }
                return c + ship.getId() - 1 > 9;

        }
    }
}
