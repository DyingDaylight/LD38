package com.mygdx.game;

import com.badlogic.gdx.*;
import com.mygdx.game.data.ImageCache;
import com.mygdx.game.data.SkinCache;
import com.mygdx.game.data.SoundCache;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.MenuScreen;

public class LDGame extends Game {
	
	@Override
	public void create () {
		ImageCache.load();
		SkinCache.load();
		SoundCache.load();
		Gdx.input.setCatchBackKey(true);
		//setMenuScreen();
		setGameScreen();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Screen screen = getScreen();
		if (screen != null) screen.dispose();
		super.dispose();
	}

	@Override
	public void setScreen(Screen screen) {
		Screen old = getScreen();
		super.setScreen(screen);
		if (old != null) old.dispose();
	}

	public void setGameScreen() {
		setScreen(new GameScreen(this));

        InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.R) {
                    setGameScreen();
                }
                return super.keyUp(keycode);
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
	}
	public void setMenuScreen() {
		setScreen(new MenuScreen(this));
	}
}
