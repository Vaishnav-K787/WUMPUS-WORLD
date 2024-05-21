package src;
import java.awt.Point;
import java.util.*;

public class WumpusMap {

    public static final int NUM_ROWS = 10;
    public static final int NUM_COLUMNS = 10;
    public static final int NUM_PITS = 10;
    private WumpusSquare[][] grid;
    private int ladderC,ladderR;
    public WumpusMap(){
        createMap();
    }
    public void createMap(){
        grid = new WumpusSquare[NUM_ROWS][NUM_COLUMNS];
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLUMNS; ++j){
                grid[i][j] = new WumpusSquare();
            }
        }
        ArrayList<Point> locations = new ArrayList<Point>();
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLUMNS; ++j){
                locations.add(new Point(i,j));
            }
        }
        Collections.shuffle(locations);
        Point p;
        for(int i = 0; i < NUM_PITS; ++i){
            p = locations.get(i);
            grid[p.x][p.y].setPit(true);
        }
        p = locations.get(NUM_PITS);
        grid[p.x][p.y].setLadder(true);
        ladderR = p.x;
        ladderC = p.y;
        locations = fixLocations(locations);
        Random rand = new Random();
        int index = rand.nextInt(locations.size());
        p = locations.get(index);
        grid[p.x][p.y].setWumpus(true);
        index = rand.nextInt(locations.size());
        p = locations.get(index);
        grid[p.x][p.y].setGold(true);
        fixSolvable();
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLUMNS; ++j){
                if(i > 0 && grid[i-1][j].getPit()) grid[i][j].setBreeze(true);
                if(i + 1 < NUM_ROWS && grid[i+1][j].getPit()) grid[i][j].setBreeze(true);
                if(j > 0 && grid[i][j-1].getPit()) grid[i][j].setBreeze(true);
                if(j + 1 < NUM_COLUMNS && grid[i][j+1].getPit()) grid[i][j].setBreeze(true);

                if(i > 0 && grid[i-1][j].getWumpus()) grid[i][j].setStench(true);
                if(i + 1 < NUM_ROWS && grid[i+1][j].getWumpus()) grid[i][j].setStench(true);
                if(j > 0 && grid[i][j-1].getWumpus()) grid[i][j].setStench(true);
                if(j + 1 < NUM_COLUMNS && grid[i][j+1].getWumpus()) grid[i][j].setStench(true);

                if(i > 0 && grid[i-1][j].getDeadWumpus()) grid[i][j].setStench(true);
                if(i + 1 < NUM_ROWS && grid[i+1][j].getDeadWumpus()) grid[i][j].setStench(true);
                if(j > 0 && grid[i][j-1].getDeadWumpus()) grid[i][j].setStench(true);
                if(j + 1 < NUM_COLUMNS && grid[i][j+1].getDeadWumpus()) grid[i][j].setStench(true);
            }
        }
    }
    public ArrayList<Point> fixLocations(ArrayList<Point> locations){
        ArrayList<Point> FIX_LOCATIONS = new ArrayList<Point>();
        for(int i = NUM_PITS + 1; i < locations.size(); ++i){
            FIX_LOCATIONS.add((Point) locations.get(i).clone());
        }
        return FIX_LOCATIONS;
    }
    private void fixSolvable(){
        // runs a 0/1 BFS to find the path with the least pits, then makes the whole thing solvable
        ArrayDeque<Point> points = new ArrayDeque<Point>();
        int[][] distance = new int[NUM_ROWS][NUM_COLUMNS];
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLUMNS; ++j){
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        Point p = new Point(ladderR,ladderC);
        points.addFirst(p);
        distance[p.x][p.y] = 0;
        int temp;
        while(!points.isEmpty()){
            p = points.removeFirst();
            temp = distance[p.x][p.y];
            if(++p.x < NUM_ROWS){
                if(grid[p.x][p.y].getPit()){
                    if(distance[p.x][p.y] > temp + 1){
                        distance[p.x][p.y] = temp + 1;
                        points.addLast((Point) p.clone());
                    }
                }
                else{
                    if(distance[p.x][p.y] > temp){
                        distance[p.x][p.y] = temp;
                        points.addFirst((Point) p.clone());
                    }
                }
            }
            p.x -= 2;
            if(p.x >= 0){
                if(grid[p.x][p.y].getPit()){
                    if(distance[p.x][p.y] > temp + 1){
                        distance[p.x][p.y] = temp + 1;
                        points.addLast((Point) p.clone());
                    }
                }
                else{
                    if(distance[p.x][p.y] > temp){
                        distance[p.x][p.y] = temp;
                        points.addFirst((Point) p.clone());
                    }
                }
            }
            ++p.x;
            if(++p.y < NUM_COLUMNS){
                if(grid[p.x][p.y].getPit()){
                    if(distance[p.x][p.y] > temp + 1){
                        distance[p.x][p.y] = temp + 1;
                        points.addLast((Point) p.clone());
                    }
                }
                else{
                    if(distance[p.x][p.y] > temp){
                        distance[p.x][p.y] = temp;
                        points.addFirst((Point) p.clone());
                    }
                }
            }
            p.y -= 2;
            if(p.y >= 0){
                if(grid[p.x][p.y].getPit()){
                    if(distance[p.x][p.y] > temp + 1){
                        distance[p.x][p.y] = temp + 1;
                        points.addLast((Point) p.clone());
                    }
                }
                else{
                    if(distance[p.x][p.y] > temp){
                        distance[p.x][p.y] = temp;
                        points.addFirst((Point) p.clone());
                    }
                }
            }
        }
        int I = -1; int J = -1;
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLUMNS; ++j){
                if(grid[i][j].getGold()){
                    if(distance[i][j] == 0){
                        return;
                    }
                    I = i;
                    J = j;
                }
            }
        }
        boolean[][] visited = new boolean[NUM_ROWS][NUM_COLUMNS];
        int X = I; int Y = J;
        while(distance[I][J] != 0){
            visited[I][J] = true;
            temp = distance[I][J];
            if(I > 0 && distance[I-1][J] <= temp && !visited[I-1][J]){
                temp = distance[I-1][J];
            }
            if(I + 1 < NUM_ROWS && distance[I + 1][J] <= temp && !visited[I + 1][J]){
                temp = distance[I+1][J];
            }
            if(J > 0 && distance[I][J-1] <= temp && !visited[I][J-1]){
                temp = distance[I][J-1];
            }
            if(J + 1 < NUM_COLUMNS && distance[I][J+1] <= temp && !visited[I][J+1]){
                temp = distance[I][J+1];
            }

            if(I > 0 && distance[I-1][J] == temp && !visited[I-1][J]){
                --I;
            }
            else if(I + 1 < NUM_ROWS && distance[I + 1][J] == temp && !visited[I + 1][J]){
                ++I;
            }
            else if(J > 0 && distance[I][J-1] == temp && !visited[I][J-1]){
                --J;
            }
            else if(J + 1 < NUM_COLUMNS && distance[I][J+1] == temp && !visited[I][J+1]){
                ++J;
            }
        }
        if(I > 0 && grid[I-1][J].getPit() && visited[I-1][J] && distance[I-1][J] == 1){
            --I;
        }
        else if(I + 1 < NUM_ROWS && grid[I+1][J].getPit() && visited[I+1][J] && distance[I+1][J] == 1){
            ++I;
        }
        else if(J > 0 && grid[I][J-1].getPit() && visited[I][J-1] && distance[I][J-1] == 1){
            --J;
        }
        else{
            ++J;
        }
        WumpusSquare end = grid[X][Y].makeCopy();
        grid[X][Y] = grid[I][J].makeCopy();
        grid[I][J] = end;
    }
    public int getLadderC(){
        return ladderC;
    }
    public int getLadderR(){
        return ladderR;
    }
    public WumpusSquare getSquare(int col, int row){
        return grid[row][col];
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < NUM_ROWS; ++i){
            for(int j = 0; j < NUM_COLUMNS; ++j){
                sb.append(grid[i][j].toString());
            }
            sb.append('\n');
        }
        return sb.toString();
    }
    public static void main(String[] args) {
        WumpusMap wm = new WumpusMap();
    }
}
