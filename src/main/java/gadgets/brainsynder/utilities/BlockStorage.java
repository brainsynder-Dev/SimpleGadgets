package gadgets.brainsynder.utilities;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BlockStorage {
    private static List<String> locationStorage = new ArrayList<>();
    private LinkedHashMap<String, BlockInfo> compoundList = new LinkedHashMap<>();

    public void addBlock(Block block) {
        String loc = new BlockLocation(block.getLocation()).toDataString();
        if (!locationStorage.contains(loc)) {
            compoundList.put(loc, new BlockInfo(block));
            locationStorage.add(loc);
        }
    }

    public BlockInfo getBlockInfo(Block block) {
        return compoundList.getOrDefault(new BlockLocation(block.getLocation()).toDataString(), new BlockInfo(block));
    }

    public boolean isEmpty () {
        return compoundList.isEmpty();
    }

    public boolean contains (Block block) {
        String loc = new BlockLocation(block.getLocation()).toDataString();
        return locationStorage.contains(loc) || compoundList.containsKey(loc);
    }

    public void reset() {
        if (compoundList.isEmpty()) return;
        compoundList.values().forEach(info -> {
            locationStorage.remove(info.getLocation().toDataString());
            //info.placeOriginal();
            info.getState().update(true);
        });
        compoundList.clear();
    }

    public void reset(Block block) {
        block.getState().getData();
        if (compoundList.isEmpty()) return;
        if (!contains(block)) return;
        BlockInfo save = getBlockInfo(block);
        save.getState().update(true);
        locationStorage.remove(save.getLocation().toDataString());
        compoundList.remove(save.getLocation().toDataString());
    }
}

