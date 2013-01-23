package com.defeo.luke.hazardsp.UI;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

/**
 * 
 * @author Thomas Yorkshire
 *
 */
public abstract class Sprite {
	
	public enum SpriteType {TERRITORY, BUTTON, BACKGROUND, DISPLAY};

	SpriteType Type;
	
	//Current graphical properties
	Paint paint;
	Paint line;
	Paint textPaint;
	Path path;
	Region region;
	String displayText;
	float centreX;
	float centreY;
	boolean dim;
	PaintPalette paintPalette;

	public Sprite() {
	
	
	
	}
	
public boolean isClicked(float f, float g) {
		
		return region.contains((int)f,(int)g);
		
	}
	
	public void setText(String text) {
		
		displayText = text;
	}
	
	
	public abstract Paint getPaint();
	
	public void setPaint(Paint input) {
		
		 this.paint = input;
		
	}
	
	public void setTextPaint(Paint input){
		
		this.textPaint = input;
	}
	
	public Paint getTextPaint() {
		
		return textPaint;
		
	}
	
	public Paint getLine() {
		return line;
	}
	
	public Path getPath() {
		return path;
	}
	
	public abstract String getText();
	
	public float getX() {
		return centreX;
	
	}
	
	public float getY() {
		return centreY;
	
	}

	public boolean isDim() {
		return dim;
	}

	public void setDim(boolean dim) {
		this.dim = dim;
	}
	
	
	
	
}
