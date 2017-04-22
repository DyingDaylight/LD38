package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.controller.MovementController;
import com.mygdx.game.controller.MovementListener;
import com.mygdx.game.controller.PlayerEventListener;
import com.mygdx.game.data.ImageCache;

import java.util.Map;

/**
 * Created by kettricken on 22.04.2017.
 */
public class Player extends Sprite implements MovementListener {

    public static final int STAND = 0;
    public static final int FALL = 1;
    public static final int KICKED = 2;
    public static final int WALK = 3;
    public static final int KICK = 4;

    private Vector2 collisionPoint;

    private MovementController movementController;
    private PlayerEventListener playerEventListener;

    private final Vector2 velocity = new Vector2();
    private final Vector2 speed = new Vector2(300, 300);
    private final Vector2 kickDirection = new Vector2(0, 1);
    private final Vector2 kickedDirection = new Vector2(0,0);
    private final float gravity = 100;

    private int state;
    private float stateTime;
    private boolean flipX = false;
    private boolean behindTerrain = false;

    private Rectangle intersection = new Rectangle();
    private Map<Integer, StateAnimation> animations;

    private Rectangle kickBox= new Rectangle();
    private Rectangle kickedBox= new Rectangle();

    public Player(Map<Integer, StateAnimation> animations) {
        super(ImageCache.getTexture("fish"));
        collisionPoint = new Vector2();
        this.animations = animations;

        setState(STAND);

        kickedBox.set(30, 30, 30, 30);
    }

    public void update(float delta) {
        updateState(delta);
        getMovementController().progress(delta);

        if (state == FALL) {
            velocity.x = 0;
            velocity.y -= gravity;
        } else if (state == KICK) {
            velocity.x = 0;
            velocity.y = 0;
        } else if (state == KICKED) {
            velocity.x = kickedDirection.x;
            velocity.y = kickedDirection.y;
        } else {
            if (movementController.isMovingLeft()) {
                if (state == STAND) setState(WALK);
                velocity.x = -speed.x;
                velocity.y = 0;
                kickDirection.set(-1, 0);
                flipX = true;
            } else if (movementController.isMovingRight()) {
                if (state == STAND) setState(WALK);
                velocity.x = speed.x;
                velocity.y = 0;
                kickDirection.set(1, 0);
                flipX = false;
            } else if (movementController.isMovingUp()) {
                if (state == STAND) setState(WALK);
                velocity.y = speed.y;
                velocity.x = 0;
                kickDirection.set(0, 1);
                flipX = true;
            } else if (movementController.isMovingDown()) {
                if (state == STAND) setState(WALK);
                velocity.y = -speed.y;
                velocity.x = 0;
                kickDirection.set(0, -1);
                flipX = false;
            } else {
                if (state == WALK) setState(STAND);
                velocity.x = 0;
                velocity.y = 0;
            }
        }

        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);

    }

    @Override
    public void draw(Batch batch) {
        try {
            setRegion(getStateFrame());
            setFlip(flipX, false);
        } catch (Exception e) {
            String message = String.format("Failed to obtain animation. State: %d; texture: %s",
                    state,
                    getCurrentAnimation().getTextureName());
            throw new RuntimeException(message);
        }
        super.draw(batch);
    }

    private void setState(int state) {
        //if (gameScreen.isGameOver()) return;
        if (this.state != state) {
            this.state = state;
            stateTime = 0;
        }
    }

    private void updateState(float delta) {
        //if (gameScreen.isGameOver()) return;

        stateTime += delta;
        StateAnimation animation = getCurrentAnimation();
        if (animation.isAnimated() && animation.isAnimationFinished(stateTime)) {
            if (state == KICKED) {
                setState(STAND);
            } else if (state == KICK) {
                setState(STAND);
            }
        }
    }

    private TextureRegion getStateFrame() {
        return getCurrentAnimation().getFrame(stateTime);
    }

    public void setMovementController(MovementController movementController) {
        this.movementController = movementController;
    }

    public MovementController getMovementController() {
        return movementController;
    }

    public void setPlayerEventListener(PlayerEventListener playerEventListener) {
        this.playerEventListener = playerEventListener;
    }

    public PlayerEventListener getPlayerEventListener() {
        return playerEventListener;
    }

    public void fall() {
        if (state == FALL) return;
        setState(FALL);
        if (kickedDirection.x == -1 || kickedDirection.y == 1) {
            behindTerrain = true;
        } else if (kickedDirection.x == 1 || kickedDirection.y == -1){
            behindTerrain = false;
        } else if (velocity.x > 0 || velocity.y < 0) {
            behindTerrain = false;
        } else if (velocity.x < 0 || velocity.y > 0) {
            behindTerrain = true;
        }
    }

    public Vector2 getCollisionPoint() {
        return collisionPoint.set(getX() + getWidth() / 2, getY());
    }

    public boolean isKicked(Rectangle kickRegion) {
        getKickedBox();
        return Intersector.intersectRectangles(kickedBox, kickRegion, intersection);
    }

    private StateAnimation getCurrentAnimation() {
        return animations.get(animations.containsKey(state) ? state : STAND);
    }

    @Override
    public void onKick() {
        setState(KICK);
        if (playerEventListener != null) {
            playerEventListener.kick(this);
        }
    }

    public Vector2 getKickDirection() {
        return kickDirection;
    }

    public Rectangle getKickBox() {
        if (kickDirection.x == -1) {
            return kickBox.set(getX() + 13, getY(), 30, 30);
        } else if (kickDirection.x == 1) {
            return kickBox.set(getX() + getRegionWidth() - 50, getY(), 30, 30);
        } else {
            return kickBox.set(getX() + getRegionWidth() / 2 - 20, getY(), getRegionWidth() / 2 - 10, 20);
        }
    }


    public Rectangle getKickedBox() {
        return kickedBox.set(getX() + getRegionWidth() / 2 - 20, getY(), 40, 60);
    }

    public void kicked(Vector2 kickDirection) {
        setState(KICKED);
        if (kickDirection.x == -1) {
            kickedDirection.x = -700;
            kickedDirection.y = 0;
            flipX = true;
        } else if (kickDirection.x == 1) {
            kickedDirection.x = 700;
            kickedDirection.y = 0;
            flipX = false;
        } else if (kickDirection.y == 1) {
            kickedDirection.x = 0;
            kickedDirection.y = 700;
            flipX = false;
        } else if (kickDirection.y == -1) {
            kickedDirection.x = 0;
            kickedDirection.y = -700;
            flipX = true;
        }
    }

    public void debug(ShapeRenderer shapes) {
        getKickBox();
        getKickedBox();
        shapes.setColor(1, 0, 0, 1);
        shapes.rect(kickBox.getX(), kickBox.getY(), kickBox.getWidth(), kickBox.getHeight());
        shapes.setColor(0, 0, 1, 1);
        shapes.rect(kickedBox.getX(), kickedBox.getY(), kickedBox.getWidth(), kickedBox.getHeight());
    }

    public boolean isBehindTerrain() {
        return behindTerrain;
    }
}
