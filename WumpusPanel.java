package src;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;


public class WumpusPanel extends JPanel implements KeyListener{
    public static final int PLAYING = 0;
    public static final int DEAD = 1;
    public static final int WON = 2;
    private int status;
    private WumpusPlayer player;
    private WumpusMap map;

    private BufferedImage floor, arrow, fog, gold, ladder, pit, breeze, wumpus, deadWumpus, stench, playerUp, playerDown, playerLeft, playerRight;
    private BufferedImage buffer;
    private boolean CHEATING;
    StringBuilder messages;
    public WumpusPanel(){
        setBounds(0,0,600,800);
        try
        {
            floor = ImageIO.read(new File("floor.gif"));
            arrow = ImageIO.read(new File("arrow.gif"));
            fog = ImageIO.read(new File("black.gif"));
            gold = ImageIO.read(new File("gold.gif"));
            ladder = ImageIO.read(new File("ladder.gif"));
            pit = ImageIO.read(new File("pit.gif"));
            breeze = ImageIO.read(new File("breeze.gif"));
            wumpus = ImageIO.read(new File("wumpus.gif"));
            deadWumpus = ImageIO.read(new File("deadWumpus.gif"));
            stench = ImageIO.read(new File("stench.gif"));
            playerUp = ImageIO.read(new File("playerUp.png"));
            playerDown = ImageIO.read(new File("playerDown.png"));
            playerLeft = ImageIO.read(new File("playerLeft.png"));
            playerRight = ImageIO.read(new File("playerRight.png"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            messages = new StringBuilder();
            addKeyListener(this);
            reset();
        }

    }
    public void reset(){
        CHEATING = false;
        status = PLAYING;
        map = new WumpusMap();
        player = new WumpusPlayer();
        player.setColPosition(map.getLadderC());
        player.setRowPosition(map.getLadderR());
        map.getSquare(map.getLadderC(),map.getLadderR()).setVisited(true);
        repaint();
    }
    public void setCHEATING(boolean CHEATING){
        this.CHEATING = CHEATING;
    }
    public boolean getCHEATING(){
        return CHEATING;
    }
    public void paint(Graphics g){
        g.setColor(Color.GRAY);
        g.fillRect(0,0,600,800);
        for(int i = 0; i < map.NUM_ROWS; ++i){
            for(int j = 0; j < map.NUM_COLUMNS; ++j){
                if(!map.getSquare(j,i).getVisited() && !CHEATING){
                    g.drawImage(fog, 50*(i+1), 50*(j+1), null);
                }
                else{
                    g.drawImage(floor,50*(i+1), 50*(j+1), null);
                    if(map.getSquare(j,i).getGold()){
                        g.drawImage(gold,50*(i+1), 50*(j+1), null);
                    }
                    if(map.getSquare(j,i).getWumpus()){
                        g.drawImage(wumpus,50*(i+1), 50*(j+1), null);
                    }
                    if(map.getSquare(j,i).getPit()){
                        g.drawImage(pit,50*(i+1), 50*(j+1), null);
                    }
                    if(map.getSquare(j,i).getBreeze()){
                        g.drawImage(breeze,50*(i+1), 50*(j+1), null);
                    }
                    if(map.getSquare(j,i).getDeadWumpus()){
                        g.drawImage(deadWumpus,50*(i+1), 50*(j+1), null);
                    }
                    if(map.getSquare(j,i).getLadder()){
                        g.drawImage(ladder,50*(i+1), 50*(j+1), null);
                    }
                    if(map.getSquare(j,i).getStench()){
                        g.drawImage(stench,50*(i+1), 50*(j+1), null);
                    }
                    if(player.getColPosition() == j && player.getRowPosition() == i){
                        if(player.getDirection() == player.NORTH){
                            g.drawImage(playerUp,50*(i+1), 50*(j+1), null);
                        }
                        if(player.getDirection() == player.SOUTH){
                            g.drawImage(playerDown,50*(i+1), 50*(j+1), null);
                        }
                        if(player.getDirection() == player.EAST){
                            g.drawImage(playerRight,50*(i+1), 50*(j+1), null);
                        }
                        if(player.getDirection() == player.WEST){
                            g.drawImage(playerLeft,50*(i+1), 50*(j+1), null);
                        }
                    }
                }
            }
        }
        g.setColor(Color.BLACK);
        g.fillRect(0,600,200,200);
        g.fillRect(210,600,590,200);
        g.setColor(Color.RED);
        g.drawString("Inventory",10,610);
        g.drawString("Messages", 220, 610);
        if(player.getArrow()){
            g.drawImage(arrow,20,700,null);
        }
        if(player.getGold()){
            g.drawImage(gold,40,700,null);
        }
        if(status == WON){
            messages.setLength(0);
            messages.append("You win (n for a new game).\n");
        }
        else if(status == DEAD){
            messages.setLength(0);
            if(map.getSquare(player.getColPosition(),player.getRowPosition()).getPit()){
                messages.append("You fell down a pit to your death.\n");
            }
            else{
                messages.append("You are eaten by the wumpus.\n");
            }
        }
        else{
            if(player.getColPosition() == map.getLadderC() && player.getRowPosition() == map.getLadderR()){
                messages.append("You bump into ladder.\n");
            }
            if(map.getSquare(player.getColPosition(),player.getRowPosition()).getStench()){
                messages.append("You smell a stench.\n");
            }
            if(map.getSquare(player.getColPosition(),player.getRowPosition()).getBreeze()){
                messages.append("You feel a breeze.\n");
            }
            if(map.getSquare(player.getColPosition(),player.getRowPosition()).getGold()){
                messages.append("You see a glimmer.\n");
            }
        }
        g.drawString(messages.toString(),230,700);
        messages.setLength(0);
    }
    @Override
    public void keyTyped(KeyEvent e){
        if(status == PLAYING){
            if(e.getKeyChar() == 'd'){
                if(player.getRowPosition() + 1 < map.NUM_ROWS){
                    player.setRowPosition(player.getRowPosition() + 1);
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getWumpus()){
                        status = DEAD;
                    }
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getPit()){
                        status = DEAD;
                    }
                    map.getSquare(player.getColPosition(),player.getRowPosition()).setVisited(true);
                    player.setDirection(player.EAST);
                }
            }
            else if(e.getKeyChar() == 'a'){
                if(player.getRowPosition() > 0){
                    player.setRowPosition(player.getRowPosition() - 1);
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getWumpus()){
                        status = DEAD;
                    }
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getPit()){
                        status = DEAD;
                    }
                    map.getSquare(player.getColPosition(),player.getRowPosition()).setVisited(true);
                    player.setDirection(player.WEST);
                }

            }
            else if(e.getKeyChar() == 's'){
                if(player.getColPosition() + 1 < map.NUM_COLUMNS){
                    player.setColPosition(player.getColPosition() + 1);
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getWumpus()){
                        status = DEAD;
                    }
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getPit()){
                        status = DEAD;
                    }
                    map.getSquare(player.getColPosition(),player.getRowPosition()).setVisited(true);
                    player.setDirection(player.SOUTH);
                }
            }
            else if (e.getKeyChar() == 'w') {
                if(player.getColPosition() > 0){
                    player.setColPosition(player.getColPosition() - 1);
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getWumpus()){
                        status = DEAD;
                    }
                    if(map.getSquare(player.getColPosition(),player.getRowPosition()).getPit()){
                        status = DEAD;
                    }
                    map.getSquare(player.getColPosition(),player.getRowPosition()).setVisited(true);
                    player.setDirection(player.NORTH);
                }
            }
            else if(e.getKeyChar() == 'l'){
                if(player.getRowPosition() + 1 < map.NUM_ROWS && map.getSquare(player.getColPosition(),player.getRowPosition() + 1).getWumpus() && player.getArrow()){
                    map.getSquare(player.getColPosition(),player.getRowPosition() + 1).setWumpus(false);
                    map.getSquare(player.getColPosition(),player.getRowPosition() + 1).setDeadWumpus(true);
                    messages.append("You hear a scream.\n");
                }
                player.setDirection(player.EAST);
                player.setArrow(false);
            }
            else if (e.getKeyChar() == 'j'){
                if(player.getRowPosition() > 0 && map.getSquare(player.getColPosition(),player.getRowPosition() - 1).getWumpus() && player.getArrow()){
                    map.getSquare(player.getColPosition(),player.getRowPosition() - 1).setWumpus(false);
                    map.getSquare(player.getColPosition(),player.getRowPosition() - 1).setDeadWumpus(true);
                    messages.append("You hear a scream.\n");
                }
                player.setDirection(player.WEST);
                player.setArrow(false);
            }
            else if(e.getKeyChar() == 'k'){
                if(player.getColPosition() + 1 < map.NUM_COLUMNS && map.getSquare(player.getColPosition() + 1,player.getRowPosition()).getWumpus() && player.getArrow()){
                    map.getSquare(player.getColPosition() + 1,player.getRowPosition()).setWumpus(false);
                    map.getSquare(player.getColPosition() + 1,player.getRowPosition()).setDeadWumpus(true);
                    messages.append("You hear a scream.\n");
                }
                player.setDirection(player.SOUTH);
                player.setArrow(false);
            }
            else if(e.getKeyChar() == 'i'){
                if(player.getColPosition() > 0 && map.getSquare(player.getColPosition() - 1,player.getRowPosition()).getWumpus() && player.getArrow()){
                    map.getSquare(player.getColPosition() - 1,player.getRowPosition()).setWumpus(false);
                    map.getSquare(player.getColPosition() - 1,player.getRowPosition()).setDeadWumpus(true);
                    messages.append("You hear a scream.\n");
                }
                player.setDirection(player.NORTH);
                player.setArrow(false);
            }
            else if(e.getKeyChar() == 'c'){
                if(player.getColPosition() == map.getLadderC() && player.getRowPosition() == map.getLadderR() && player.getGold()){
                    status = WON;
                }
            }
            else if(e.getKeyChar() == 'p' && map.getSquare(player.getColPosition(),player.getRowPosition()).getGold()){
                player.setGold(true);
                map.getSquare(player.getColPosition(),player.getRowPosition()).setGold(false);
            }
            else if(e.getKeyChar() == '*'){
                CHEATING = !CHEATING;
            }
        }
        else if(e.getKeyChar() == 'n' && (status == WON || status == DEAD) ){
            reset();
        }
        repaint();
    }
    @Override
    public void addNotify(){
        super.addNotify();
        requestFocus();
    }
    @Override
    public void keyPressed(KeyEvent e){

    }
    @Override
    public void keyReleased(KeyEvent e){

    }


    public static void main(String[] args) {

    }
    public WumpusMap getMap(){return map;}

}
