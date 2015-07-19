package dk.mrspring.threedblock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 19-07-2015.
 */
public class WorldWrapper implements IBlockAccess
{
    BlockState[][][] blocks;
    List<TileEntity> tileEntities;
    public int w, h, d;

    public WorldWrapper(int width, int height, int depth)
    {
        blocks = new BlockState[width][height][depth];
        tileEntities = new ArrayList<TileEntity>();
        w = width;
        h = height;
        d = depth;
    }

    private BlockState getBlockState(int x, int y, int z)
    {
        return isInBounds(x, y, z) ? blocks[x][y][z] : null;
    }

    private boolean isInBounds(int x, int y, int z)
    {
        return (x >= 0 && x < blocks.length) && (y >= 0 && y < blocks[0].length) && (z >= 0 && z < blocks[0][0].length);
    }

    public boolean setBlock(int x, int y, int z, Block block, int metadata)
    {
        if (isInBounds(x, y, z))
        {
            TileEntity tileEntity = block instanceof ITileEntityProvider ? getTileEntity((ITileEntityProvider) block, metadata) : null;
            if (tileEntity != null)
            {
                tileEntity.xCoord = x;
                tileEntity.yCoord = y;
                tileEntity.zCoord = z;
                tileEntity.blockMetadata = metadata;
                tileEntity.blockType = block;
                tileEntities.add(tileEntity);
            }
            BlockState state = new BlockState(block, metadata, tileEntity);
            blocks[x][y][z] = state;
            return true;
        } else return false;
    }

    private TileEntity getTileEntity(ITileEntityProvider provider, int metadata)
    {
        try
        {
            TileEntity tileEntity = provider.createNewTileEntity(null, metadata);
            return tileEntity;
        } catch (NullPointerException e)
        {
            return null;
        }
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        BlockState state = getBlockState(x, y, z);
        return state != null ? state.getBlock() : Blocks.air;
    }

    @Override
    public TileEntity getTileEntity(int x, int y, int z)
    {
        BlockState state = getBlockState(x, y, z);
        return state != null ? state.getTileEntity() : null;
    }

    @Override
    public int getLightBrightnessForSkyBlocks(int p_72802_1_, int p_72802_2_, int p_72802_3_, int p_72802_4_)
    {
        return 0;
    }

    @Override
    public int getBlockMetadata(int x, int y, int z)
    {
        BlockState state = getBlockState(x, y, z);
        return state != null ? state.getMetadata() : 0;
    }

    @Override
    public int isBlockProvidingPowerTo(int p_72879_1_, int p_72879_2_, int p_72879_3_, int p_72879_4_)
    {
        return 0;
    }

    @Override
    public boolean isAirBlock(int x, int y, int z)
    {
        BlockState state = getBlockState(x, y, z);
        return state == null || state.getBlock() == Blocks.air;
    }

    @Override
    public BiomeGenBase getBiomeGenForCoords(int x, int y)
    {
        return BiomeGenBase.plains;
    }

    @Override
    public int getHeight()
    {
        return 128;
    }

    @Override
    public boolean extendedLevelsInChunkCache()
    {
        return false;
    }

    @Override
    public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default)
    {
        return getBlock(x, y, z).isSideSolid(this, x, y, z, side);
    }

    public List<TileEntity> getTileEntities()
    {
        return tileEntities;
    }
}
