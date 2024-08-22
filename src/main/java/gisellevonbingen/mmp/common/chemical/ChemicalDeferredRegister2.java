package gisellevonbingen.mmp.common.chemical;

import java.util.function.UnaryOperator;

import gisellevonbingen.mmp.common.material.MaterialState;
import gisellevonbingen.mmp.common.material.MaterialType;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import mekanism.common.registration.impl.ChemicalDeferredRegister;
import mekanism.common.registration.impl.DeferredChemical;
import mekanism.common.registration.impl.SlurryRegistryObject;

public class ChemicalDeferredRegister2 extends ChemicalDeferredRegister
{
	public ChemicalDeferredRegister2(String modid)
	{
		super(modid);
	}

	public SlurryRegistryObject<Chemical, Chemical> registerSlurries(MaterialType materialType, UnaryOperator<ChemicalBuilder> builderModifier)
	{
		DeferredChemical<Chemical> dirty = this.register(MMPSlurryBuilder.dirty(), materialType, builderModifier);
		DeferredChemical<Chemical> clean = this.register(MMPSlurryBuilder.clean(), materialType, builderModifier);
		return new SlurryRegistryObject<>(dirty, clean);
	}

	public DeferredChemical<Chemical> register(MMPSlurryBuilder builder, MaterialType materialType, UnaryOperator<ChemicalBuilder> builderModifier)
	{
		builder.materialType(materialType);
		builder.ore(MaterialState.ORE.getStateTag(materialType));
		return this.register(builder, builderModifier);
	}

	public DeferredChemical<Chemical> register(MMPSlurryBuilder builder, UnaryOperator<ChemicalBuilder> builderModifier)
	{
		return this.register(builder.getSlurryName(), () -> new MMPSlurry((MMPSlurryBuilder) builderModifier.apply(builder)));
	}

}
