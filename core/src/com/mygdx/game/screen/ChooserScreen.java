package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.LDGame;
import com.mygdx.game.data.AnimationCache;
import com.mygdx.game.data.Configuration;
import com.mygdx.game.data.ImageCache;
import com.mygdx.game.data.SkinCache;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.StateAnimation;
import com.mygdx.game.ui.AnimatedImage;

/**
 * Created by kettricken on 23.04.2017.
 */
public class ChooserScreen extends BaseScreen {

    private Table table;
    private AnimatedImage animatedImage;
    private int chosenIndex = 0;
    private int maxPlayers = AnimationCache.PLAYER_ANIMATION.size();

    public ChooserScreen(LDGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        StateAnimation animation = AnimationCache.PLAYER_ANIMATION.get(chosenIndex).get(Player.WALK);
        animatedImage = new AnimatedImage(animation);
        Group group = new Group();
        group.addActor(animatedImage);

        final Image start = new Image(ImageCache.getTexture("start_button"));
        start.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                startGame();
            }
        });

        final Image next = new Image(ImageCache.getTexture("right_button"));
        next.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                nextPlayer();
            }
        });

        final Image previous = new Image(ImageCache.getTexture("left_button"));
        previous.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                previousPlayer();
            }
        });

        Label helpLabel = new Label(Configuration.INFO_TEXT, SkinCache.getDefaultSkin());
        helpLabel.setFontScale(0.5f);
        helpLabel.setColor(0.29f, 0.30f, 0.49f, 1);

        Table helpTable = new Table();
        helpTable.setFillParent(true);
        helpTable.center();
        helpTable.top().left();
        helpTable.add(helpLabel).pad(10);
        helpTable.row();

        table = new Table();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("image/cloud.png")))));
        table.setFillParent(true);
        table.add(group).center().expandY().colspan(3);
        table.row();
        table.add(previous).padBottom(100).padRight(30);
        table.add(start).padBottom(100).padRight(30);
        table.add(next).padBottom(100);

        stage().addActor(table);
        stage().addActor(helpTable);

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage());
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                } else if (keycode == Input.Keys.ENTER) {
                    startGame();
                } else if (keycode == Input.Keys.LEFT) {
                    previousPlayer();
                } else if (keycode == Input.Keys.RIGHT) {
                    nextPlayer();
                }
                return super.keyUp(keycode);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void startGame() {
        getGame().setGameScreen(chosenIndex);
    }

    private void previousPlayer() {
        chosenIndex -= 1;
        if (chosenIndex < 0) {
            chosenIndex = maxPlayers - 1;
        }
        redrawPlayer();
    }

    private void nextPlayer() {
        chosenIndex += 1;
        if (chosenIndex >= maxPlayers) {
            chosenIndex = 0;
        }
        redrawPlayer();
    }

    private void redrawPlayer() {
        StateAnimation animation = AnimationCache.PLAYER_ANIMATION.get(chosenIndex).get(Player.WALK);
        animatedImage.setAnimation(animation);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl20.glClearColor(Configuration.BKG_R, Configuration.BKG_G, Configuration.BKG_B, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage().draw();
    }

    @Override
    protected void layoutViewsLandscape(int width, int height) {

    }

    @Override
    protected void layoutViewsPortrait(int width, int height) {

    }
}
