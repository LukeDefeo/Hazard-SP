package com.defeo.luke.hazardsp.UI;

import android.graphics.Paint;

public class BackgroundSprite extends Sprite {

	BackgroundSprite() {
		Type = SpriteType.BACKGROUND;
	}
	
	@Override
	public Paint getPaint() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}
}