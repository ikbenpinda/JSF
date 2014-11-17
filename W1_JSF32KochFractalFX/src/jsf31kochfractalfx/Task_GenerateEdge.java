package jsf31kochfractalfx;

import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import calculate.Side;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Etienne
 */
public class Task_GenerateEdge extends javafx.concurrent.Task<ArrayList<Edge>> implements Observer{

    KochManager km;
    KochFractal kf;    
    Side side;
    
    ArrayList<Edge> array;

    public Task_GenerateEdge(KochManager km, KochFractal kf, Side side, int level){
        this.km = km;
        this.kf = kf;
        this.side = side;
        this.array = new ArrayList<>();              
        kf.setLevel(level);
        kf.addObserver(this);  
    }
    
    @Override
    protected ArrayList<Edge> call() throws Exception {
        
        // GenerateEdge will call update() on this.
        GenerateEdge();
        
        // Update label and progress.
        updateProgress(33, 100);
        updateMessage("Edges: " + array.size());
        
        return array;
        
    }
    
    /**
     * Generates a given edge.
     */
    public void GenerateEdge() throws InterruptedException {
        switch (side) {
            case LEFT:
                kf.generateLeftEdge();
                km.drawEdges();
                Thread.sleep(200);
                break;
            case RIGHT:
                kf.generateRightEdge();
                km.drawEdges();
                Thread.sleep(350);
                break;
            case BOTTOM:
                kf.generateBottomEdge();
                km.drawEdges();
                Thread.sleep(500);
                break;
            default:
                System.out.println("Entered default case.");
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge)arg;
        //System.out.println("Sending Edge." + e.X1 + e.X2 + e.Y1 + e.Y2);
        array.add(e);
    }

    /**
     * Makes sure the calculating of edge gets cancelled too.
     * @param bln
     * @return 
     */
    @Override
    public boolean cancel(boolean bln){
        kf.cancel();
        return super.cancel(bln);
    }
}
