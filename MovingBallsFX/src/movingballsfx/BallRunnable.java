package movingballsfx;

/**
 *
 * @author Peter Boots
 */
public class BallRunnable implements Runnable {

    private Ball ball;
    private Monitor m;

    public BallRunnable(Ball ball, Monitor m) {
        this.ball = ball;
        this.m = m;
    }

    @Override
    public void run() {
        // How can we tell if the ball is in the Critical Section?
        boolean insideCs = false;
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                
                // As long as the balls are not in the Critical Section (Cs),
                // they can run freely, simultaneously in peaceful co-existence.
                if (ball.isEnteringCs()) {
                    m.enter(ball);
                    insideCs = true;
                }                
                
                ball.move();                   
                Thread.sleep(ball.getSpeed());
                
                if (ball.isLeavingCs()) {
                    m.exit(ball);
                    insideCs = false;
                }                
                
            } catch (InterruptedException ex) {
                
                // Make sure interrupted waiting threads exit correctly. [7.3]
                if (insideCs) {
                    m.exit(ball);
                }                
                
                Thread.currentThread().interrupt();
            }
        }
    }
}
