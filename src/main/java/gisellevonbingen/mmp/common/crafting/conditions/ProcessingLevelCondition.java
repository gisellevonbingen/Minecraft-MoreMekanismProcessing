package gisellevonbingen.mmp.common.crafting.conditions;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import gisellevonbingen.mmp.common.config.MMPConfigs;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.neoforged.neoforge.common.conditions.ICondition;

public record ProcessingLevelCondition(String materialType, int requireLevel) implements ICondition
{
	public static MapCodec<ProcessingLevelCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(//
			Codec.STRING.fieldOf("materialType").forGetter(ProcessingLevelCondition::materialType), //
			Codec.INT.fieldOf("requireLevel").forGetter(ProcessingLevelCondition::requireLevel))//
			.apply(builder, ProcessingLevelCondition::new));

	@Override
	public boolean test(IContext context)
	{
		Optional<MaterialType> materialType = Arrays.stream(MaterialType.values()).filter(t -> StringUtils.equals(t.getBaseName(), this.materialType)).findFirst();

		if (!materialType.isPresent())
		{
			return true;
		}

		int level = MMPConfigs.COMMON.processingLevels.get(materialType.get()).get();
		return level >= this.requireLevel;
	}

	@Override
	public String toString()
	{
		return "processing_level(\"" + this.materialType + "\" require " + this.requireLevel + ")";
	}

	@Override
	public MapCodec<? extends ICondition> codec()
	{
		return CODEC;
	}

}
