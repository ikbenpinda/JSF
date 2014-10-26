/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf31kochfractalfx;

import java.util.Observable;
import java.util.Observer;
import calculate.Edge;

/**
 *
 * @author rage 
 */
public class KochObserver implements Observer {

    public KochObserver(){}

    @Override
    public void update(Observable o, Object arg) {
        
        Edge e = (Edge)arg;
        
        System.out.println("Beginpunt van Edge = " + e.X1 + "," + e.Y1 + ".\n");
        System.out.println("Eindpunt van Edge = " + e.X2 + "," + e.Y2 + ".\n");
    }
    
}
