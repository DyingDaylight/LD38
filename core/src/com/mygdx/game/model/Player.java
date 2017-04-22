package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private final Vector2 kickDirection = new Vector2(300, 300);
    private final float gravity = 100;

    private int state;
    private float stateTime;

    private Rectangle kickRegion;
    private Rectangle intersection = new Rectangle();
    private Map<Integer, StateAnimation> animations;

    public Player(Map<Integer, StateAnimation> animations) {
        super(ImageCache.getTexture("fish"));
        collisionPoint = new Vector2();
        this.animations = animations;

        setState(STAND);
        kickRegion = new Rectangle(0, 0, getRegionWidth() / 2, getRegionHeight());
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
            velocity.x = 0;
            velocity.y = 0;
        } else {
            if (movementController.isMovingLeft()) {
                velocity.x = -speed.x;
                velocity.y = 0;
            } else if (movementController.isMovingRight()) {
                velocity.x = speed.x;
                velocity.y = 0;
            } else if (movementController.isMovingUp()) {
                velocity.y = speed.y;
                velocity.x = 0;
            } else if (movementController.isMovingDown()) {
                velocity.y = -speed.y;
                velocity.x = 0;
            } else {
                velocity.x = 0;
                velocity.y = 0;
            }
            kickDirection.set(velocity.x, velocity.y);
        }

        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);

    }

    @Override
    public void draw(Batch batch) {
        try {
            setRegion(getStateFrame());
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
        setState(FALL);
    }

    public void kicked() {
        setState(KICKED);
    }

    public Vector2 getCollisionPoint() {
        return collisionPoint.set(getX() + getWidth() / 2, getY());
    }

    public boolean inRegion(Rectangle kickRegion) {
        return Intersector.intersectRectangles(getBoundingRectangle(), kickRegion, intersection);
    }

    @Override
    public void onKick() {
        setState(KICK);
        if (playerEventListener != null) {
            playerEventListener.kick(this);
        }
    }

    public Rectangle getKickRegion() {
        return getBoundingRectangle();
    }

    private StateAnimation getCurrentAnimation() {
        return animations.get(animations.containsKey(state) ? state : STAND);
    }
}
