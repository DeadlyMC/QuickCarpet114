package quickcarpet.skyblock.mixin;

import com.google.common.collect.Maps;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block.Settings;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import quickcarpet.skyblock.SkyBlockSettings;

@Mixin(FlowerPotBlock.class)
public abstract class FlowerPotBlockMixin extends Block {
    
    @Shadow
    @Final
    private static Map<Block, Block> CONTENT_TO_POTTED = Maps.newHashMap();
    private Block content;
    
    protected FlowerPotBlockMixin(Block block_1, Settings block$Settings_1)
    {
        super(block$Settings_1);
        this.content = block_1;
        CONTENT_TO_POTTED.put(block_1, this);
    }
    
    /**
     * @author PR0CESS
     */
   
    @Inject(method = "activate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FlowerPotBlock;activate(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)V"))
    private void doActivate(BlockState blockState_1, World world_1, BlockPos blockPos_1, PlayerEntity playerEntity_1, Hand hand_1, BlockHitResult blockHitResult_1)
    {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        Item item_1 = itemStack_1.getItem();
        Block block_1 = item_1 instanceof BlockItem ? (Block)CONTENT_TO_POTTED.getOrDefault(((BlockItem)item_1).getBlock(), Blocks.AIR) : Blocks.AIR;
        boolean boolean_1 = block_1 == Blocks.AIR;
        boolean boolean_2 = this.content == Blocks.AIR;
        if (boolean_1 != boolean_2) {
            world_1.setChunkForced(blockPos_1.getX(), blockPos_1.getZ(), boolean_2);
        }
    }
    @Inject(method = "onBreak", at = @At(target = "Lnet/minecraft/block/FlowerPotBlock;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"))
    public void onOnBreak(World world_1, BlockPos blockPos_1, BlockState blockState_1, PlayerEntity playerEntity_1) {
        world_1.setChunkForced(blockPos_1.getX(), blockPos_1.getZ(), false);
    }
}