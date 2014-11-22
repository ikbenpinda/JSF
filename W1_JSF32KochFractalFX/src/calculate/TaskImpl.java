package calculate;

//<editor-fold defaultstate="Collapsed" desc="imports">
import calculate.Edge;
import calculate.KochFractal;
import calculate.KochManager;
import calculate.Side;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import jsf31kochfractalfx.JSF31KochFractalFX;
//</editor-fold>

/**
 * JavaFx.Concurrent.Task implementation as a replacement for Calculator.
 * Note: Task is already observable
 * @author Etienne
 */
public class TaskImpl extends Task implements Observer{

    JSF31KochFractalFX gui;
    KochManager km;
    KochFractal kf;
    Side side;
    ArrayList<Edge> array;

    public TaskImpl(JSF31KochFractalFX gui, KochManager km, KochFractal kf,
	    Side side, int level){
	this.gui = gui;
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
    public void GenerateEdge() throws InterruptedException {
        switch (side) {
            case LEFT:
                kf.generateLeftEdge();
                Thread.sleep(200);
                break;
            case RIGHT:
                kf.generateRightEdge();
                Thread.sleep(350);
                break;
            case BOTTOM:
                kf.generateBottomEdge();
                Thread.sleep(500);
                break;
            default:
                System.out.println("Entered default case.");
                break;
        }
    }
   
    //<editor-fold desc="Overrides">
    
    /**
     * Gets called by GenerateEdge().
     * @param o the KochFractal.
     * @param arg an Edge upcasted as an Object.
     */
    @Override
    public void update(Observable o, Object arg) {
        Edge e = (Edge)arg;
        //System.out.println("Sending Edge." + e.X1 + e.X2 + e.Y1 + e.Y2);
        array.add(e);
	gui.drawEdge(e, Color.WHITE);
    }

    /**
     * Makes sure the calculating of edge gets cancelled too.
     * The other Cancel() wasn't overridable, FYI.
     * @param bln
     * @return 
     */
    @Override
    public boolean cancel(boolean bln){
        kf.cancel();
        return super.cancel(bln);
    }

    /**
     * This works the same as your dog, just call it and it will return.
     * @return an array of edges unless cancelled; null
     * @throws Exception
     */
    @Override
    protected ArrayList<Edge> call() throws Exception {

	// GenerateEdge will call update() on this.
	GenerateEdge();

	// Update label and progress.
	updateProgress(33, 100);
	updateMessage("Edges: " + array.size());	

	return array;
    }
    //</editor-fold>
}