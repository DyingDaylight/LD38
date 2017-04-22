package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.LDGame;
import com.mygdx.game.model.Terrain;

/**
 * Created by kettricken on 21.04.2017.
 */
public class GameScreen extends BaseScreen {


    private Terrain terrain;

    public GameScreen(LDGame game) {
        super(game);
        terrain = new Terrain();

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl20.glClearColor(0f, 0f, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        terrain.draw(batch);
        batch.end();

        shapes.begin();
        terrain.debug(shapes);
        shapes.end();
    }

    @Override
    protected void layoutViewsLandscape(int width, int height) {

    }

    @Override
    protected void layoutViewsPortrait(int width, int height) {

    }
}
