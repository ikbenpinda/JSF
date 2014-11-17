package calculate;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import jsf31kochfractalfx.*;
import timeutil.TimeStamp;
//</editor-fold>

/**
 * Manager and global class for calculating fractals.
 * @author rage/Etienne (rage is my username in Linux Mint.)
 */
public class KochManager{
    
    private final JSF31KochFractalFX application;
    private ArrayList<Edge> edges;
    private TimeStamp threadTime;
    private ExecutorService pool;
    private Future<ArrayList<Edge>> f1;
    private Future<ArrayList<Edge>> f2;
    private Future<ArrayList<Edge>> f3;
    public Task_GenerateEdge tge1;
    public Task_GenerateEdge tge2;
    public Task_GenerateEdge tge3;
    
    public CyclicBarrier cb;
    
    public KochManager(JSF31KochFractalFX application) {
        this.application = application;
        this.edges = new ArrayList<>();
        this.pool = Executors.newFixedThreadPool(3);
        this.cb = new CyclicBarrier(3);        
    }    
    
    /**
     * Changes the fractal to a given level.
     * @param nxt an integer indicating the level.
     */
    public void changeLevel(int nxt){        
        
        //Cancel running tasks.
        if (tge1.isRunning()) {
            tge1.cancel(true);
        }
        if (tge2.isRunning()) {
            tge2.cancel(true);
        }
        if (tge3.isRunning()) {
            tge3.cancel(true);
        }

        this.tge1 = new Task_GenerateEdge(this, new KochFractal(), Side.LEFT, nxt);
        this.tge2 = new Task_GenerateEdge(this, new KochFractal(), Side.RIGHT, nxt);
        this.tge3 = new Task_GenerateEdge(this, new KochFractal(), Side.BOTTOM, nxt);
        
        //Prevents redrawing of previously generated edges.
        clearEdges();                

        Thread t1 = new Thread(tge1);
        Thread t2 = new Thread(tge2);
        Thread t3 = new Thread(tge3);
        
        //Measures the length of the multithreaded calculations.
        /*TimeStamp*/ threadTime = new TimeStamp();
        threadTime.setBegin();        
        
        t1.start();
        t2.start();
        t3.start();
        
        threadTime.setEnd();
        
        application.setTextCalc(threadTime.toString());
        application.requestDrawEdges();
        //drawEdges();
    }
    
    /**
     * Draws the edges and measures how long it takes.
     */
    public synchronized void drawEdges(){       
        application.clearKochPanel();
        
        //Sets timing for drawing said edges.
        TimeStamp drawTime = new TimeStamp();
        drawTime.setBegin();                
        
        for (Edge e : edges){
            application.drawEdge(e);
        }
        
        //For unknown reason drawing takes longer than calculating.
        drawTime.setEnd();
        
        //Implicit conversion of int to string.
        application.setTextNrEdges("" + edges.size());
        application.setTextDraw(drawTime.toString());        
    }   
    
    /**
     * Synchronized method for adding edges.
     */
    public synchronized void addEdges(ArrayList<Edge> list){
        for (Edge e : list){
            edges.add(e);
        }        
    }
    
    /**
     * Synchronized method to clear edges.
     */
    public synchronized void clearEdges(){
        edges.clear();
    }        
}
