package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.LDGame;
import com.mygdx.game.controller.InputController;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Terrain;

/**
 * Created by kettricken on 21.04.2017.
 */
public class GameScreen extends BaseScreen {


    private Terrain terrain;
    private Player player;

    public GameScreen(LDGame game) {
        super(game);
        terrain = new Terrain();
        player = new Player();
        player.setMovementController(new InputController());
        float x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape());
        float y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape());
        player.setPosition(x, y);
    }

    @Override
    public void show() {
        super.show();
        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage());
        inputMultiplexer.addProcessor((InputController) player.getMovementController());
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
//                if (keycode == Input.Keys.ESCAPE) {
//                    if (isPlayable()) {
//                        pauseGame();
//                    } else {
//                        unpauseGame();
//                    }
//                } else if (keycode == Input.Keys.ENTER) {
//                    if (!isPlayable()) getGame().setGameScreen();
//                }
                return super.keyUp(keycode);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl20.glClearColor(0f, 0f, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.update(delta);
        if (! terrain.contains(player)) {
            player.fall();
        }

        batch.begin();
        terrain.draw(batch);
        player.draw(batch);
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
