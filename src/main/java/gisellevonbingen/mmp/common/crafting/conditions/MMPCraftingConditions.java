package gisellevonbingen.mmp.common.crafting.conditions;

import com.mojang.serialization.MapCodec;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class MMPCraftingConditions
{
	public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, MoreMekanismProcessing.MODID);
	public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ProcessingLevelCondition>> PROCESSING_LEVEL = CONDITIONS.register("processing_level", () -> ProcessingLevelCondition.CODEC);

}
