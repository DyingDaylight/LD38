package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.LDGame;
import com.mygdx.game.data.Configuration;

/**
 * Created by kettricken on 21.04.2017.
 */
public class MenuScreen extends BaseScreen {

    private Table table;

    public MenuScreen(LDGame game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        final Image title = new Image(new Texture(Gdx.files.internal("image/title.png")));
        title.addListener(new ClickListener(){
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                getGame().setChooserScreen();
            }
        });

        table = new Table();
        table.left().top();
        table.setFillParent(true);
        table.add(title).center();
        stage().addActor(table);

        InputMultiplexer inputMultiplexer = new InputMultiplexer(stage());
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                } else if (keycode == Input.Keys.ENTER) {
                    getGame().setChooserScreen();
                }
                return super.keyUp(keycode);
            }
        });
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        Gdx.gl20.glClearColor(Configuration.BKG_R, Configuration.BKG_G, Configuration.BKG_B, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage().draw();
    }

    @Override
    protected void layoutViewsLandscape(int width, int height) {

    }

    @Override
    protected void layoutViewsPortrait(int width, int height) {

    }
}
