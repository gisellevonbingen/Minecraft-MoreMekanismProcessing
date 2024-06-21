package gisellevonbingen.mmp.common.material;

import org.apache.commons.lang3.NotImplementedException;

import gisellevonbingen.mmp.common.MoreMekanismProcessing;
import gisellevonbingen.mmp.common.item.MMPItems;
import gisellevonbingen.mmp.common.tag.MMPTags;
import mekanism.common.tags.MekanismTags;
import net.minecraft.Util;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

public enum MaterialState
{
	ORE(new MaterialStateBuilder("ore", Tags.Items.ORES).hasOwnItem(false)),
	RAW_STORAGE_BLOCKS(new MaterialStateBuilder("raw_storage_blocks", Tags.Items.STORAGE_BLOCKS).tagPrefix("raw_").hasOwnItem(false)),
	RAW_ITEM(new MaterialStateBuilder("raw_ore", Tags.Items.RAW_MATERIALS).hasOwnItem(false)),
	DUST(new MaterialStateBuilder("dust", Tags.Items.DUSTS)),
	DIRTY_DUST(new MaterialStateBuilder("dirty_dust", MekanismTags.Items.DIRTY_DUSTS, commonTag("dirty_dusts"))),
	CLUMP(new MaterialStateBuilder("clump", MekanismTags.Items.CLUMPS, commonTag("clumps"))),
	SHARD(new MaterialStateBuilder("shard", MekanismTags.Items.SHARDS, commonTag("shards"))),
	CRYSTAL(new MaterialStateBuilder("crystal", MekanismTags.Items.CRYSTALS, commonTag("crystals"))),
	INGOT(new MaterialStateBuilder("ingot", Tags.Items.INGOTS)),
	GEM(new MaterialStateBuilder("gem", Tags.Items.GEMS)),
	NUGGET(new MaterialStateBuilder("nugget", Tags.Items.NUGGETS)),;

	private static TagKey<Item> commonTag(String name)
	{
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
	}

	public static class MaterialStateBuilder
	{
		private final String baseName;
		private final TagKey<Item> categoryTag;
		private final TagKey<Item> stateTag;

		private boolean hasOwnItem;
		private String tagPrefix;

		public MaterialStateBuilder(String baseName, TagKey<Item> categoryTag)
		{
			this(baseName, categoryTag, categoryTag);
		}

		public MaterialStateBuilder(String baseName, TagKey<Item> categoryTag, TagKey<Item> stateTag)
		{
			this.baseName = baseName;
			this.categoryTag = categoryTag;
			this.stateTag = stateTag;

			this.hasOwnItem = true;
			this.tagPrefix = "";
		}

		public MaterialStateBuilder hasOwnItem(boolean hasOwnItem)
		{
			this.hasOwnItem = hasOwnItem;
			return this;
		}

		public MaterialStateBuilder tagPrefix(String tagPrefix)
		{
			this.tagPrefix = tagPrefix;
			return this;
		}

	}

	private String baseName;
	private TagKey<Item> categoryTag;
	private TagKey<Item> stateTag;

	private boolean hasOwnItem;
	private String tagPrefix;

	MaterialState(MaterialStateBuilder builder)
	{
		this.baseName = builder.baseName;
		this.categoryTag = builder.categoryTag;
		this.stateTag = builder.stateTag;

		this.hasOwnItem = builder.hasOwnItem;
		this.tagPrefix = builder.tagPrefix;
	}

	public ResourceLocation getStateTagName(MaterialType materialType)
	{
		ResourceLocation stateTagName = this.stateTag.location();
		String tagPrefix = this.getTagPrefix();

		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(stateTagName.getPath());
		pathBuilder.append("/");
		pathBuilder.append(tagPrefix);
		pathBuilder.append(materialType.getBaseName());
		return ResourceLocation.fromNamespaceAndPath(stateTagName.getNamespace(), pathBuilder.toString());
	}

	public TagKey<Item> getStateTag(MaterialType materialType)
	{
		if (this.hasOwnItem() == true)
		{
			return MMPTags.Items.getProcessingItemTag(materialType, this);
		}
		else
		{
			ResourceLocation tagName = this.getStateTagName(materialType);
			return ItemTags.create(tagName);
		}

	}

	public TagKey<Block> getStateBlockTag(MaterialType materialType)
	{
		ResourceLocation tagName = this.getStateTagName(materialType);
		return BlockTags.create(tagName);
	}

	public String getItemNamePath(MaterialType materialType)
	{
		String stateName = this.getBaseName();
		String baseName = materialType.getBaseName();

		if (this == INGOT || this == NUGGET)
		{
			return baseName + "_" + stateName;
		}
		else
		{
			return stateName + "_" + baseName;
		}

	}

	public ResourceLocation getItemName(MaterialType materialType)
	{
		if (this.hasOwnItem() == true)
		{
			return MMPItems.getProcessingItemName(materialType, this);
		}
		else
		{
			throw new NotImplementedException("getItemName(" + materialType.name() + ")");
		}

	}

	public Item getItem(MaterialType materialType)
	{
		if (this.hasOwnItem() == true)
		{
			return MMPItems.getProcessingItem(materialType, this);
		}
		else
		{
			throw new NotImplementedException("getItem(" + materialType.name() + ")");
		}

	}

	public ItemStack getItemStack(MaterialType materialType)
	{
		return new ItemStack(this.getItem(materialType));
	}

	public ItemStack getItemStack(MaterialType materialType, int count)
	{
		return new ItemStack(this.getItem(materialType), count);
	}

	public String getStatedDescriptionId()
	{
		return makeDescriptionId(this.baseName);
	}

	public static String makeDescriptionId(String baseName)
	{
		return Util.makeDescriptionId("statedMaterial", MoreMekanismProcessing.rl(baseName));
	}

	public static Component createTextComponent(String translationKey, String statedDescriptionId, MaterialType materialType)
	{
		if (Language.getInstance().has(translationKey) == true)
		{
			return Component.translatable(translationKey);
		}
		else
		{
			return Component.translatable(statedDescriptionId, Component.translatable(materialType.getDescriptionId()));
		}

	}

	public String getBaseName()
	{
		return this.baseName;
	}

	public TagKey<Item> getCategoryTag()
	{
		return this.categoryTag;
	}

	public boolean hasOwnItem()
	{
		return this.hasOwnItem;
	}

	public String getTagPrefix()
	{
		return this.tagPrefix;
	}

}
