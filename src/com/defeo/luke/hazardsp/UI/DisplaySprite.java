package com.defeo.luke.hazardsp.UI;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

/*
 * Thomas
 */
public class DisplaySprite extends ButtonSprite {

    private String text;

	public DisplaySprite(Path inputPath, Paint inputLine, PaintPalette paintPalette, String text) {
		super(inputPath, inputLine, paintPalette, text);
		Type = SpriteType.DISPLAY;
		// Change the posiiton of the text
		RectF bounds = new RectF();
		this.path.computeBounds(bounds, true);
		this.centreY = bounds.left;  
		System.out.println("Bounds -" +this.centreY);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}