package gisellevonbingen.mmp.common.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MMPConfigs
{
	public static final Map<ModConfig.Type, ModConfigSpec> SPECS;

	public static final CommonConfig COMMON;
	public static final ModConfigSpec COMMON_SPEC;

	public static final ClientConfig CLIENT;
	public static final ModConfigSpec CLIENT_SPEC;

	static
	{
		SPECS = new HashMap<>();

		Pair<CommonConfig, ModConfigSpec> common = new ModConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = common.getLeft();
		COMMON_SPEC = common.getRight();
		SPECS.put(ModConfig.Type.COMMON, COMMON_SPEC);

		Pair<ClientConfig, ModConfigSpec> client = new ModConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT = client.getLeft();
		CLIENT_SPEC = client.getRight();
		SPECS.put(ModConfig.Type.CLIENT, CLIENT_SPEC);
	}

	public static void register(ModLoadingContext modLoadingContext)
	{
		register(modLoadingContext, ModConfig.Type.COMMON);

		Dist dist = FMLEnvironment.dist;

		if (dist.isClient())
		{
			register(modLoadingContext, ModConfig.Type.CLIENT);
		}

		if (dist.isDedicatedServer())
		{
			register(modLoadingContext, ModConfig.Type.SERVER);
		}

	}

	public static void register(ModLoadingContext modLoadingContext, ModConfig.Type type)
	{
		ModConfigSpec spec = SPECS.get(type);

		if (spec != null)
		{
			modLoadingContext.getActiveContainer().registerConfig(type, spec);
		}

	}

	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading event)
	{
		parseConfig(event);
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent.Reloading event)
	{
		parseConfig(event);
	}

	public static void parseConfig(ModConfigEvent event)
	{
		if (event.getConfig().getSpec() == CLIENT_SPEC)
		{
			CLIENT.parseConfig();
		}

	}

}
