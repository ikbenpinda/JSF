package write;

import write.Edge;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible for writing the edges to a file.
 *
 * @author Etienne
 */
public class Writer implements Observer {

    /**
     * I'm being lazy here, I know.
     */
    int level = 9;

    /**
     * Filename in field for ease of lookup/change.
     */
    String filename = "Edges";

    /**
     * Makes writing easier.
     */
    ArrayList<Edge> edges = new ArrayList<>();

    /**
     * The selected writing method for the calculated Edges.
     */
    WritingMethod writingmethod;

    /**
     * An enum with the methods to use for writing edges.
     */
    static enum WritingMethod {

	Mapped,
	Binary,
	BinaryBuffered,
	Both
    }

    /**
     * Writes edges for a given level in a given way to a file.
     *
     * @param args none.
     */
    public static void main(String[] args) {

	/**
	 * Results (in mSec), 5 runs:
	 *
	 * Level 9  Unbuffed:	1420 / 2930 / 2831 / 2891 / 2850 
	 *	    Buffed:	150 / 150 / 150 / 150 / 150
	 *
	 * Level 10 Unbuffed:	11612 / 11641 / 11681 / 11733 / 11544 
	 *	    Buffed:	890 / 660 / 664 / 670 / 680
	 *
	 * Level 11 Unbuffed:	49110 / 48362 / 47535 / 46377 / 46579 
	 *	    Buffed:	3469 / 2990 / 3030 / 2921 / 3090
	 *
	 * Level 12 Unbuffed:	Exception in thread "main" java.lang.OutOfMemoryError: Java heap space 
	 *	    Buffed:	Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	 */
	
	Writer writer = new Writer();

	// Already written binary, mapped now.
	writer.writingmethod = WritingMethod.Both;

	// Calculate edges.
	KochFractal kf = new KochFractal();
	kf.setLevel(writer.level);
	kf.addObserver(writer);

	// No manager, so no threading here for now.
	// Might be useful to dramatize timestamps.
	kf.generateRightEdge();
	kf.generateBottomEdge();
	kf.generateLeftEdge();

	// After calculating all edges, write them.
	writer.write();
    }

    /**
     * Picks the right writingmethod for the writing of edges.
     *
     * @param e the Edge object to write.
     */
    void write() {
	// Write accordingly.
	try {
	    switch (writingmethod) {
		case Mapped:
		    Mapped();
		    break;
		case Binary:
		    Binary();
		    break;
		case BinaryBuffered:
		    BinaryBuffered();
		    break;
		case Both:
		    Mapped();
		    Binary();
		    BinaryBuffered();
		    break;
	    }
	} catch (IOException ex) {
	    Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    // Note: Try-with auto-closes streams.
    /**
     * Writes to binary.
     *
     * @param e the Edge object to write.
     * @throws IOException
     */
    void Binary() throws IOException {
	FileOutputStream fout = new FileOutputStream(filename + "Unbuffered");
	TimeStamp ts = new TimeStamp();
	ts.setBegin();
	try (ObjectOutputStream oout = new ObjectOutputStream(fout)) {
	    oout.writeObject(level);
	    for (Edge edge : edges) {
		oout.writeObject((Object) edge);
	    }
	}
	ts.setEnd();
	System.out.println("Written all edges, unbuffered/binary: " + ts.toString());
    }

    /**
     * Writes to binary using a buffer.
     *
     * @param e the Edge object to write.
     * @throws IOException
     */
    void BinaryBuffered() throws IOException {
	FileOutputStream fout = new FileOutputStream(filename + "Buffered");
	BufferedOutputStream bout = new BufferedOutputStream(fout);
	TimeStamp ts = new TimeStamp();
	ts.setBegin();
	try (ObjectOutputStream oout = new ObjectOutputStream(bout)) {
	    oout.writeObject(level);
	    for (Edge edge : edges) {
		oout.writeObject((Object) edge);
	    }
	}
	ts.setEnd();
	System.out.println("Written all edges buffered/binary: " + ts.toString());
    }

    public void Mapped(){
	try {
	    RandomAccessFile ras = new RandomAccessFile(filename + "mapped", "rw");
	    FileChannel fc = ras.getChannel();
	    MappedByteBuffer out;
	    out = fc.map(FileChannel.MapMode.READ_WRITE,0, Integer.BYTES + (Double.BYTES * 4) * edges.size());
	    out.putInt(level);
	    for (Edge e : edges){
		out.putDouble(e.X1);
		out.putDouble(e.X2);
		out.putDouble(e.Y1);
		out.putDouble(e.Y2);
	    }
	    
	    // write object
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(Writer.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    /**
     * KochFractals gives you these edges when it is generating them.
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
	edges.add((Edge) arg);
    }
}
