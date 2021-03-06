package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.mygdx.game.data.ImageCache;
import com.mygdx.game.data.SkinCache;
import com.mygdx.game.data.SoundCache;
import com.mygdx.game.screen.ChooserScreen;
import com.mygdx.game.screen.GameScreen;
import com.mygdx.game.screen.MenuScreen;

public class LDGame extends Game {
	
	@Override
	public void create () {
		ImageCache.load();
		SkinCache.load();
		SoundCache.load();
		Gdx.input.setCatchBackKey(true);
		setMenuScreen();
		//setGameScreen();
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
		if (old != null) {
			old.dispose();
			SoundCache.play("click");
		}
	}

	public void setGameScreen(int chosenPlayerType) {
		setScreen(new GameScreen(this, chosenPlayerType));
	}
	public void setMenuScreen() {
		setScreen(new MenuScreen(this));
	}
	public void setChooserScreen() {
		setScreen(new ChooserScreen(this));
	}
}
