package com.SoulflameAirhorn;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.SoundEffectPlayed;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import javax.sound.sampled.*;
import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@PluginDescriptor(
	name = "SoulflameAirhorn"
)
public class SoulflameAirhornPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private SoulflameAirhornConfig config;

	private Clip clip;

	private static final int HORN_FILE_COUNT = 3;

	@Provides
	SoulflameAirhornConfig provideConfig(final ConfigManager configManager)
	{
		return configManager.getConfig(SoulflameAirhornConfig.class);
	}

	@Override
	protected void shutDown() throws Exception
	{
		if (clip != null && clip.isOpen())
		{
			clip.close();
		}
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted event)
	{
		if (event.getCommand().equals("horn"))
		{
			playRandomHorn();
		}

		if (event.getCommand().equals("Allhorn"))
		{
			playSequentialHorn();
		}
	}

	@Subscribe
	public void onSoundEffectPlayed(SoundEffectPlayed event)
	{
		if (event.getSoundId() == 10236)
		{
			event.consume();
			playRandomHorn();
		}
	}

	public void playRandomHorn(MenuEntry menuEntry)
	{
		playRandomHorn();
	}

	public void playRandomHorn()
	{
		int random = ThreadLocalRandom.current().nextInt(1, HORN_FILE_COUNT);

		playHorn(random);
	}

	public void playSequentialHorn()
	{
		for (int i = 1; i <= HORN_FILE_COUNT; i++)
		{
			playHorn(i);

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public void playHorn(int index)
	{
		try {
			if (clip != null)
			{
				clip.close();
			}

			AudioInputStream stream = null;
			InputStream is;
			String filename = String.format("/%s.wav", index);

			is = getClass().getResourceAsStream(filename);

			if (is == null) {
				log.debug(String.format("Resource not found: %s", filename));
				return;
			}

			BufferedInputStream bis = new BufferedInputStream(is);

			stream = AudioSystem.getAudioInputStream(bis);
			AudioFormat format = stream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);

			clip.open(stream);

			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float volumeValue = volume.getMinimum() + ((50 + (config.volumeLevel()*5)) * ((volume.getMaximum() - volume.getMinimum()) / 100));

			volume.setValue(volumeValue);

			clip.start();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}
}
