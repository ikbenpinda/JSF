/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.*;
import java.util.Observable;
import java.util.Observer;
import jsf31kochfractalfx.JSF31KochFractalFX;
import javafx.application.Application;
import timeutil.TimeStamp;

/**
 *
 * @author rage
 */
public class KochManager implements Observer{
    
    private Application application;
    private ArrayList<Edge> arraylist;
    private KochFractal koch;
    
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        this.arraylist = new ArrayList<Edge>();
        this.koch = new KochFractal();
    }
    
    public void changeLevel(int nxt){
        koch.setLevel(nxt);
        drawEdges();
    }
    
    public void drawEdges(){
        
        TimeStamp timestamp = new TimeStamp();
        timestamp.setBegin();
        
        application.clearKochPanel();
        koch.generateLeftEdge();
        koch.generateBottomEdge();
        koch.generateRightEdge();
        
        timestamp.setEnd();
        
        application.setTextCalc();
    }

    @Override
    public void update(Observable o, Object arg) {
        application.drawEdge((Edge)arg);        
    }
}
