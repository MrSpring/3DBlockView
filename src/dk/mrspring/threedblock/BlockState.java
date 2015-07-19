package dk.mrspring.threedblock;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Konrad on 19-07-2015.
 */
public class BlockState
{
    Block block;
    byte metadata;
    TileEntity tileEntity;

    public BlockState(Block block, byte metadata, TileEntity tileEntity)
    {
        this.block = block;
        this.metadata = metadata;
        this.tileEntity = tileEntity;
    }

    public BlockState(Block block, int metadata, TileEntity tileEntity)
    {
        this(block, (byte) metadata, tileEntity);
    }

    public Block getBlock()
    {
        return block;
    }

    public byte getMetadata()
    {
        return metadata;
    }

    public TileEntity getTileEntity()
    {
        return tileEntity;
    }
}
