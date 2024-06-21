package gisellevonbingen.mmp.common.slurry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryBuilder;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MMPSlurries
{
	public static final SlurryDeferredRegister2 SLURRIES = new SlurryDeferredRegister2(MoreMekanismProcessing.MODID);
	public static final Map<MaterialType, SlurryRegistryObject<Slurry, Slurry>> MAP = new HashMap<>();

	public static SlurryRegistryObject<Slurry, Slurry> getSlurryRegistry(MaterialType materialType)
	{
		return MAP.get(materialType);
	}

	static
	{
		for (MaterialType materialType : MaterialType.values())
		{
			MaterialState crystal = MaterialState.CRYSTAL;

			if (materialType.getResultShape().canProcess(crystal) == true)
			{
				SlurryRegistryObject<Slurry, Slurry> registryObject = SLURRIES.register(materialType, new SlurryBuildOperator(materialType));
				MAP.put(materialType, registryObject);
			}

		}

	}

	public static final class SlurryBuildOperator implements UnaryOperator<SlurryBuilder>
	{
		private final MaterialType materialType;

		private SlurryBuildOperator(MaterialType materialType)
		{
			this.materialType = materialType;
		}

		@Override
		public SlurryBuilder apply(SlurryBuilder builder)
		{
			TagKey<Item> tag = MaterialState.ORE.getStateTag(this.materialType);
			return builder.ore(tag);
		}

		public MaterialType getOreType()
		{
			return this.materialType;
		}

	}

}
