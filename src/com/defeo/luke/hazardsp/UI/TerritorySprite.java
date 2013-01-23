package com.defeo.luke.hazardsp.UI;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;
import com.defeo.luke.hazardsp.GameEngine.*;

/**
 * 
 * @author Thomas Yorkshire
 *
 */
public class TerritorySprite extends Sprite {

	//Territory
	Territory territory;
	
	public TerritorySprite(Path inputPath, Paint inputLine, Territory territory, PaintPalette paintPalette){
	
		super();
		//Set up the type of spriteFactory
		super.Type = SpriteType.TERRITORY;
		
		//Set up defaults
		path = inputPath;
		line = inputLine;
		this.territory = territory;
		this.paintPalette = paintPalette;
		
		/*Create region object
		 *Found this workaround here:
		 *http://stackoverflow.com/questions/2597590/how-can-i-tell-if-a-closed-path-contains-a-given-point
		 */
		
		RectF rectF = new RectF();
		path.computeBounds(rectF, true);
		region = new Region();
		region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
		
		//Determine the centre point
		centreY = region.getBounds().centerX();
		centreX = region.getBounds().centerY();
		
	}
	
	public void setCentrePoint(float newCentreY, float newCentreX) {
		centreY = newCentreY;
		centreX = newCentreX;
	}
	
    public void setDimmed() {
        
    	setDim(true);
    }

    public void setNormal() {

    	setDim(false);
    }

	@Override
	public Paint getPaint() {
		
		Paint paint = null;
		
		//Log.i("TERRITORY SPRITE GUI ID", String.valueOf(territory.getOwner().getGuiID()));

		
		if (!isDim()) {
			
		switch(territory.getOwner().getGuiID()) {
		
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
		default:
			Log.i("TERRITORY SPRITE", "DEFAULTED" + territory.getOwner().getGuiID());
	
		}
		}
		else {
			switch(territory.getOwner().getGuiID()) {
			
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
			default:
				Log.i("TERRITORY SPRITE", "DEFAULTED ON DIM");
			}
			
		}
		
		if (paint == null) {Log.i("TERRITORY SPRITE", "PAINT IS NULL");};
		
		return paint;
	}

	@Override
	public String getText() {

		//Print the territory armies present
		return String.valueOf(territory.getNoOfArmiesPresent());
	}

    public Territory getTerritory() {
        return territory;
    }
}
