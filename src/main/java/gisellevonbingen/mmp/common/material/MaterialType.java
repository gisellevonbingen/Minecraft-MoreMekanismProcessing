package gisellevonbingen.mmp.common.material;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.config.MMPConfigs;
import mekanism.common.registries.MekanismItems;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public enum MaterialType
{
	ALUMINUM(new MaterialTypeBuilder("aluminum").resultShape(MaterialResultShape.INGOT).displayName("Aluminum").color(0xA0C4D8)),
	AMETHYST(new MaterialTypeBuilder("amethyst").resultShape(MaterialResultShape.GEM).displayName("Amethyst").color(0xCC33CC).presetItem(MaterialState.GEM, Items.AMETHYST_SHARD)),
	APATITE(new MaterialTypeBuilder("apatite").resultShape(MaterialResultShape.GEM).displayName("Apatite").color(0x3595B2)),
	AZURESILER(new MaterialTypeBuilder("azure_silver").resultShape(MaterialResultShape.INGOT).displayName("Azure Silver").color(0xCE9CF3)),
	BISMUTH(new MaterialTypeBuilder("bismuth").resultShape(MaterialResultShape.INGOT).displayName("Bismuth").color(0xE8D8E8)),
	BORT(new MaterialTypeBuilder("bort").resultShape(MaterialResultShape.GEM).displayName("Bort").color(0x7495AF)),
	CINNABAR(new MaterialTypeBuilder("cinnabar").resultShape(MaterialResultShape.GEM).displayName("Cinnabar").color(0x990F0F)),
	COBALT(new MaterialTypeBuilder("cobalt").resultShape(MaterialResultShape.INGOT).displayName("Cobalt").color(0x1E66BF)),
	CRIMSONIRON(new MaterialTypeBuilder("crimson_iron").resultShape(MaterialResultShape.INGOT).displayName("Crimson Iron").color(0xF44770)),
	DESH(new MaterialTypeBuilder("desh").resultShape(MaterialResultShape.INGOT).displayName("Desh").color(0xFF9F51)),
	DILITHIUM(new MaterialTypeBuilder("dilithium").resultShape(MaterialResultShape.GEM).displayName("Dilithium").color(0xD8C4C4)),
	DRACONIUM(new MaterialTypeBuilder("draconium").resultShape(MaterialResultShape.INGOT).displayName("Draconium").color(0xA351CC)),
	ELECTROTINE(new MaterialTypeBuilder("electrotine").resultShape(MaterialResultShape.DUST).displayName("Electrotine").color(0x0CB4F8)),
	GREENSAPPHIRE(new MaterialTypeBuilder("green_sapphire").resultShape(MaterialResultShape.GEM).displayName("Green Sapphire").color(0x33CC33)),
	IRIDIUM(new MaterialTypeBuilder("iridium").resultShape(MaterialResultShape.INGOT).displayName("Iridium").color(0xD8D8C4)),
	LITHIUM(new MaterialTypeBuilder("lithium").resultShape(MaterialResultShape.INGOT).displayName("Lithium").color(0x808080).presetItem(MaterialState.DUST, MekanismItems.LITHIUM_DUST)),
	NICKEL(new MaterialTypeBuilder("nickel").resultShape(MaterialResultShape.INGOT).displayName("Nickel").color(0xE5E09E)),
	NITER(new MaterialTypeBuilder("niter").resultShape(MaterialResultShape.GEM).displayName("Niter").color(0xB8AFAF)),
	PERIDOT(new MaterialTypeBuilder("peridot").resultShape(MaterialResultShape.GEM).displayName("Peridot").color(0x66FF66)),
	PLATINUM(new MaterialTypeBuilder("platinum").resultShape(MaterialResultShape.INGOT).displayName("Platinum").color(0xA0F0FF)),
	RUBY(new MaterialTypeBuilder("ruby").resultShape(MaterialResultShape.GEM).displayName("Ruby").color(0xCC3333)),
	SAPPHIRE(new MaterialTypeBuilder("sapphire").resultShape(MaterialResultShape.GEM).displayName("Sapphire").color(0x3333CC)),
	OSTRUM(new MaterialTypeBuilder("ostrum").resultShape(MaterialResultShape.INGOT).displayName("Ostrum").color(0x9F616F)),
	CALORITE(new MaterialTypeBuilder("calorite").resultShape(MaterialResultShape.INGOT).displayName("Calorite").color(0xC44249)),
	SILVER(new MaterialTypeBuilder("silver").resultShape(MaterialResultShape.INGOT).displayName("Silver").color(0xD8E4ED)),
	SULFUR(new MaterialTypeBuilder("sulfur").resultShape(MaterialResultShape.DUST).displayName("Sulfur").color(0xD8CC51).presetItem(MaterialState.DUST, MekanismItems.SULFUR_DUST)),
	TITANIUM(new MaterialTypeBuilder("titanium").resultShape(MaterialResultShape.INGOT).displayName("Titanium").color(0x405060)),
	TUNGSTEN(new MaterialTypeBuilder("tungsten").resultShape(MaterialResultShape.INGOT).displayName("Tungsten").color(0x333333)),
	ZINC(new MaterialTypeBuilder("zinc").resultShape(MaterialResultShape.INGOT).displayName("Zinc").color(0xCCCC8E)),

	COAL(new MaterialTypeBuilder("coal").respect(true).resultShape(MaterialResultShape.DUST).displayName("Coal").color(0x2D2D2D).presetItem(MaterialState.DUST, MekanismItems.COAL_DUST)),
	LAPIS(new MaterialTypeBuilder("lapis").respect(true).resultShape(MaterialResultShape.DUST).displayName("Lapis Lazuli").color(0x2C69C8).presetItem(MaterialState.DUST, MekanismItems.LAPIS_LAZULI_DUST)),
	DIAMOND(new MaterialTypeBuilder("diamond").respect(true).resultShape(MaterialResultShape.DUST).displayName("Diamond").color(0x49EAD6).presetItem(MaterialState.DUST, MekanismItems.DIAMOND_DUST)),
	REDSTONE(new MaterialTypeBuilder("redstone").respect(true).resultShape(MaterialResultShape.DUST).displayName("Redstone").color(0xFF0000).presetItem(MaterialState.DUST, Items.REDSTONE)),
	EMERALD(new MaterialTypeBuilder("emerald").respect(true).resultShape(MaterialResultShape.DUST).displayName("Emerald").color(0x17DA61).presetItem(MaterialState.DUST, MekanismItems.EMERALD_DUST)),
	QUARTZ(new MaterialTypeBuilder("quartz").respect(true).resultShape(MaterialResultShape.DUST).displayName("Nether Quartz").color(0xEBE3DB).presetItem(MaterialState.DUST, MekanismItems.QUARTZ_DUST)),

	// EOL
	;

	private final String baseName;
	private final Map<MaterialState, ResourceLocation> presetItems;
	private final MaterialResultShape resultShape;
	private final String displayName;
	private final int defaultColor;
	private final boolean respectMekanism;

	private MaterialType(MaterialTypeBuilder builder)
	{
		this.baseName = builder.baseName();
		this.resultShape = builder.resultShape();
		this.presetItems = new HashMap<>(builder.presetItems());
		this.displayName = builder.displayName();
		this.defaultColor = builder.color();
		this.respectMekanism = builder.respect();
	}

	public static Optional<MaterialType> find(String name)
	{
		return Arrays.stream(MaterialType.values()).filter(t -> StringUtils.equals(t.getBaseName(), name)).findAny();
	}

	public String getDescriptionId()
	{
		return makeDescriptionId(this.baseName);
	}

	public static String makeDescriptionId(String baseName)
	{
		return Util.makeDescriptionId("materialType", MoreMekanismProcessing.rl(baseName));
	}

	public String getBaseName()
	{
		return this.baseName;
	}

	public Map<MaterialState, ResourceLocation> getPresetItems()
	{
		return new HashMap<>(this.presetItems);
	}

	public ResourceLocation getPresetItem(MaterialState materialState)
	{
		return this.presetItems.get(materialState);
	}

	public MaterialResultShape getResultShape()
	{
		return this.resultShape;
	}

	public String getDisplayName()
	{
		return this.displayName;
	}

	public int getDefaultColor()
	{
		return this.defaultColor;
	}

	public int getColor()
	{
		Integer color = MMPConfigs.CLIENT.parsedColors.get(this);
		return color != null ? color : this.getDefaultColor();
	}

	public boolean isRespectMekanism()
	{
		return this.respectMekanism;
	}

}
