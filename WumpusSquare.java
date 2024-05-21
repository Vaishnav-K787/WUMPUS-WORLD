package src;

public class WumpusSquare {
    private boolean gold,ladder,pit,breeze,wumpus,deadWumpus,stench,visited;
    public WumpusSquare(){
        gold = false;
        ladder = false;
        pit = false;
        breeze = false;
        wumpus = false;
        deadWumpus = false;
        stench = false;
        visited = false;
    }
    public boolean getVisited(){
        return visited;
    }
    public boolean getGold(){
        return gold;
    }
    public boolean getLadder(){
        return ladder;
    }
    public boolean getPit(){
        return pit;
    }
    public boolean getBreeze(){
        return breeze;
    }
    public boolean getWumpus(){
        return wumpus;
    }
    public boolean getDeadWumpus(){
        return deadWumpus;
    }
    public boolean getStench(){
        return stench;
    }
    public void setVisited(boolean visited){
        this.visited = visited;
    }

    public void setGold(boolean gold){
        this.gold = gold;
    }
    public void setLadder(boolean ladder){
        this.ladder = ladder;
    }
    public void setPit(boolean pit){
        this.pit = pit;
    }
    public void setBreeze(boolean breeze){
        this.breeze = breeze;
    }
    public void setWumpus(boolean wumpus){
        this.wumpus = wumpus;
    }
    public void setDeadWumpus(boolean deadWumpus){
        this.deadWumpus = deadWumpus;
    }
    public void setStench(boolean stench){
        this.stench = stench;
    }
    public String toString(){
        if(wumpus && gold) return "@";
        if(deadWumpus && gold) return "!";
        if(wumpus) return "W";
        if(pit) return "P";
        if(deadWumpus) return "D";
        if(gold) return "G";
        if(ladder) return "L";
        return "*";
    }
    public WumpusSquare makeCopy(){
        WumpusSquare copy = new WumpusSquare();
        copy.gold = gold;
        copy.ladder = ladder;
        copy.pit = pit;
        copy.breeze = breeze;
        copy.wumpus = wumpus;
        copy.deadWumpus = deadWumpus;
        copy.stench = stench;
        copy.visited = visited;
        return copy;
    }
}
