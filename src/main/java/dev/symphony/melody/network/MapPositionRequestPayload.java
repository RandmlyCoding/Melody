package dev.symphony.melody.network;

import dev.symphony.melody.Melody;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.map.MapState;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record MapPositionRequestPayload(MapIdComponent mapIdComponent) implements CustomPayload {
    public static final Id<MapPositionRequestPayload> PACKET_ID = new Id<>(Melody.id("map_position_request"));

    public static final PacketCodec<RegistryByteBuf, MapPositionRequestPayload> PACKET_CODEC = PacketCodec.tuple(
            MapIdComponent.PACKET_CODEC,
            MapPositionRequestPayload::mapIdComponent,
            MapPositionRequestPayload::new
    );


    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(PACKET_ID, PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PACKET_ID, MapPositionRequestPayload::onRequest);
    }

    private static void onRequest(MapPositionRequestPayload payload, ServerPlayNetworking.Context context) {
        MapState mapState = context.server().getOverworld().getMapState(payload.mapIdComponent);
        if (mapState == null) return;

        ServerPlayNetworking.send(context.player(), new MapPositionPayload(payload.mapIdComponent, mapState.centerX, mapState.centerZ));
    }
}
