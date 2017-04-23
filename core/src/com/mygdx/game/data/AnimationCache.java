package com.mygdx.game.data;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.StateAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kettricken on 22.04.2017.
 */
public class AnimationCache {

    public static final ArrayList<Map<Integer, StateAnimation>> PLAYER_ANIMATION = new ArrayList() {{

             add(new HashMap<Integer, StateAnimation>() {{
                put(Player.STAND, new StateAnimation("person1-walk", 1, 0, Animation.PlayMode.NORMAL));
                put(Player.FALL, new StateAnimation("person1-falling", 0, 0, Animation.PlayMode.NORMAL));
                put(Player.KICKED, new StateAnimation("person1-surprise", 1, 0.2f, Animation.PlayMode.NORMAL));
                put(Player.WALK, new StateAnimation("person1-walk", 2, 0.1f, Animation.PlayMode.LOOP));
                put(Player.KICK, new StateAnimation("person1-kick-right-boot", 1, 0.1f, Animation.PlayMode.NORMAL));
                put(Player.WIN, new StateAnimation("person1-happy", 2, 0.2f, Animation.PlayMode.LOOP));
            }});

            add(new HashMap<Integer, StateAnimation>() {{
                put(Player.STAND, new StateAnimation("person2-walk", 1, 0, Animation.PlayMode.NORMAL));
                put(Player.FALL, new StateAnimation("person2-falling", 0, 0, Animation.PlayMode.NORMAL));
                put(Player.KICKED, new StateAnimation("person2-surprise", 1, 0.2f, Animation.PlayMode.NORMAL));
                put(Player.WALK, new StateAnimation("person2-walk", 2, 0.1f, Animation.PlayMode.LOOP));
                put(Player.KICK, new StateAnimation("person2-kick-right-boot", 1, 0.1f, Animation.PlayMode.NORMAL));
                put(Player.WIN, new StateAnimation("person2-happy", 2, 0.2f, Animation.PlayMode.LOOP));
            }});

            add(new HashMap<Integer, StateAnimation>() {{
                put(Player.STAND, new StateAnimation("person3-walk", 1, 0, Animation.PlayMode.NORMAL));
                put(Player.FALL, new StateAnimation("person3-falling", 0, 0, Animation.PlayMode.NORMAL));
                put(Player.KICKED, new StateAnimation("person3-surprise", 1, 0.2f, Animation.PlayMode.NORMAL));
                put(Player.WALK, new StateAnimation("person3-walk", 2, 0.1f, Animation.PlayMode.LOOP));
                put(Player.KICK, new StateAnimation("person3-kick-right-boot", 1, 0.1f, Animation.PlayMode.NORMAL));
                put(Player.WIN, new StateAnimation("person3-happy", 2, 0.2f, Animation.PlayMode.LOOP));
            }});

            add(new HashMap<Integer, StateAnimation>() {{
                put(Player.STAND, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
                put(Player.FALL, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
                put(Player.KICKED, new StateAnimation("fish", 1, 0.2f, Animation.PlayMode.LOOP));
                put(Player.WALK, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
                put(Player.KICK, new StateAnimation("fish-kick-right", 1, 0.1f, Animation.PlayMode.LOOP));
                put(Player.WIN, new StateAnimation("fish", 3, 0.1f, Animation.PlayMode.LOOP));
            }});
        }};
}
