package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.controller.InputController;
import com.mygdx.game.controller.MovementController;
import com.mygdx.game.data.ImageCache;

/**
 * Created by kettricken on 22.04.2017.
 */
public class Player extends Sprite {

    enum State {
        STAND("fish", 3, 0.1f, Animation.PlayMode.LOOP),
        FALL("fish", 3, 0.1f, Animation.PlayMode.LOOP),
        JUMP("player", 0, 0, Animation.PlayMode.NORMAL),
        WALK("player", 2, 0.1f, Animation.PlayMode.LOOP),
        KEEK("player", 2, 0.1f, Animation.PlayMode.NORMAL);

        private boolean animated;
        private Animation animation;
        private TextureRegion region;

        State(String texture, int count, float duration, Animation.PlayMode playMode) {
            if (count > 0) {
                animated = true;
                animation = new Animation(duration, ImageCache.getFrames(texture, 1, count));
                animation.setPlayMode(playMode);
            } else {
                animated = false;
                region = ImageCache.getTexture(texture);
            }
        }

        public TextureRegion getFrame(float stateTime) {
            if (animated) {
                return (TextureRegion) animation.getKeyFrame(stateTime, false);
            } else {
                return region;
            }
        }
    }

    private final int FEET_HEIGHT = 50;

    private TextureRegion textureRegion;
    private Vector2 collisionPoint;

    private MovementController movementController;

    private final Vector2 velocity = new Vector2();
    private final Vector2 speed = new Vector2(300, 300);
    private final float gravity = 100;

    private State state;
    private float stateTime;

    public Player() {
        super(ImageCache.getTexture("fish"));
        textureRegion = ImageCache.getTexture("fish");
        collisionPoint = new Vector2();

        setState(State.STAND);
    }

    public void update(float delta) {
        updateState(delta);
        getMovementController().progress(delta);

        if (state == State.FALL) {
            velocity.x = 0;
            velocity.y -= gravity;
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
        }

        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);

    }

    @Override
    public void draw(Batch batch) {
        setRegion(getStateFrame());
        super.draw(batch);
        //batch.draw(textureRegion, getX(), getY());
    }

    private void setState(State state) {
        //if (gameScreen.isGameOver()) return;
        if (this.state != state) {
            this.state = state;
            stateTime = 0;
        }
    }

    private void updateState(float delta) {
        //if (gameScreen.isGameOver()) return;

        stateTime += delta;
        if (state.animated && state.animation.isAnimationFinished(stateTime)) {
            if (state == State.JUMP) {
                setState(State.STAND);
            }
        }
    }

    private TextureRegion getStateFrame() {
        return state.getFrame(stateTime);
        //return textureRegion;
    }

    public void setMovementController(InputController movementController) {
        this.movementController = movementController;
    }

    public MovementController getMovementController() {
        return movementController;
    }

    public void fall() {
        setState(State.FALL);
    }

    public Vector2 getCollisionPoint() {
        return collisionPoint.set(getX() + getWidth() / 2, getY());
    }
}
