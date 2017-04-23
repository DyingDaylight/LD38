package com.mygdx.game.controller;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.Player;
import com.mygdx.game.screen.GameScreen;

/**
 * Created by kettricken on 23.04.2017.
 */
public class AIController implements MovementController {

    private GameScreen gameScreen;
    private Player player;

    public static final int IDLE = 0;
    public static final int RUNNING = 1;
    public static final int KIKCING = 2;
    public static final int KICKED = 3;

    private int state = 0;

    private Vector2 target = new Vector2();
    private Vector2 direction = new Vector2();

    public AIController(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
    }

    @Override
    public void setMovementListener(MovementListener movementListener) {

    }

    @Override
    public void progress(float delta) {
        if (state == IDLE) {
            Player closestPlayer = gameScreen.getClosestPlayer(player);
            if (closestPlayer != null) {
                setState(RUNNING);
                target.set(closestPlayer.getX(), closestPlayer.getY());
            }
        } else if (state == RUNNING) {
            if (target.x > player.getX()) {
                direction.x = 1;
            } else if (target.x < player.getX()) {
                direction.x = -1;
            } else {
                direction.x = 0;
            }
            if (target.y > player.getY()) {
                direction.y = 1;
            } else if (target.y < player.getY()) {
                direction.y = -1;
            } else {
                direction.y = 0;
            }
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

    public void setState(int state) {
        if (this.state != state)
            this.state = state;
    }
}
