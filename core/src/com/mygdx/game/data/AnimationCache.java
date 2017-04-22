package com.mygdx.game.data;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.StateAnimation;

import java.util.HashMap;

/**
 * Created by kettricken on 22.04.2017.
 */
public class AnimationCache {

    public static final HashMap<Integer, StateAnimation> FISH_ANIMATION = new HashMap<Integer, StateAnimation>() {{
        put(Player.STAND, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.FALL, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.KICKED, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.WALK, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.KICK, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
    }};

    public static final HashMap<Integer, StateAnimation> PLAYER1_ANIMATION = new HashMap<Integer, StateAnimation>() {{
        put(Player.STAND, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.FALL, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.KICKED, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.WALK, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
        put(Player.KICK, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
    }};

    public static final HashMap<Integer, StateAnimation> PLAYER2_ANIMATION = new HashMap<Integer, StateAnimation>() {{
        put(Player.STAND, new StateAnimation("person2-walk", 1, 0, Animation.PlayMode.NORMAL));
        put(Player.FALL, new StateAnimation("person2-falling", 0, 0, Animation.PlayMode.NORMAL));
        put(Player.KICKED, new StateAnimation("person2-surprise", 1, 0.1f, Animation.PlayMode.NORMAL));
        put(Player.WALK, new StateAnimation("person2-walk", 2, 0.1f, Animation.PlayMode.LOOP));
        put(Player.KICK, new StateAnimation("person2-kick-left-boot", 1, 0.1f, Animation.PlayMode.NORMAL));
    }};
}
