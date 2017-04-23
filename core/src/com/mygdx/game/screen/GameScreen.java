package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.LDGame;
import com.mygdx.game.controller.AIController;
import com.mygdx.game.controller.InputController;
import com.mygdx.game.controller.PlayerEventListener;
import com.mygdx.game.data.*;
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

    private static final float DELAY_TIME = 4;

    private float delay = 0.0f;

    private InputMultiplexer inputMultiplexer;

    private ParticleEffect ripples;
    private Terrain terrain;
    private Player player;
    private ArrayList<Player> players = new ArrayList();
    private int chosenPlayerType;

    private Table finalTable;
    private Table helpTable;
    private Image winImage;
    private Image counterImage;

    private boolean isGameFinished = false;

    public GameScreen(LDGame game, final int chosenPlayerType) {
        super(game);
        terrain = new Terrain();
        this.chosenPlayerType = chosenPlayerType;

        ripples = new ParticleEffect();
        ripples.load(Gdx.files.internal("fx/ripples.particle"),
                ImageCache.getAtlas());
        ripples.setPosition(getWorldWidth() / 2f, getWorldHeight() / 2f);

        counterImage = new Image();
        winImage = new Image();
        Image restartButton = new Image(ImageCache.getTexture("restart_button"));
        restartButton.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                getGame().setGameScreen(chosenPlayerType);
            }
        });


        Label helpLabel = new Label(Configuration.HELP_TEXT, SkinCache.getDefaultSkin());
        helpLabel.setFontScale(0.5f);

        helpTable = new Table();
        helpTable.setFillParent(true);
        helpTable.center();
        helpTable.top().left();
        helpTable.add(helpLabel).pad(10).left();
        helpTable.row();
        helpTable.add(counterImage).expandX().center().padTop(80);

        finalTable = new Table();
        finalTable.setFillParent(true);
        finalTable.center();
        finalTable.add(winImage).center().padBottom(50);
        finalTable.row();
        finalTable.add(restartButton).center();
        finalTable.setVisible(false);
        stage.addActor(finalTable);
        stage.addActor(helpTable);
    }

    @Override
    public void show() {
        super.show();

        inputMultiplexer = new InputMultiplexer(stage());
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.R || (keycode == Input.Keys.ENTER && isGameFinished)) {
                    getGame().setGameScreen(chosenPlayerType);
                } else if (keycode == Input.Keys.ESCAPE) {
                    getGame().setChooserScreen();
                }
                return super.keyUp(keycode);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);

        generatePlayer(chosenPlayerType);
        generateBots(chosenPlayerType);

        SoundCache.play("countdown");
    }

    @Override
    public void render(float delta) {
        super.render(delta);

//        Gdx.gl20.glClearColor(0.75f, 0.75f, 1, 1);
        Gdx.gl20.glClearColor(Configuration.BKG_R, Configuration.BKG_G, Configuration.BKG_B, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (delay <= DELAY_TIME) {
            delay += delta;
            if (delay >= DELAY_TIME) {
                MusicCache.play("soundtrack");
            }

            int count = (int) (DELAY_TIME - delay);
            String name = "";
            if (count <= 3 && count > 0) {
                name = String.format("number%d", count);
            } else {
                name = "fight";
            }
            counterImage.setDrawable(new TextureRegionDrawable(ImageCache.getTexture(name)));
        } else {
            if (counterImage != null) counterImage.remove();
            update(delta);
        }

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

        ripples.update(delta);

        batch.begin();
        ripples.draw(batch);
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

        stage.draw();
    }

    @Override
    protected void layoutViewsLandscape(int width, int height) {

    }

    @Override
    protected void layoutViewsPortrait(int width, int height) {

    }

    private void generatePlayer(int chosenPlayerType) {
        player = new Player(AnimationCache.PLAYER_ANIMATION.get(chosenPlayerType));
        player.setMovementController(InputController.getInputControllerArrows());
        player.setPlayerEventListener(this);
        float x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape() - player.getWidth());
        float y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape() - player.getHeight() / 2);
        player.setPosition(x, y);
        player.getMovementController().setMovementListener(player);
        inputMultiplexer.addProcessor((InputController) player.getMovementController());
        players.add(player);
    }

    private void generateBots(int chosenPlayerType) {
        int amount = MathUtils.random(2, 10);
        for (int i = 0; i < amount; i++) {
            int type;
            do {
                type = MathUtils.random(0, 3);
            } while(type == chosenPlayerType);
            players.add(generateBot(type));
        }
    }

    private Player generateBot(int type) {
        Player player = new Player(AnimationCache.PLAYER_ANIMATION.get(type));
        player.setMovementController(new AIController(this, player));
        player.getMovementController().setMovementListener(player);
        player.setPlayerEventListener(this);
        float x = MathUtils.random(terrain.getLeftCape(), terrain.getRightCape() - player.getWidth());
        float y = MathUtils.random(terrain.getBottomCape(), terrain.getTopCape()- player.getHeight() / 2);
        player.setPosition(x, y);
        return player;
    }

    private void update(float delta) {
        Iterator<Player> playerIterator = players.iterator();
        while (playerIterator.hasNext()) {
            Player player = playerIterator.next();
            player.setActive(true);
            player.update(delta);
            if (! terrain.contains(player)) {
                if (player.isAlive()) {
                    if (player.getCollisionPoint().y > terrain.getTopCape() || player.getCollisionPoint().x < terrain.getLeftCape()) {
                        player.fall(true);
                    } else if (player.getCollisionPoint().y < terrain.getBottomCape() || player.getCollisionPoint().x > terrain.getRightCape()) {
                        player.fall(false);
                    }
                } else {
                    if (player.getX() > 1500 || player.getX() < -500 || player.getY() < -100) {
                        if (player == this.player) {
                            if (!isGameFinished) winImage.setDrawable(new TextureRegionDrawable(ImageCache.getTexture("game_over")));
                            this.player = null;
                            this.isGameFinished = true;
                            this.finalTable.setVisible(true);
                        }
                        playerIterator.remove();
                    }
                }
            }
        }

        if (players.size() == 1 ) {
            if (player != null && !isGameFinished) {
                winImage.setDrawable(new TextureRegionDrawable(ImageCache.getTexture("nice")));
            }
            players.get(0).onWin();
            isGameFinished = true;
            finalTable.setVisible(true);
        }
    }

    @Override
    public void kick(Player player) {
        for (Player counterPlayer : players) {
            if (counterPlayer != player) {
                if (counterPlayer.isKicked(player.getKickBox())) {
                    counterPlayer.kicked(player.getKickDirection());
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

    @Override
    public void dispose() {
        MusicCache.dispose();
        super.dispose();
    }
}
