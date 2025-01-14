package dev.symphony.melody.item.map_book;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.PersistentState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public final class MapBookState extends PersistentState {
    @NotNull
    private final ArrayList<Integer> mapIDs;

    public MapBookState() {
        this.mapIDs = new ArrayList<>();
    }

    @NotNull
    public ArrayList<Integer> getMapIDs() {
        return this.mapIDs;
    }

    public MapBookState(int[] ids) {
        this();
        this.mapIDs.clear();
        this.mapIDs.addAll(Arrays.stream(ids).boxed().toList());
        this.markDirty();
    }

    @Override @NotNull
    public NbtCompound writeNbt(@NotNull NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        if (!this.mapIDs.isEmpty()) {
            nbt.putIntArray("mapIDs", this.mapIDs);
        }

        return nbt;
    }

    @NotNull
    public MapBookState fromNbt(@NotNull NbtCompound nbt) {
        this.mapIDs.clear();
        int[] ids = nbt.getIntArray("mapIDs");
        this.mapIDs.addAll(Arrays.stream(ids).boxed().toList());
        return this;
    }

    public void addMapID(int id) {
        this.mapIDs.add(id);
        this.markDirty();
    }
}
