package com.mygdx.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kettricken on 22.04.2017.
 */
public class InputController extends InputAdapter implements MovementController {

    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int KICK = 4;

    private Map<Integer, Integer> keys;
    private Map<Integer, Integer> actions;
    private MovementListener movementListener;

    public static InputController getInputControllerArrows() {
        return new InputController(new HashMap<Integer, Integer>() {{
            put(TOP, Input.Keys.UP);
            put(RIGHT, Input.Keys.RIGHT);
            put(DOWN, Input.Keys.DOWN);
            put(LEFT, Input.Keys.LEFT);
            put(KICK, Input.Keys.SPACE);
        }});
    }

    public static InputController getInputControllerWasd() {
        return new InputController(new HashMap<Integer, Integer>() {{
            put(TOP, Input.Keys.W);
            put(RIGHT, Input.Keys.D);
            put(DOWN, Input.Keys.S);
            put(LEFT, Input.Keys.A);
            put(KICK, Input.Keys.P);
        }});
    }

    public InputController(Map<Integer, Integer> keys) {
        this.keys = keys;

        actions = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry: keys.entrySet()) {
            actions.put(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void setMovementListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    @Override
    public void progress(float delta) {
        if (isKeyPressed(LEFT)) {
            direction.x = -1;
        } else if (isKeyPressed(RIGHT)){
            direction.x = 1;
        } else {
            direction.x = 0;
        }
        if (isKeyPressed(TOP)){
            direction.y = 1;
        } else if (isKeyPressed(DOWN)){
            direction.y = -1;
        } else {
            direction.y = 0;
        }
    }

    @Override
    public void kickedFinished() {

    }

    @Override
    public void kicked() {

    }

    private boolean isKeyPressed(Integer action) {
        return Gdx.input.isKeyPressed(keys.get(action));
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
    public void kickFinished() {

    }

    @Override
    public boolean keyDown(int keycode) {
        int action = actions.containsKey(keycode) ? actions.get(keycode) : -1;
        switch (action){
            case KICK:
                if (movementListener != null)
                    movementListener.onKick();
                break;

            default:
                break;
        }
        return super.keyDown(keycode);
    }
}
