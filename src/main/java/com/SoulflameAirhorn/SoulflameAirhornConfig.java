package com.SoulflameAirhorn;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("SoulflameHorn")
public interface SoulflameAirhornConfig extends Config
{
	@Range(
			min = 1,
			max = 10
	)
	@ConfigItem(
			keyName = "volumeLevel",
			name = "Volume",
			description = "Adjust fart volume",
			position = 8
	)
	default int volumeLevel() {
		return 10;
	}
}
