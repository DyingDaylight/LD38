package com.mygdx.game.controller;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by kettricken on 22.04.2017.
 */
public interface MovementController {

    Vector2 direction = new Vector2();
    public void progress(float delta);
    public boolean isMovingRight();
    public boolean isMovingLeft();
    public boolean isMovingUp();
    public boolean isMovingDown();
}
