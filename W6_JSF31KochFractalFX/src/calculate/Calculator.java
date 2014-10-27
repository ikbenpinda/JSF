/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.Observable;
import java.util.Observer;

/**
 * Seperate Runnable class for threaded calculating of fractal's sides.
 * @author Etienne
 */
public class Calculator implements Runnable, Observer{

    private KochManager km;
    private KochFractal kf;
    private Side side;
    
    public Calculator(KochManager m, KochFractal f, int level, Side side) {
        this.km = m;
        this.kf = f;  
        this.side = side;
        kf.setLevel(level);
        kf.addObserver(this);
    }    

    /**
     * Generates a given edge.
     * @param side A Side enum indicating the side of the initial triangle.
     */
    public void GenerateEdge(Side side){
            switch(side){
                case LEFT:
                    kf.generateLeftEdge();                    
                    break;
                case RIGHT:
                    kf.generateRightEdge();
                    break;
                case BOTTOM:
                    kf.generateBottomEdge();
                    break;
                default:
                    System.out.println("Entered default case.");
                    break;
                }
    }
                
    @Override
    public void run() {
        GenerateEdge(side);        
        km.updateCount();
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge)arg;
        //System.out.println("Sending Edge." + e.X1 + e.X2 + e.Y1 + e.Y2);
        km.addEdges((Edge)arg);           
    }
    
}
