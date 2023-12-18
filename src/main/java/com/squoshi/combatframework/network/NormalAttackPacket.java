package com.squoshi.combatframework.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class NormalAttackPacket {
    private static final Logger LOGGER = LogManager.getLogger("Combat Framework");

    private final UUID entityId;
    private final UUID playerId;
    private final int attack;

    public NormalAttackPacket(UUID entityId, UUID playerId, int attack) {
        this.entityId = entityId;
        this.playerId = playerId;
        this.attack = attack;
    }

    public static void encode(final NormalAttackPacket msg, final FriendlyByteBuf buf) {
        buf.writeUUID(msg.entityId);
        buf.writeUUID(msg.playerId);
        buf.writeInt(msg.attack);
    }

    public static NormalAttackPacket decode(FriendlyByteBuf buf) {
        return new NormalAttackPacket(buf.readUUID(), buf.readUUID(), buf.readInt());
    }


    public static void handle(final NormalAttackPacket msg, final Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context ctx = contextSupplier.get();
        if (ctx.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            ctx.enqueueWork(() -> {
                ServerPlayer player = ctx.getSender();
                if (player != null) {
                    HitResult result = ProjectileUtil.getEntityHitResult(player.level(), player, new Vec3(player.getX(), player.getY(), player.getZ()), new Vec3(player.getX() + player.getLookAngle().x * 4.0D, player.getY() + player.getLookAngle().y * 4.0D, player.getZ() + player.getLookAngle().z * 4.0D), new AABB(player.getX() - 4.0D, player.getY() - 4.0D, player.getZ() - 4.0D, player.getX() + 4.0D, player.getY() + 4.0D, player.getZ() + 4.0D), (entity) -> !entity.isSpectator() && entity instanceof LivingEntity);
                    assert result != null;
                    List<Entity> entities = player.level().getEntities(player, new AABB(result.getLocation().x - 0.1, result.getLocation().y - 0.1, result.getLocation().z - 0.1, result.getLocation().x + 0.1, result.getLocation().y + 0.1, result.getLocation().z + 0.1), (ent) -> ent instanceof LivingEntity);
                    entities.forEach(player::attack);
                }
            });
        } else {
            LOGGER.error("NormalAttackPacket received on the wrong side. This shouldn't happen! Please report to the mod author.");
        }
        ctx.setPacketHandled(true);
    }
}
