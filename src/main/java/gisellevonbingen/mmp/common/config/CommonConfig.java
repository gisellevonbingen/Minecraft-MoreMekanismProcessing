package gisellevonbingen.mmp.common.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig
{
	public final ModConfigSpec.BooleanValue showOreNotExistRecipes;
	public final ModConfigSpec.ConfigValue<List<? extends String>> recipeOutputPriority;
	public final Map<MaterialType, ModConfigSpec.ConfigValue<Integer>> processingLevels;

	public CommonConfig(ModConfigSpec.Builder builder)
	{
		builder.push("general");

		this.showOreNotExistRecipes = builder.comment("true : show all recipes", "false : hide ore not exist recipes (can hide recipes when some mod not installed)").define("showOreNotExistRecipes", false);

		var modList = Arrays.asList("alltheores", "create", "immersiveengineering");
		builder.comment("Each recipes output item be determined by this list.", "First is Highest.");
		this.recipeOutputPriority = builder.defineList("recipeOutputPriority", modList, s -> true);

		builder.pop();

		builder.comment("processingLevel : set ores processing max level", "    5 : can into up x5 (able all procssing)", "    4 : can into up x4 (disable ore dissolution, slurry crystalizing)", "    3 : can into up x3 (disable injecting)", "    2 : can into up x2 (disable purifying and clumps crushing)", "    1 : reserved (currently same with 0)", "    0 : disable all processing in this mod",
				"disabled processing items are hided your world");
		builder.push("ores");

		this.processingLevels = new HashMap<>();

		for (MaterialType materialType : MaterialType.values())
		{
			builder.push(materialType.getBaseName());
			int processingLevel = 5;

			if (materialType.isRespectMekanism() == true)
			{
				ResourceLocation dustTag = MaterialState.DUST.getStateTagName(materialType);
				builder.comment("exist for modpacks, set greater than 2 to enable this material recipes", "less than or equals 2 is use Mekanism Default Recipes", "    warning : when enabled, ore block can infinitely regenerate using Mekanism Combiner Default Recipe", "    propose remove/override Mekanism Combiner Default Recipe",
						"    e.g.) \"ingredient\":{\"tag\":\"" + dustTag + "\"}},\"amount\":8");
				processingLevel = 0;
			}

			this.processingLevels.put(materialType, builder.defineInRange("processingLevel", processingLevel, 0, 5));

			builder.pop();
		}

		builder.pop();
	}

}
