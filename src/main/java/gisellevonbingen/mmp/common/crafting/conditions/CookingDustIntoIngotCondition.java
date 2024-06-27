package gisellevonbingen.mmp.common.crafting.conditions;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import gisellevonbingen.mmp.common.config.MMPConfigs;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.neoforged.neoforge.common.conditions.ICondition;

public record CookingDustIntoIngotCondition(String materialType) implements ICondition
{
	public static MapCodec<CookingDustIntoIngotCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(//
			Codec.STRING.fieldOf("materialType").forGetter(CookingDustIntoIngotCondition::materialType)) //
			.apply(builder, CookingDustIntoIngotCondition::new));

	@Override
	public boolean test(IContext context)
	{
		Optional<MaterialType> materialType = MaterialType.find(this.materialType);

		if (!materialType.isPresent())
		{
			return true;
		}

		var config = MMPConfigs.COMMON.disableDustCookings.get(materialType.get());

		if (config == null)
		{
			return true;
		}

		var disabled = config.get();
		return disabled == false;
	}

	@Override
	public String toString()
	{
		return "cooking_dust_into_ingot(\"" + this.materialType + ")";
	}

	@Override
	public MapCodec<? extends ICondition> codec()
	{
		return CODEC;
	}

}
