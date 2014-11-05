package movingballsfx;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.lang.InterruptedException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.scene.paint.Color;
//</editor-fold>

/**
 * Monitor class for monitoring balls.
 * @author Etienne
 */
public class Monitor {
    
    // Colors blue and red redefined as reader/writer for readability.
    private final Color READER;
    private final Color WRITER;
    
    private final ReentrantLock lock = new ReentrantLock();
    private Condition okToRead = lock.newCondition();
    private Condition okToWrite = lock.newCondition();
    private int readersActive = 0;
    private int writersActive = 0;
    private int readersWaiting = 0;
    private int writersWaiting = 0;
    
    /**
     * Creates a new Monitor object.
     */
    public Monitor(){
        READER = Color.RED;
        WRITER = Color.BLUE;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Methodpicker for runnable.">

    /**
     * Automatically checks ballcolor to see which enter method to use.
     * @param b - A ball object to enter or exit the a reader/writer given it's color.
     * @throws InterruptedException - a bubble up from the enterReader/enterWriter methods.
     */
    public void enter(Ball b) throws InterruptedException{
        Color ball = b.getColor();
        
        if (ball == READER) {
            enterReader();
        }        
        if (ball == WRITER){ 
            enterWriter();
        }
    }
    
    /**
     * Automatically checks ballcolor to see which exit method to use.
     * @param b - A ball object to enter or exit the a reader/writer given it's color.
     */
    public void exit(Ball b){
        Color ball = b.getColor();
        
        if (ball == READER) {
            exitReader();
        }        
        if (ball == WRITER){ 
            exitWriter();
        }
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Reader methods.">
    
    /**
     * Enters the reader.
     * @throws InterruptedException - Indicates the await() was interrupted.
     */
    public void enterReader() throws InterruptedException{
        lock.lock();
        
        try {
            readersWaiting++;
            
            while(writersActive > 0) {                
                okToRead.await();                
            }
            
            readersWaiting--;
            readersActive++;
        } finally { // Make sure that the lock is always unlocked.
            lock.unlock();
        }
    }
    
    /**
     * Exits the reader.
     */
    public void exitReader(){ 
        lock.lock();
        
        try {
            readersActive--;
            if (readersActive == 0)
                okToWrite.signal();
        } finally {
            lock.unlock();
        }                
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Writer methods.">
    
    /**
     * Enters the writer.
     * @throws InterruptedException - Indicates the await() was interrupted.
     */
    public void enterWriter() throws InterruptedException{
        lock.lock();
        
        try {
            // This could be done inside the following while-loop [sheets 1.14].
            // However, there would then be a space where the ball 
            // is going to wait while the integer indicates it is not.
            writersWaiting++;
            
            while(writersActive > 0 | readersActive > 0) {
                okToWrite.await();                
            }
            
            writersWaiting--;
            writersActive++;
        } finally {
            lock.unlock();
        }    
    }
    
    /**
     * Exits the writer.
     */
    public void exitWriter(){
        lock.lock();
        
        try {
            writersActive--;
            
            if (writersWaiting > 0)
                okToWrite.signal(); // Writers have a higher priority than readers. [7.2]
            else
                okToRead.signalAll(); // Readers can run simultaneously, so we can signal all.
            
        } finally {
            lock.unlock();
        }            
    }
    
    //</editor-fold>    
}
