package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by kettricken on 22.04.2017.
 */
public class InputController extends InputAdapter implements MovementController {

    @Override
    public void progress(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x = -1;
            direction.y = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)){
            direction.x = 1;
            direction.y = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.W)){
            direction.y = 1;
            direction.x = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)){
            direction.y = -1;
            direction.x = 0;
        } else {
            direction.x = 0;
            direction.y = 0;
        }
    }

    @Override
    public boolean isMovingRight() {
        return direction.x == 1;
    }

    @Override
    public boolean isMovingLeft() {
        return direction.x == -1;
    }

    @Override
    public boolean isMovingUp() {
        return direction.y == 1;
    }

    @Override
    public boolean isMovingDown() {
        return direction.y == -1;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.W:
//                if (movementListener != null)
//                    movementListener.onJump();
                break;
            case Input.Keys.LEFT:
//                if (movementListener != null)
//                    movementListener.onLeftLegJump();
                break;
            case Input.Keys.RIGHT:
//                if (movementListener != null)
//                    movementListener.onRightLegJump();
                break;
            default:
                break;
        }
        return super.keyDown(keycode);
    }
}
