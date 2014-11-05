/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package calculate;

import java.util.*;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;
import jsf31kochfractalfx.*;
import timeutil.TimeStamp;

/**
 * Manager and global class for calculating fractals.
 * If the application won't start and says it can't find main,
 * restarting netbeans should work.
 * @author rage/Etienne (rage is my username on Linux Mint.)
 */
public class KochManager{
    
    private JSF31KochFractalFX application;
    private ArrayList<Edge> edges;
    private AtomicInteger count;        
    private TimeStamp threadTime;
    
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        this.edges = new ArrayList<>();
        this.count = new AtomicInteger(0);
    }    
    
    /**
     * Changes the fractal to a given level.
     * @param nxt an integer indicating the level.
     */
    public void changeLevel(int nxt){        
        
        //Prevents redrawing of previously generated edges.
        clearEdges();
        
        //Make 3 threads to calculate an individual side.        
        Thread t1 = makeCalcThread(nxt, Side.LEFT);
        Thread t2 = makeCalcThread(nxt, Side.RIGHT);
        Thread t3 = makeCalcThread(nxt, Side.BOTTOM);                
        
        //Measures the length of the multithreaded calculations.
        /*TimeStamp*/ threadTime = new TimeStamp();
        threadTime.setBegin();        
        
        t1.start();        
        t2.start();
        t3.start();
               
        //threadTime.setEnd();                               
        //application.setTextCalc(threadTime.toString());
        
    }
    
    /**
     * Draws the edges and measures how long it takes.
     */
    public synchronized void drawEdges(){
        
        application.clearKochPanel();
        
        //Sets timing for drawing said edges.
        TimeStamp drawTime = new TimeStamp();
        drawTime.setBegin();                
        int i = 0;
        
        for (Edge e : edges) {
            application.drawEdge(e);            
            i++;
        }        
                
        //For unknown reason drawing takes longer than calculating.
        drawTime.setEnd();
        
        //Implicit conversion to string of int.
        application.setTextNrEdges("" + i);
        application.setTextDraw(drawTime.toString());
    }
    
    /**
     * Synchronized method for adding edges.
     * @param e the Edge object to add.
     */
    public synchronized void addEdges(Edge e){
        edges.add(e);
    }
    
    /**
     * Synchronized method to clear edges.
     */
    public synchronized void clearEdges(){
        edges.clear();
    }
    
    /**
     * Updates count and resets count + draws edges when all threads are ready.
     */
    public void updateCount(){        
        if (count.incrementAndGet() == 3) {
            threadTime.setEnd();                                           
            //Reset counter and draw when all threads are ready.
            count.set(0);                        
            application.setTextCalc(threadTime.toString());
            application.requestDrawEdges();
        }
    }    
    
    /**
    * Refactoring of Thread(Calculator) creation.
    * @return a Thread(Calculator) object.
    */
    private Thread makeCalcThread(int nxt, Side side){        
        Calculator c = new Calculator(this, new KochFractal(), nxt, side);
        return new Thread(c);
    }
}
