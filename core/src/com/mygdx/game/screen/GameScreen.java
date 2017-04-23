package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.LDGame;
import com.mygdx.game.controller.AIController;
import com.mygdx.game.controller.InputController;
import com.mygdx.game.controller.PlayerEventListener;
import com.mygdx.game.data.AnimationCache;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.Terrain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

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
//        inputMultiplexer.addProcessor((InputController) player2.getMovementController());
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

        shapes.begin();
        terrain.debug(shapes);
        for (Player player : players) {
            player.debug(shapes);
        }
        shapes.end();
    }

    @Override
    protected void layoutViewsLandscape(int width, int height) {

    }

    @Override
    protected void layoutViewsPortrait(int width, int height) {

    }

    private void generatePlayer() {
        player = new Player(AnimationCache.FISH_ANIMATION);
        player.setMovementController(InputController.getInputControllerArrows());
        player.setPlayerEventListener(this);
        float x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape());
        float y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape());
        player.setPosition(x, y);
        player.getMovementController().setMovementListener(player);
        players.add(player);

        player2 = new Player(AnimationCache.PLAYER1_ANIMATION);
        //player2.setMovementController(InputController.getInputControllerWasd());
        player2.setMovementController(new AIController(this, player2));
        player2.setPlayerEventListener(this);
        x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape());
        y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape());
        player2.setPosition(x, y);
        player2.getMovementController().setMovementListener(player2);
        players.add(player2);
    }

    private void update(float delta) {
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            player.update(delta);
            if (! terrain.contains(player)) {
                if (player.isAlive()) {
                    if (player.getCollisionPoint().y > terrain.getTopCape() || player.getCollisionPoint().x < terrain.getLeftCape()) {
                        player.fall(true);
                    } else if (player.getCollisionPoint().y < terrain.getBottomCape() || player.getCollisionPoint().x > terrain.getRightCape()) {
                        player.fall(false);
                    }
                } else {
                    if (player.getX() > 1500 || player.getX() < -500/* || player.getY() > 1000*/ || player.getY() < -100) {
                        playerIterator.remove();
                    }
                }
            }
        }

        if (players.size() == 1) {
            players.get(0).onWin();
            // TODO implement win screen
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

    public Player getClosestPlayer(Player player) {
        float distance = Float.MAX_VALUE;
        Player closestPlayer = null;
        for (Player p : players) {
            if (p == player) continue;
            float d = Vector2.dst(player.getX(), player.getY(), p.getX(), p.getY());
            if (d < distance) {
                distance = d;
                closestPlayer = p;
            }
        }
        return closestPlayer;
    }
}
