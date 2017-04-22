package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.LDGame;
import com.mygdx.game.controller.InputController;
import com.mygdx.game.controller.InputController2;
import com.mygdx.game.controller.PlayerEventListener;
import com.mygdx.game.data.AnimationCache;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kettricken on 21.04.2017.
 */
public class GameScreen extends BaseScreen implements PlayerEventListener {


    private Terrain terrain;
    private Player player;
    private Player player2;

    ArrayList<Player> players = new ArrayList();

    public GameScreen(LDGame game) {
        super(game);
        terrain = new Terrain();
    }

    @Override
    public void show() {
        super.show();

        generatePlayer();

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage());
        inputMultiplexer.addProcessor((InputController) player.getMovementController());
        inputMultiplexer.addProcessor((InputController2) player2.getMovementController());
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

        Gdx.gl20.glClearColor(0.75f, 0.75f, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if (o1.getY() > o2.getY()) {
                    return -1;
                } else if (o1.getY() < o2.getY()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        batch.begin();
        for (Player player : players) {
            if (player.isBehindTerrain()) {
                player.draw(batch);
            }
        }
        terrain.draw(batch);
        for (Player player : players) {
            if (!player.isBehindTerrain()) {
                player.draw(batch);
            }
        }
        batch.end();

//        shapes.begin();
//        terrain.debug(shapes);
//        for (Player player : players) {
//            player.debug(shapes);
//        }
//        shapes.end();
    }

    @Override
    protected void layoutViewsLandscape(int width, int height) {

    }

    @Override
    protected void layoutViewsPortrait(int width, int height) {

    }

    private void generatePlayer() {
        player = new Player(AnimationCache.FISH_ANIMATION);
        player.setMovementController(new InputController());
        player.setPlayerEventListener(this);
        float x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape());
        float y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape());
        player.setPosition(x, y);
        player.getMovementController().setMovementListener(player);
        players.add(player);

        player2 = new Player(AnimationCache.PLAYER1_ANIMATION);
        player2.setMovementController(new InputController2());
        player2.setPlayerEventListener(this);
        x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape());
        y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape());
        player2.setPosition(x, y);
        player2.getMovementController().setMovementListener(player2);
        players.add(player2);
    }

    private void update(float delta) {
        player.update(delta);
        player2.update(delta);
        if (! terrain.contains(player)) {
            player.fall();
        }
        if (! terrain.contains(player2)) {
            player2.fall();
        }
    }

    @Override
    public void kick(Player player) {
        for (Player conterPlayer : players) {
            if (conterPlayer != player) {
                if (conterPlayer.isKicked(player.getKickBox())) {
                    conterPlayer.kicked(player.getKickDirection());
                }
            }
        }
    }
}
