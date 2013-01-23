package com.defeo.luke.hazardsp.UI;

import android.graphics.*;
import android.util.Log;
import com.defeo.luke.hazardsp.Activities.R;
import com.defeo.luke.hazardsp.Client.Client;


import java.util.ArrayList;


/**
 * Keeps a reference to all paints required throughout the game
 *
 * @author Thomas Yorkshire
 */
public class PaintPalette {

    //Enum of pallete colours
//	public enum Type {	PLAYER1_NORMAL, PLAYER2_NORMAL, PLAYER3_NORMAL, PLAYER4_NORMAL, PLAYER5_NORMAL, PLAYER6_NORMAL,
//						PLAYER1_DIM, PLAYER2_DIM, PLAYER3_DIM, PLAYER4_DIM, PLAYER5_DIM, PLAYER6_DIM,
//					};

    //List of accessible paints
    ArrayList<Paint> palette;

    /**
     * @param widthPixels
     * @param heightPixels
     * @param
     */
    public PaintPalette(int heightPixels, int widthPixels) {

        //Create all paints to be used
        palette = new ArrayList<Paint>();

        //Set up all territory paints
        for (int a = 0; a <= 11; a++) {
            Paint currentPaint = new Paint();
            currentPaint.setStyle(Paint.Style.FILL);
            currentPaint.setStrokeWidth(2);
            palette.add(currentPaint);
        }

//		//Give each player a different colour
//			palette.get(0).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, Color.RED, Color.RED, Shader.TileMode.MIRROR));
//			palette.get(1).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, Color.YELLOW, Color.YELLOW, Shader.TileMode.MIRROR));
//			palette.get(2).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, Color.GREEN, Color.GREEN, Shader.TileMode.MIRROR));
//			palette.get(3).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, Color.BLUE, Color.BLUE, Shader.TileMode.MIRROR));
//			palette.get(4).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, Color.MAGENTA, Color.MAGENTA, Shader.TileMode.MIRROR));
//			palette.get(5).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, Color.CYAN, Color.CYAN, Shader.TileMode.MIRROR));
//
//		//and also set the dimmed gradient													0xFF00FF00
//			palette.get(6).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, 0xFF800000, 0xFF800000, Shader.TileMode.MIRROR));
//			palette.get(7).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, 0xFF808000, 0xFF808000, Shader.TileMode.MIRROR));
//			palette.get(8).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, 0xFF008000, 0xFF008000, Shader.TileMode.MIRROR));
//			palette.get(9).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, 0xFF000080, 0xFF000080, Shader.TileMode.MIRROR));
//			palette.get(10).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, 0xFF800080, 0xFF800080, Shader.TileMode.MIRROR));
//			palette.get(11).setShader (new LinearGradient(0, 0, widthPixels, heightPixels, 0XFF008080, 0XFF008080, Shader.TileMode.MIRROR));

        //Give each player a different colour
        palette.get(0).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFFC80000, 0xFF750000, Shader.TileMode.MIRROR));
        palette.get(1).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFFC8C800, 0xFF757500, Shader.TileMode.MIRROR));
        palette.get(2).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF00C800, 0xFF007500, Shader.TileMode.MIRROR));
        palette.get(3).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF0000C8, 0xFF000075, Shader.TileMode.MIRROR));
        palette.get(4).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFFC800C8, 0xFF750075, Shader.TileMode.MIRROR));
        palette.get(5).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0XFF00C8C8, 0XFF007575, Shader.TileMode.MIRROR));

        //and also set the dimmed gradient													0xFF00FF00
        palette.get(6).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF250000, 0xFF170000, Shader.TileMode.MIRROR));
        palette.get(7).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF252500, 0xFF171700, Shader.TileMode.MIRROR));
        palette.get(8).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF002500, 0xFF001700, Shader.TileMode.MIRROR));
        palette.get(9).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF000025, 0xFF000017, Shader.TileMode.MIRROR));
        palette.get(10).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF250025, 0xFF170017, Shader.TileMode.MIRROR));
        palette.get(11).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0XFF002525, 0XFF001717, Shader.TileMode.MIRROR));


        //Add the text and fonts paints

        //Set up all territory paints
        for (int a = 0; a <= 5; a++) {
            Paint currentPaint = new Paint();
            currentPaint.setStyle(Paint.Style.FILL);
            currentPaint.setStrokeWidth(2);
            currentPaint.setStyle(Paint.Style.FILL);
            currentPaint.setColor(Color.WHITE);
            currentPaint.setStrokeWidth(2);
            currentPaint.setTextAlign(Paint.Align.LEFT);
            currentPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
            currentPaint.setTextSize(Client.get().getCurrentActivity().getResources().getDimension(R.dimen.textsize));
            currentPaint.setTypeface(Typeface.DEFAULT);
            palette.add(currentPaint);

        }

        palette.get(12).setColor(Color.RED);
        palette.get(13).setColor(Color.YELLOW);
        palette.get(14).setColor(Color.GREEN);
        palette.get(15).setColor(Color.BLUE);
        palette.get(16).setColor(Color.MAGENTA);
        palette.get(17).setColor(Color.CYAN);

        //Add the grey gradient
        Paint currentPaint = new Paint();
        currentPaint.setStyle(Paint.Style.FILL);
        currentPaint.setStrokeWidth(2);
        palette.add(currentPaint);
        palette.get(18).setShader(new LinearGradient(0, 0, widthPixels / 17, heightPixels / 17, 0xFF222222, 0xFF555555, Shader.TileMode.MIRROR));
        //


    }

    public Paint getPaint(int index) {

        if (palette.get(index) == null) {
            Log.i("PAINT PALETTE", "PAINT IS NULL");
        }

        return palette.get(index);
    }


}
