package dk.mrspring.threedblock;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Konrad on 19-07-2015.
 */
public class GuiScreen3DBlockView extends GuiScreen
{
    double rotateY = 0, rotateX = 90 - 25;
    WorldWrapper worldWrapper;
    RenderBlocks renderer;
    boolean dragging = false;
    boolean autoRotate = true;
    int dragStartX = 0, dragStartY = 0;
    private final ResourceLocation BLOCK_ATLAS = new ResourceLocation("minecraft", "textures/atlas/blocks.png");

    @Override
    public void initGui()
    {
        super.initGui();

//        SchematicBuilding building = new SchematicBuilding("MCA").addBottomSchematic(new Schematic("MCA"));
        /*Schematic building = new Schematic("MCA");
        worldWrapper = new WorldWrapper(building.width, 256, building.length);
        int x = 0, y = 0, z = 0;
        for (int i = 0; i < building.blocks.length; i++)
        {
            byte block = building.blocks[i];

            Block blockBlock = Block.getBlockById((int) block);
            if (blockBlock != null && blockBlock != Blocks.air)
                worldWrapper.setBlock(x, y, z, blockBlock, 0);

            x++;
            if (x >= building.width)
            {
                x = 0;
                z++;
                if (z >= building.length)
                {
                    z = 0;
                    y++;
                }
            }
        }*/

//        Schematic schematic = new Schematic("MCA");
//        worldWrapper = new WorldWrapper(schematic.width, schematic.height, schematic.length);
//        byte[] blocks = schematic.blocks;
//        for (int i = 0; i < blocks.length;i++)
//        {
//            int x = i;
//        }
        worldWrapper = new WorldWrapper(10, 128, 10);
        for (int x = 0; x < worldWrapper.w; x++)
            for (int z = 0; z < worldWrapper.d; z++) worldWrapper.setBlock(x, 0, z, Blocks.stone, 0);

        for (int y = 0; y < 4; y++)
        {
            for (int i = 0; i < 8; i++) worldWrapper.setBlock(1 + i, 1 + y, 1, Blocks.brick_block, 0);
            for (int i = 0; i < 8; i++) worldWrapper.setBlock(1 + i, 1 + y, 8, Blocks.brick_block, 0);
            for (int i = 0; i < 6; i++) worldWrapper.setBlock(1, 1 + y, 2 + i, Blocks.brick_block, 0);
            for (int i = 0; i < 6; i++) worldWrapper.setBlock(8, 1 + y, 2 + i, Blocks.brick_block, 0);
        }

        worldWrapper.setBlock(2, 2, 1, Blocks.stained_glass_pane, 0);
        worldWrapper.setBlock(3, 2, 1, Blocks.stained_glass_pane, 1);

        worldWrapper.setBlock(6, 2, 1, Blocks.stained_glass_pane, 2);
        worldWrapper.setBlock(7, 2, 1, Blocks.stained_glass_pane, 3);

        worldWrapper.setBlock(2, 1, 2, Blocks.chest, 1);
        worldWrapper.setBlock(2, 1, 3, Blocks.enchanting_table, 0);
        worldWrapper.setBlock(2, 1, 4, Blocks.fire, 0);

        renderer = new RenderBlocks(worldWrapper);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int p_73864_3_)
    {
        super.mouseClicked(mouseX, mouseY, p_73864_3_);

        dragStartX = mouseX;
        dragStartY = mouseY;
        dragging = true;
        autoRotate = false;
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseMovedOrUp(mouseX, mouseY, mouseButton);
        if (mouseButton != -1) dragging = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_)
    {
        super.drawScreen(mouseX, mouseY, p_73863_3_);

        if (autoRotate)
        {
            rotateY += p_73863_3_;
            if (rotateY >= 360) rotateY -= 360;
        }

        if (dragging)
        {
            float diffY = mouseX - dragStartX;
//            diffY *= mouseSpeedMultiplier;
            rotateY += diffY;
            if (rotateY >= 360) rotateY -= 360;
            else if (rotateY < 0) rotateY += 360;
            dragStartX = mouseX;

            float diffX = mouseY - dragStartY;
//            diffX *= mouseSpeedMultiplier;
            rotateX -= diffX;
            if (rotateX > 180) rotateX = 180;
            else if (rotateX < 0) rotateX = 0;
            dragStartY = mouseY;
        }

//        rotateX=90-25;

        drawDefaultBackground();

//        drawCenteredString(mc.fontRenderer, "Cusomize Wasteland World!", width / 2, 20, 0xFFFFFF);

//        GLClippingPlanes.glEnableClipping(30, width - 30, 20, height - 20);

        GL11.glPushMatrix();

        GL11.glTranslatef(width / 2, height / 2, 0);
//        GL11.glRotatef(180, 1, 0, 0);

        GL11.glRotatef((float) rotateX - 90, 1, 0, 0);
        GL11.glRotatef((float) rotateY, 0, 1, 0);
        GL11.glTranslatef((worldWrapper.w * 16 / 2), 0, (worldWrapper.d * 16 / 2));

        GL11.glPushMatrix();

        float s = -16F;
        GL11.glScalef(s, s, s);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
        if (this.mc.gameSettings.ambientOcclusion != 0)
            GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        for (int x = 0; x < worldWrapper.w; x++)
            for (int y = 0; y < worldWrapper.h; y++)
                for (int z = 0; z < worldWrapper.d; z++)
                {
                    Block block = worldWrapper.getBlock(x, y, z);
                    drawBlock(block, x, y, z);
                }
        tessellator.draw();
        for (TileEntity tileEntity : worldWrapper.getTileEntities())
        {
            if (tileEntity != null)
            {
                int x = tileEntity.xCoord, y = tileEntity.yCoord, z = tileEntity.zCoord;
                TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity, x, y, z, 0.0F);
            }
        }
//        drawBlock(Blocks.anvil, 0, 0, 0);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        GL11.glPopMatrix();
        GL11.glPopMatrix();
//        GLClippingPlanes.glDisableClipping();
//        renderer.renderBlockByRenderType(worldWrapper.getBlock(0, 0, 0), 0, 0, 0);

//        GL11.glTranslatef(-8F, -8F, -8F);

//        System.out.println(rotateY);

//        renderer.renderMinX = 0;
//        renderer.renderMinY = 0;
//        renderer.renderMinZ = 0;
//        renderer.renderMaxX = 16;
//        renderer.renderMaxY = 16;
//        renderer.renderMaxZ = 16;
//        renderer.renderFromInside = false;
//        renderer.renderAllFaces = true;
//        renderer.enableAO = false;
//        renderer.renderMinX = 0D;
//        renderer.renderMinY = 0D;
//        renderer.renderMinZ = 0D;
//        renderer.renderMaxX = 1D;
//        renderer.renderMaxY = 1D;
//        renderer.renderMaxZ = 1D;
//        renderer.renderBlockByRenderType(Blocks.anvil, 0, 0, 0);
        /*double p_147806_2_ = 0;
        double p_147806_4_ = 0;
        double p_147806_6_ = 0;
        IIcon p_147806_8_ = Blocks.stone.getIcon(0, 0);

        Tessellator tessellator = Tessellator.instance;

//        if (this.hasOverrideBlockTexture())
//        {
//            p_147806_8_ = this.overrideBlockTexture;
//        }

        double d3 = (double) p_147806_8_.getInterpolatedU(renderer.renderMinX * 16.0D);
        double d4 = (double) p_147806_8_.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double d5 = (double) p_147806_8_.getInterpolatedV(renderer.renderMinZ * 16.0D);
        double d6 = (double) p_147806_8_.getInterpolatedV(renderer.renderMaxZ * 16.0D);

        if (renderer.renderMinX < 0.0D || renderer.renderMaxX > 1.0D)
        {
            d3 = (double) p_147806_8_.getMinU();
            d4 = (double) p_147806_8_.getMaxU();
        }

        if (renderer.renderMinZ < 0.0D || renderer.renderMaxZ > 1.0D)
        {
            d5 = (double) p_147806_8_.getMinV();
            d6 = (double) p_147806_8_.getMaxV();
        }

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;*/

        /*if (this.uvRotateTop == 1)
        {
            d3 = (double) p_147806_8_.getInterpolatedU(this.renderMinZ * 16.0D);
            d5 = (double) p_147806_8_.getInterpolatedV(16.0D - this.renderMaxX * 16.0D);
            d4 = (double) p_147806_8_.getInterpolatedU(this.renderMaxZ * 16.0D);
            d6 = (double) p_147806_8_.getInterpolatedV(16.0D - this.renderMinX * 16.0D);
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateTop == 2)
        {
            d3 = (double) p_147806_8_.getInterpolatedU(16.0D - this.renderMaxZ * 16.0D);
            d5 = (double) p_147806_8_.getInterpolatedV(this.renderMinX * 16.0D);
            d4 = (double) p_147806_8_.getInterpolatedU(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) p_147806_8_.getInterpolatedV(this.renderMaxX * 16.0D);
            d7 = d4;
            d8 = d3;
            d3 = d4;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateTop == 3)
        {
            d3 = (double) p_147806_8_.getInterpolatedU(16.0D - this.renderMinX * 16.0D);
            d4 = (double) p_147806_8_.getInterpolatedU(16.0D - this.renderMaxX * 16.0D);
            d5 = (double) p_147806_8_.getInterpolatedV(16.0D - this.renderMinZ * 16.0D);
            d6 = (double) p_147806_8_.getInterpolatedV(16.0D - this.renderMaxZ * 16.0D);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }*/

        /*double d11 = p_147806_2_ + renderer.renderMinX;
        double d12 = p_147806_2_ + renderer.renderMaxX;
        double d13 = p_147806_4_ + renderer.renderMaxY;
        double d14 = p_147806_6_ + renderer.renderMinZ;
        double d15 = p_147806_6_ + renderer.renderMaxZ;

        if (renderer.renderFromInside)
        {
            d11 = p_147806_2_ + renderer.renderMaxX;
            d12 = p_147806_2_ + renderer.renderMinX;
        }*/

//        if (this.enableAO)
//        {
//            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
//            tessellator.setBrightness(this.brightnessTopLeft);
//            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
//            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
//            tessellator.setBrightness(this.brightnessBottomLeft);
//            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
//            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
//            tessellator.setBrightness(this.brightnessBottomRight);
//            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
//            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
//            tessellator.setBrightness(this.brightnessTopRight);
//            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
//        }
//        else
        /*d11 *= 16;
        d12 *= 16;
        d13 *= 16;
        d14 *= 16;
        d15 *= 16;
        tessellator.startDrawingQuads();
        {
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        }
        tessellator.draw();*/

//        renderer.renderFaceXPos(Blocks.stone, 0, 0, 0, Blocks.stone.getIcon(0, 0));
//        renderer.renderFaceXNeg(Blocks.stone, 0, 0, 0, Blocks.stone.getIcon(0, 0));
//        renderer.renderFaceYPos(Blocks.stone, 0, 0, 0, Blocks.stone.getIcon(0, 0));
//        renderer.renderFaceYNeg(Blocks.stone, 0, 0, 0, Blocks.stone.getIcon(0, 0));
//        renderer.renderFaceZPos(Blocks.stone, 0, 0, 0, Blocks.stone.getIcon(0, 0));
//        renderer.renderFaceZNeg(Blocks.stone, 0, 0, 0, Blocks.stone.getIcon(0, 0));
//        renderer.setRenderBounds(0, 0, 0, 16, 16, 16);
//        renderer.renderFaceYPos(null, 0, 0, 0, Blocks.anvil.getIcon(0, 0));

//        drawBlock(new ResourceLocation("minecraft", "textures/blocks/bedrock.png"), true, true, false, true, true, true);
//        GL11.glTranslatef(16, 0, 0);
//        drawBlock(new ResourceLocation("minecraft", "textures/blocks/bedrock.png"), true, true, false, false, true, true);
//        GL11.glTranslatef(16, 0, 0);
//        drawBlock(new ResourceLocation("minecraft", "textures/blocks/bedrock.png"), true, true, true, false, true, true);

        /*

        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + h), (double)this.zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + w), (double)(p_73729_2_ + h), (double)this.zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + p_73729_6_) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + w), (double)(p_73729_2_ + 0), (double)this.zLevel, (double)((float)(p_73729_3_ + p_73729_5_) * f), (double)((float)(p_73729_4_ + 0) * f1));
        tessellator.addVertexWithUV((double)(p_73729_1_ + 0), (double)(p_73729_2_ + 0), (double)this.zLevel, (double)((float)(p_73729_3_ + 0) * f), (double)((float)(p_73729_4_ + 0) * f1));

         */

//        float s = 16F;
//        GL11.glScalef(s, s, s);
//        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
//
//        renderer.renderBlockAsItem(Blocks.anvil, 0, 1F);
        /*EntityLivingBase p_147046_5_ = new EntityChicken(null);
        int p_147046_0_ = 51, p_147046_1_ = 75, p_147046_2_ = 30;
        float p_147046_3_ = p_147046_0_ - width, p_147046_4_ = 75 - 50 - height;

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float) p_147046_0_, (float) p_147046_1_, 50.0F);
        GL11.glScalef((float) (-p_147046_2_), (float) p_147046_2_, (float) p_147046_2_);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = p_147046_5_.renderYawOffset;
        float f3 = p_147046_5_.rotationYaw;
        float f4 = p_147046_5_.rotationPitch;
        float f5 = p_147046_5_.prevRotationYawHead;
        float f6 = p_147046_5_.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float) Math.atan((double) (p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float) Math.atan((double) (p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GL11.glTranslatef(0.0F, p_147046_5_.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        p_147046_5_.renderYawOffset = f2;
        p_147046_5_.rotationYaw = f3;
        p_147046_5_.rotationPitch = f4;
        p_147046_5_.prevRotationYawHead = f5;
        p_147046_5_.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

        GL11.glPopMatrix();*/
    }

    private void drawBlock(Block block, int x, int y, int z)
    {
        mc.getTextureManager().bindTexture(BLOCK_ATLAS/*new ResourceLocation("minecraft", "textures/blocks/bedrock.png")*/);
        renderer.setRenderFromInside(false);
//        renderer.
        renderer.renderBlockByRenderType(block, x, y, z);
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.addTranslation(x, y, z);
//        tessellator.setColorRGBA_F(1, 1, 1, 1);
//        tessellator.setBrightness(200);
//        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
//        tessellator.setColorRGBA_F(1, 0, 0, 1);
//        tessellator.addVertexWithUV(1, 0, 0, 1, 0);
//        tessellator.setColorRGBA_F(0, 1, 0, 1);
//        tessellator.addVertexWithUV(1, 1, 0, 1, 1);
//        tessellator.setColorRGBA_F(0, 0, 1, 1);
//        tessellator.addVertexWithUV(0, 1, 0, 0, 1);
//        tessellator.draw();
//        tessellator.addTranslation(-x, -y, -z);
//        tessellator.startDrawingQuads();
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.setNormal(0,1,0);
//        tessellator.setColorRGBA_F(1F, 1F, 1F, 0.9F);
//        tessellator.addVertexWithUV(0, 1, 1, 1, 0);
//        tessellator.setColorRGBA_F(1F, 1F, 1F, 0.75F);
//        tessellator.addVertexWithUV(1, 1, 1, 1, 1);
//        tessellator.setColorRGBA_F(1F, 1F, 1F, 0.5F);
//        tessellator.addVertexWithUV(1, 1, 0, 0, 1);
//        tessellator.setColorRGBA_F(1F, 1F, 1F, 0.25F);
//        tessellator.addVertexWithUV(0, 1, 0, 0, 0);
//        tessellator.draw();
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();

        /*if (autoRotate)
        {
            rotateY++;
            if (rotateY == 360) rotateY = 0;
        }*/
//        rotateY++;
//        if (rotateY == 360) rotateY = 0;
    }
}
