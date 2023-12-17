package com.squoshi.combatframework.client;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class CombatFrameworkKeys {
    public static KeyMapping NORMAL_ATTACK = new KeyMapping("key.combatframework.normalattack", GLFW.GLFW_KEY_E, "key.categories.combatframework.combat");
    public static KeyMapping SPECIAL_ATTACK = new KeyMapping("key.combatframework.specialattack", GLFW.GLFW_KEY_R, "key.categories.combatframework.combat");
    public static KeyMapping HEAVY_ATTACK = new KeyMapping("key.combatframework.heavyattack", GLFW.GLFW_KEY_C, "key.categories.combatframework.combat");

    public static KeyMapping[] getKeys() {
        return new KeyMapping[] {
                NORMAL_ATTACK,
                SPECIAL_ATTACK,
                HEAVY_ATTACK
        };
    }
}
