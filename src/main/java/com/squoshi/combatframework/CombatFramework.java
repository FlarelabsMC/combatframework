package com.squoshi.combatframework;

import com.squoshi.combatframework.client.CombatFrameworkKeys;
import com.squoshi.combatframework.network.AttackPacket;
import com.squoshi.combatframework.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod("combatframework")
public class CombatFramework {
    private static final Logger LOGGER = LogManager.getLogger("Combat Framework");

    public static final String MOD_ID = "combatframework";

    public CombatFramework() {
        PacketHandler.registerMessages();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeyMappings);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
    }

    private void processIMC(final InterModProcessEvent event) {
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(this::keyInputEvent);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        Arrays.stream(CombatFrameworkKeys.getKeys()).toList().forEach(event::register);
    }

    public void onClientTick(TickEvent.ClientTickEvent event) {

    }

    public void keyInputEvent(InputEvent event) {
        if (CombatFrameworkKeys.NORMAL_ATTACK.consumeClick()) {
            double x = Minecraft.getInstance().player.getX();
            double y = Minecraft.getInstance().player.getY();
            double z = Minecraft.getInstance().player.getZ();
            for (Entity entity : Minecraft.getInstance().level.getEntities(Minecraft.getInstance().player, new AABB(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5))) {
                if (entity instanceof LivingEntity && entity.isAlive() && entity != Minecraft.getInstance().player) {
                    LOGGER.info(entity);
                    PacketHandler.INSTANCE.sendToServer(new AttackPacket(Constants.AttackType.NORMAL_ATTACK_ID));
                }
            }
        }
    }
}
