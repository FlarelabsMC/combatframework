package com.squoshi.combatframework.network;

import com.squoshi.combatframework.CombatFramework;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    public static SimpleChannel INSTANCE;
    private static final String VERSION = "1.0";

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CombatFramework.MOD_ID, "main"), () -> VERSION, VERSION::equals, VERSION::equals);
        INSTANCE.registerMessage(0, NormalAttackPacket.class, NormalAttackPacket::encode, NormalAttackPacket::decode, NormalAttackPacket::handle)
    }
}
