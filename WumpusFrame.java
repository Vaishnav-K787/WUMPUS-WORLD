package src;

import javax.swing.*;

public class WumpusFrame extends JFrame {
    WumpusPanel wp = new WumpusPanel();
    public WumpusFrame(){
        super("Wumpus World");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,600,800);
        add(wp);
        setVisible(true);
    }
    public WumpusPanel getWp(){
        return wp;
    }
}
