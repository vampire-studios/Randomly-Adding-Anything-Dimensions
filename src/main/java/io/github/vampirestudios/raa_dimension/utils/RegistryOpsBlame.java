package io.github.vampirestudios.raa_dimension.utils;

import com.mojang.serialization.DataResult;
import io.github.vampirestudios.raa_dimension.RAADimensionAddon;
import org.apache.logging.log4j.Level;

import java.util.Map;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceLocation;

/* @author - TelepathicGrunt
 *
 * A mixin to make Minecraft actually tell me which
 * datapack json file broke. SPEAK TO ME MINECRAFT!
 *
 * LGPLv3
 */
public class RegistryOpsBlame {

	private static ResourceLocation CURRENT_IDENTIFIER;

	/**
	 * Grabs the current file we are at to pass to next mixin in case file explodes.
	 */
	public static void getCurrentFile(ResourceLocation identifier)
	{
		CURRENT_IDENTIFIER = identifier;
	}

	/**
	 * Checks if the loaded datapack file errored and print it's resource location if it did
	 */
	public static <E> void addBrokenFileDetails(DataResult<MappedRegistry<E>> dataresult)
	{
		if(dataresult.error().isPresent()){
			String brokenJSON = null;
			String reason = null;

			// Attempt to pull the JSON out of the error message if it exists.
			// Has a try/catch in case there's an error message that somehow breaks the string split.
			if(dataresult.error().isPresent()){
				try{
					String[] parsed = dataresult.error().get().message().split(": \\{", 2);
					reason = parsed[0];
					brokenJSON = "{" + parsed[1];
				}
				catch(Exception e){
					try{
						String[] parsed = dataresult.error().get().message().split("\\[", 2);
						reason = parsed[0];
						brokenJSON = "[" + parsed[1];
					}
					catch(Exception e2){
						brokenJSON = "Failed to turn error msg into string. Please notify " +
								"TelepathicGrunt (Blame creator) and show him this message:  \n" + dataresult.error().get().message();
					}
				}
			}

			// gets the hint that might help with the error
			String hint = null;
			if(reason!= null){
				for(Map.Entry<String, String> hints : ErrorHints.HINT_MAP.entrySet()){
					if(reason.contains(hints.getKey())){
						hint = hints.getValue();
						break;
					}
				}
			}
			// default hint that covers most basis.
			if(hint == null){
				hint = "If this is a worldgen JSON file, check out slicedlime's example datapack\n   for worldgen to find what's off about the JSON: https://t.co/cm3pJcAHcy?amp=1";
			}

			RAADimensionAddon.LOGGER.log(Level.ERROR,
					"\n****************** Blame Report ******************"
					+ "\n\n Failed to load resource file: "+ CURRENT_IDENTIFIER
					+ "\n\n Reason stated: " + reason
					+ "\n\n Possibly helpful hint (hopefully): " + hint
					+ "\n\n Prettified form of the broken JSON: \n" + (brokenJSON != null ? PrettyPrintBrokenJSON.prettyPrintJSONAsString(brokenJSON) : " Unable to display JSON. ")
					+ "\n\n"
					);

		}
	}

}