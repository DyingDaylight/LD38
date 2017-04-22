package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by kettricken on 22.04.2017.
 */
public class InputController2 extends InputAdapter implements MovementController {

    MovementListener movementListener;

    @Override
    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    @Override
    public void progress(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            direction.x = -1;
            direction.y = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            direction.x = 1;
            direction.y = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            direction.y = 1;
            direction.x = 0;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
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
            case Input.Keys.P:
                System.out.println("P");
                System.out.println(movementListener);
                if (movementListener != null) {
                    System.out.println("Kick 2");
                    movementListener.onKick();
                }
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
