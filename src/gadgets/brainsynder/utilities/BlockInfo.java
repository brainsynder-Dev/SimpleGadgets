package gadgets.brainsynder.utilities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import simple.brainsynder.nbt.StorageTagCompound;
import simple.brainsynder.wrappers.MaterialWrapper;

public class BlockInfo {
    private StorageTagCompound compound = null;
    private BlockLocation location = null;

    BlockInfo(Block block) {
        compound = new StorageTagCompound();
        compound.setString("type", block.getType().name());
        compound.setByte("data", block.getState().getRawData());
        location = new BlockLocation(block.getLocation());
        compound.setString("location", location.toDataString());
    }
    
    public BlockLocation getLocation() {
        return location;
    }
    
    public StorageTagCompound getCompound() {
        return compound;
    }
    
    void placeOriginal() {
        MaterialWrapper wrapper = MaterialWrapper.fromName(compound.getString("type"));
        byte data = compound.getByte("data");
        if (location != null) {
            Location loc = location.toLocation();
            loc.getBlock().setType(wrapper.toMaterial());
            loc.getBlock().setData(data);
        }
    }
}