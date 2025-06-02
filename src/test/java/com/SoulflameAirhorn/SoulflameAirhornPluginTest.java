package com.SoulflameAirhorn;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SoulflameAirhornPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SoulflameAirhornPlugin.class);
		RuneLite.main(args);
	}
}