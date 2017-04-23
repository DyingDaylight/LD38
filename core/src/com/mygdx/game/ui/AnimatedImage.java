package com.mygdx.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.model.StateAnimation;

/**
 * Created by kettricken on 23.04.2017.
 */
public class AnimatedImage extends Actor {

    private StateAnimation stateAnimation;

    private float elapsedTime = 0;

    public AnimatedImage(StateAnimation stateAnimation){
        this.stateAnimation = stateAnimation;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        TextureRegion frame = stateAnimation.getFrame(elapsedTime);
        batch.draw(stateAnimation.getFrame(elapsedTime), 0 - frame.getRegionWidth() / 2,0- frame.getRegionHeight() / 2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
    }

    public void setAnimation(StateAnimation animation) {
        this.stateAnimation = animation;
        elapsedTime = 0;
    }
}
