package com.mygdx.game.model;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.data.ImageCache;

/**
 * Created by kettricken on 22.04.2017.
 */
public class StateAnimation {

    private boolean animated;
    private Animation animation;
    private TextureRegion region;
    private String textureName; //TODO: delete. Need it for logging

    public StateAnimation(String texture, int count, float duration, Animation.PlayMode playMode) {
        textureName = texture;

        if (count > 0) {
            animated = true;
            animation = new Animation(duration, ImageCache.getFrames(texture, 1, count));
            animation.setPlayMode(playMode);
        } else {
            animated = false;
            region = ImageCache.getTexture(texture);
        }
    }

    public String getTextureName() {
        return textureName;
    }

    public TextureRegion getFrame(float stateTime) {
        if (animated) {
            return (TextureRegion) animation.getKeyFrame(stateTime, false);
        } else {
            return region;
        }
    }

    public boolean isAnimated() {
        return animated;
    }

    public boolean isAnimationFinished(float stateTime) {
        return animation.isAnimationFinished(stateTime);
    }
}
