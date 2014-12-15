package write;

import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 *
 * @author Peter Boots
 */
public class Edge implements Serializable{
    public double X1, Y1, X2, Y2;
    public transient Color color;
    public double r, g, b, a, sat, hue, bri;
    
    /**
     * Creates an Edge. 
     * @param X1
     * @param Y1
     * @param X2
     * @param Y2
     * @param color Null if unknown.
     */
    public Edge(double X1, double Y1, double X2, double Y2, Color color) {
        this.X1 = X1;
        this.Y1 = Y1;
        this.X2 = X2;
        this.Y2 = Y2;	
	
	if (color == null) {
	    getColor();
	}
	
	this.color = color;
	
	// Serializing Color
	this.r = color.getRed();
	this.g = color.getGreen();
	this.b = color.getBlue();
	this.a = color.getOpacity();
	
	/*
	this.sat = color.getSaturation();
	this.hue = color.getHue();
	this.bri = color.getBrightness();
	*/	
    }
    
    public Color getColor(){
	color = new Color(r, g, b, a);
	return color;
    }
}
