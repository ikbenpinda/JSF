package read;

//<editor-fold defaultstate="collapsed" desc="imports">
import read.JSF32KochFractalFX;
import read.TimeStamp;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import write.Edge;
//</editor-fold>

/**
 * Manager and global class for calculating fractals.
 * @author rage/Etienne (rage is my username in Linux Mint.)
 */
public class KochManager{
    
    private final JSF32KochFractalFX application;
    
    public KochManager(JSF32KochFractalFX application) {        
        this.application = application;
    }    
    
    /**
     * Draws the edges and measures how long it takes.
     */
    public void drawEdges() throws IOException, ClassNotFoundException{
	
        application.clearKochPanel();
        	
	FileInputStream fin = new FileInputStream("EdgesUnbuffered");
	ObjectInputStream oin = new ObjectInputStream(fin);
	
	// Sets timing for drawing said edges.
	TimeStamp drawTime = new TimeStamp();
	drawTime.setBegin();        
	
	try {	    
	
	    Object obj = oin.readObject();
	    System.out.println("Level: " + obj.toString());
	    
	    while (true) {
		try {
		    obj = oin.readObject();
		    application.drawEdge((write.Edge)obj);
		} catch (EOFException e) {
		    break;
		}
	    }
	    
	    // For unknown reason drawing takes longer than calculating.
	    drawTime.setEnd();
	    
	} finally {
	    oin.close();
	}

        application.setTextDraw(drawTime.toString());
    }   
}