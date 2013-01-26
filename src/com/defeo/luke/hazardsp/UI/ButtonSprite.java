package com.defeo.luke.hazardsp.UI;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import com.defeo.luke.hazardsp.Client.Client;
import com.defeo.luke.hazardsp.UI.PaintPalette;
import com.defeo.luke.hazardsp.UI.Sprite;

/**
 * @author Thomas Yorkshire
 */
public class ButtonSprite extends Sprite {

	public ButtonSprite(Path inputPath, Paint inputLine, PaintPalette paintPalette, String text) {
		super();
		super.Type = SpriteType.BUTTON;
	
		//Set up defaults
		this.path = inputPath;
		this.line = inputLine;
		this.paintPalette = paintPalette;
		this.displayText = text;
		
		/*
		 * Create region object
		 * Found this workaround here:
		 * http://stackoverflow.com/questions/2597590/how-can-i-tell-if-a-closed-path-contains-a-given-point
		 */
		RectF rectF = new RectF();
		path.computeBounds(rectF, true);
		region = new Region();
		region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
		
		//Determine the centre point
		centreY = region.getBounds().centerX();
		centreX = region.getBounds().centerY();
	}

    public void setDisplayType() {
        super.Type = SpriteType.DISPLAY;
    }

	@Override
	public Paint getPaint() {
		Paint paint = null;
		boolean beDim = true;
		
		// Need to determine what phase we are in, what our current label is, and thus if we need to be dim.		
		switch (Client.get().getGame().getCurrentPlayerTurn().getCurrentPhase()) {
			case ATTACK:
				if (displayText.equals("Attack")) {beDim = false;}
				break;
	        case MOVE_IN:
	            if (displayText.equals("Attack")) {beDim = false;}
	            break;
			case FORTIFY:
				if (displayText.equals("Fortify")) {beDim = false;}
				break;
			case REINFORCE:
				if (displayText.equals("Reinforce")) {beDim = false;}
				break;
			default:
				//Do nothing for anything else
				break;
		}
		
		if (!beDim){		
			// Need to determine who's go it currently is
			switch (Client.get().getGame().getCurrentPlayerTurn().getGuiID()) {
				case 1:
					paint = paintPalette.getPaint(0);
				break;		
				case 2:
					paint = paintPalette.getPaint(1);
				break;
				case 3:
					paint = paintPalette.getPaint(2);
				break;
				case 4:
					paint = paintPalette.getPaint(3);
				break;
				case 5:
					paint = paintPalette.getPaint(4);
				break;
				case 6:
					paint = paintPalette.getPaint(5);
				break;
			}
		} else {
			// Need to determine who's go it currently is...
			switch (Client.get().getGame().getCurrentPlayerTurn().getGuiID()) {
				case 1:
					paint = paintPalette.getPaint(6);
				break;		
				case 2:
					paint = paintPalette.getPaint(7);
				break;
				case 3:
					paint = paintPalette.getPaint(8);
				break;
				case 4:
					paint = paintPalette.getPaint(9);
				break;
				case 5:
					paint = paintPalette.getPaint(10);
				break;
				case 6:
					paint = paintPalette.getPaint(11);
				break;
			}	
		}
		return paint;
	}

	@Override
	public String getText() {
		return displayText;
	}
}