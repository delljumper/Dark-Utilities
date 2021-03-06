package net.darkhax.darkutils.features.antislime;

import net.darkhax.bookshelf.util.OreDictUtils;
import net.darkhax.darkutils.DarkUtils;
import net.darkhax.darkutils.features.DUFeature;
import net.darkhax.darkutils.features.Feature;
import net.darkhax.darkutils.features.material.FeatureMaterial;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@DUFeature(name = "Anti Slime Block", description = "Undo slime chunks")
public class FeatureAntiSlime extends Feature {

    public static Block blockAntiSlime;

    @Override
    public void onPreInit () {

        blockAntiSlime = DarkUtils.REGISTRY.registerBlock(new BlockAntiSlime(), "anti_slime");
        GameRegistry.registerTileEntity(TileEntityAntiSlime.class, "anti_slime");
    }

    @Override
    public void onPreRecipe () {

        DarkUtils.REGISTRY.addShapedRecipe("antislime", new ItemStack(blockAntiSlime), "sws", "wbw", "sws", 's', OreDictUtils.STONE, 'w', Blocks.COBBLESTONE_WALL, 'b', new ItemStack(FeatureMaterial.itemMaterial, 1, 2));
    }

    @Override
    public boolean usesEvents () {

        return true;
    }

    @SubscribeEvent
    public void checkSpawn (EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntitySlime && !event.getEntity().hasCustomName()) {

            for (final TileEntity tile : event.getWorld().loadedTileEntityList) {

                if (tile instanceof TileEntityAntiSlime && ((TileEntityAntiSlime) tile).shareChunks((EntityLivingBase) event.getEntity())) {

                    if (event.getWorld().isBlockPowered(tile.getPos())) {
                        continue;
                    }

                    event.setCanceled(true);
                    event.getEntity().setDead();
                    break;
                }
            }
        }
    }
}
