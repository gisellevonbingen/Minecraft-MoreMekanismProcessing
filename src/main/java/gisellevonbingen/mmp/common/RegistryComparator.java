package gisellevonbingen.mmp.common;

import java.util.Comparator;
import java.util.List;

import gisellevonbingen.mmp.common.config.MMPConfigs;
import net.minecraft.core.Registry;

public record RegistryComparator<T>(Registry<T> registry) implements Comparator<T>
{
	@Override
	public int compare(T o1, T o2)
	{
		var key1 = this.registry.getKey(o1);
		var key2 = this.registry.getKey(o2);

		var entries = MMPConfigs.COMMON.recipeOutputPriority.get();
		var index1 = this.getIndex(entries, key1.getNamespace());
		var index2 = this.getIndex(entries, key2.getNamespace());

		if (index1 != index2)
		{
			return Integer.compare(index1, index2);
		}

		return key1.compareNamespaced(key2);
	}

	private int getIndex(List<? extends String> list, String namespace)
	{
		if (namespace.equals(MoreMekanismProcessing.MODID))
		{
			return list.size() + 1;
		}

		var index = list.indexOf(namespace);
		return index == -1 ? list.size() : index;
	}

}
