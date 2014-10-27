package calculate;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * Seperate Runnable class for threaded calculating of fractal's sides.
 * @author Etienne
 */
public class Calculator implements Callable, Observer{
    
    private KochManager km;
    private KochFractal kf;
    private Side side;    

    public ArrayList<Edge> array;
    
    public Calculator(KochManager km, KochFractal kf, int level, Side side) {
        this.km = km;
        this.kf = kf;  
        this.side = side;
        this.array = new ArrayList<>();
        kf.setLevel(level);
        kf.addObserver(this);
    }    

    /**
     * Generates a given edge.     
     */
    public void GenerateEdge(){
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
    public void update(Observable o, Object arg) {
        Edge e = (Edge)arg;
        //System.out.println("Sending Edge." + e.X1 + e.X2 + e.Y1 + e.Y2);
        array.add(e);
    }
    
    @Override
    public Object call() throws Exception {
        GenerateEdge();        
        try {
            //System.out.println("Entering waiting state.");
            //System.out.println(km.cb.getNumberWaiting());
            km.cb.await();            
            //Thread.sleep(5000);
            //System.out.println("Exiting waiting state.");
        } catch (InterruptedException | BrokenBarrierException ex) {
            Logger.getLogger(Calculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.array;
    }
    
}
