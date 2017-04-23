package com.mygdx.game.controller;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.model.Player;
import com.mygdx.game.screen.GameScreen;

/**
 * Created by kettricken on 23.04.2017.
 */
public class AIController implements MovementController {

    private GameScreen gameScreen;
    private Player player;
    private Player targetPlayer;

    public static final int IDLE = 0;
    public static final int RUNNING = 1;
    public static final int KICKING = 2;
    public static final int KICKED = 3;
    public static final int WAITING_FOR_KICK = 4;

    private static final float MIN_DIFF = 10;
    private static final float KICK_DELAY = 0.2f;

    private int state = 0;
    private float kickDelay = 0;

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
            targetPlayer = gameScreen.getClosestPlayer(player);
            if (targetPlayer != null) {
                if (targetPlayer.isKickable()) {
                    setState(RUNNING);
                }  else {
                    targetPlayer = null;
                }
            }
        } else if (state == RUNNING) {
            if (canKick(player, targetPlayer)) {
                setState(WAITING_FOR_KICK);
                direction.x = 0;
                direction.y = 0;
                return;
            }

            direction.x = comparePositionComponent(targetPlayer.getX(), player.getX());
            direction.y = comparePositionComponent(targetPlayer.getY(), player.getY());
        } else if (state == WAITING_FOR_KICK) {
            kickDelay += delta;
            if (kickDelay >= KICK_DELAY) {
                kickDelay = 0;
                setState(KICKING);
                player.onKick();
            }
        }
    }

    private int comparePositionComponent(float first, float second) {
        if (Math.abs(first - second) < MIN_DIFF) return 0;
        return first > second ? 1 : -1;
    }

    @Override
    public void kickFinished() {
        if (state == KICKING) {
            setState(IDLE);
        }
    }

    @Override
    public void kickedFinished() {
        if (state == KICKED) {
            setState(IDLE);
        }
    }

    @Override
    public void kicked() {
        if (state != KICKED) {
            setState(KICKED);
        }
    }

    private boolean canKick(Player player, Player targetPlayer) {
        return Intersector.overlaps(player.getKickBox(), targetPlayer.getKickedBox());
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
