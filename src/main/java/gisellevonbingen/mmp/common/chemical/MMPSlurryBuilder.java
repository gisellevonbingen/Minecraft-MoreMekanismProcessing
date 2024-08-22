package gisellevonbingen.mmp.common.chemical;

import java.util.Objects;

import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.common.Mekanism;
import net.minecraft.resources.ResourceLocation;

public class MMPSlurryBuilder extends ChemicalBuilder
{
	public static final String DIRTY = "dirty";
	public static final String CLEAN = "clean";

	private final String slurryType;

	private MaterialType materialType;

	protected MMPSlurryBuilder(String slurryType, ResourceLocation texture)
	{
		super(texture);

		this.slurryType = slurryType;
	}

	public String slurryType()
	{
		return this.slurryType;
	}

	public MaterialType materialType()
	{
		return this.materialType;
	}

	public void materialType(MaterialType materialType)
	{
		this.materialType = materialType;
	}

	public String getSlurryName()
	{
		return MMPSlurry.getSlurryName(this.slurryType(), this.materialType());
	}

	public static MMPSlurryBuilder dirty()
	{
		return new MMPSlurryBuilder(DIRTY, Mekanism.rl(MMPSlurry.SLURRY + "/" + DIRTY));
	}

	public static MMPSlurryBuilder clean()
	{
		return new MMPSlurryBuilder(CLEAN, Mekanism.rl(MMPSlurry.SLURRY + "/" + CLEAN));
	}

	public static MMPSlurryBuilder builder(String state, ResourceLocation texture)
	{
		return new MMPSlurryBuilder(state, Objects.<ResourceLocation> requireNonNull(texture));
	}

}
