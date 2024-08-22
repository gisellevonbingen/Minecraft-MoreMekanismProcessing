package gisellevonbingen.mmp.common.chemical;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.common.registration.impl.SlurryRegistryObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class MMPChemicals
{
	public static final ChemicalDeferredRegister2 CHEMICALS = new ChemicalDeferredRegister2(MoreMekanismProcessing.MODID);
	public static final Map<MaterialType, SlurryRegistryObject<Chemical, Chemical>> MAP = new HashMap<>();

	public static SlurryRegistryObject<Chemical, Chemical> getSlurryRegistry(MaterialType materialType)
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
				SlurryRegistryObject<Chemical, Chemical> registryObject = CHEMICALS.registerSlurries(materialType, new ChemicalBuildOperator(materialType));
				MAP.put(materialType, registryObject);
			}

		}

	}

	public static final class ChemicalBuildOperator implements UnaryOperator<ChemicalBuilder>
	{
		private final MaterialType materialType;

		private ChemicalBuildOperator(MaterialType materialType)
		{
			this.materialType = materialType;
		}

		@Override
		public ChemicalBuilder apply(ChemicalBuilder builder)
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
