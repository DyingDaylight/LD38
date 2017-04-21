package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by kettricken on 21.04.2017.
 */
public class Utils {

    public static boolean isLandscape() {
        return Gdx.input.getNativeOrientation() == Input.Orientation.Landscape
                && (Gdx.input.getRotation() == 0 || Gdx.input.getRotation() == 180)
                || Gdx.input.getNativeOrientation() == Input.Orientation.Portrait
                && (Gdx.input.getRotation() == 90 || Gdx.input.getRotation() == 270);
    }
}
