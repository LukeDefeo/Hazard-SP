package com.defeo.luke.hazardsp.UI;

import android.graphics.Paint;
import android.graphics.Path;
import com.defeo.luke.hazardsp.Client.Client;

public class PlayerButtonSprite extends ButtonSprite {

	public PlayerButtonSprite(Path inputPath, Paint inputLine,
			PaintPalette paintPalette, String text) {
		super(inputPath, inputLine, paintPalette, text);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Paint getPaint() {
//		old
//		String playerName = Client.get().getClientName();
//
//		int guiID = Client.get().getGame().getPlayers().get(playerName).getGuiID();

        int guiID = Client.get().getGame().getCurrentPlayerTurn().getGuiID();

		return paintPalette.getPaint(guiID-1);
	}

}
