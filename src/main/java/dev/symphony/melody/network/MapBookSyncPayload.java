package dev.symphony.melody.network;

import dev.symphony.melody.Melody;
import dev.symphony.melody.item.map_book.MapBookItem;
import dev.symphony.melody.item.map_book.MapBookState;
import dev.symphony.melody.item.map_book.MapBookStateManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public record MapBookSyncPayload(int bookID, int[] mapIDs) implements CustomPayload {
    public static final Id<MapBookSyncPayload> PACKET_ID = new Id<>(Melody.id("map_book_sync"));

    public static final PacketCodec<RegistryByteBuf, MapBookSyncPayload> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.VAR_INT,
            MapBookSyncPayload::bookID,
            IntArray.ARRAY_CODEC,
            MapBookSyncPayload::mapIDs,
            MapBookSyncPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void register() {
        PayloadTypeRegistry.playS2C().register(PACKET_ID, PACKET_CODEC);
    }

    @Nullable
    public static MapBookSyncPayload of(ServerPlayerEntity player, ItemStack itemStack) {
        Integer bookId = ((MapBookItem)itemStack.getItem()).getMapBookId(itemStack);
        if (bookId == null) return null;

        MapBookState mapBookState = MapBookStateManager.INSTANCE.getMapBookState(player.server, bookId);
        if (mapBookState != null) {
            return new MapBookSyncPayload(bookId, mapBookState.getMapIDs().stream().mapToInt(i -> i).toArray());
        }
        return null;
    }
}
