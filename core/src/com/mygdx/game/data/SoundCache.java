package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by st on 9/25/14.
 */
public class SoundCache {

    /**
     * TODO: put sounds here as constants with names of mp3 without extension
     */

    private static final String SOUND_DIR = "sfx/";
    private static final String SOUND_EXT = ".wav";

    private static ObjectMap<String, Sound> sounds;

    public static void load() {
        sounds = new ObjectMap<String, Sound>();

        String[] keys = {
                "click",
                "countdown",
                "punch",
                "whoosh"
        };
        for (String key: keys) {
            Sound sound = Gdx.audio.newSound(Gdx.files.internal(SOUND_DIR + key + SOUND_EXT));
            sounds.put(key, sound);
        }
    }

    public static void play(String key) {
        sounds.get(key).play();
    }
}
