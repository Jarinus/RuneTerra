package com.runeterra;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.runeterra.cache.CacheLoader;
import com.runeterra.cache.impl.definitions.ItemDefinition;
import com.runeterra.cache.impl.definitions.NpcDefinition;
import com.runeterra.cache.impl.definitions.ObjectDefinition;
import com.runeterra.cache.impl.definitions.ShopDefinition;
import com.runeterra.cache.impl.definitions.WeaponInterfaces;
import com.runeterra.engine.GameEngine;
import com.runeterra.engine.task.impl.CombatPoisonEffect.CombatPoisonData;
import com.runeterra.net.NetworkConstants;
import com.runeterra.net.channel.ChannelPipelineHandler;
import com.runeterra.util.ShutdownHook;
import com.runeterra.world.collision.region.RegionClipping;
import com.runeterra.world.content.clan.ClanChatManager;
import com.runeterra.world.model.dialogue.DialogueManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import jaggrab.Jaggrab;

public class RuneTerra {

	/**
	 * The game engine, executed by {@link ScheduledExecutorService}. The game
	 * engine's cycle rate is normally 600 ms.
	 */
	private static final GameEngine engine = new GameEngine();

	/**
	 * The cache loader. Loads the client's cache files. Used for definitions,
	 * clipping, etc..
	 */
	private static final CacheLoader cacheLoader = new CacheLoader();

	/**
	 * Is the server currently updating?
	 */
	private static boolean updating;

	/**
	 * The main logger.
	 */
	private static final Logger logger = Logger.getLogger("RuneTerra");

	public static void main(String[] params) {
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		try {
			logger.info("Initializing the game...");

			final ExecutorService serviceLoader = Executors
					.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());

			// Load the cache..
			serviceLoader.execute(() -> {
				try {
					cacheLoader.init();

					// JAGGRAB
					if (GameConstants.JAGGRAB_ENABLED) {
						new Jaggrab().init();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			// DEFINITIONS
			logger.info("Loading definitions...");
			serviceLoader.execute(() -> ItemDefinition.init());
			serviceLoader.execute(() -> NpcDefinition.init());
			serviceLoader.execute(() -> ObjectDefinition.init());
			serviceLoader.execute(() -> RegionClipping.init());
			serviceLoader.execute(() -> ObjectDefinition.parseObjects().load());
			serviceLoader.execute(() -> ShopDefinition.parseShops().load());
			serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
			serviceLoader.execute(() -> DialogueManager.parseDialogues().load());

			// OTHERS
			serviceLoader.execute(() -> ClanChatManager.init());
			serviceLoader.execute(() -> CombatPoisonData.init());

			// Shutdown the loader
			serviceLoader.shutdown();

			// Make sure the loader is properly shut down
			if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
				throw new IllegalStateException("The background service load took too long!");

			// Bind the port...
			logger.info("Binding port " + NetworkConstants.GAME_PORT + "...");
			ResourceLeakDetector.setLevel(Level.DISABLED);
			EventLoopGroup loopGroup = new NioEventLoopGroup();
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(loopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelPipelineHandler())
					.bind(NetworkConstants.GAME_PORT).syncUninterruptibly();

			// Start the game engine using a {@link ScheduledExecutorService}
			logger.info("Starting game engine...");
			final ScheduledExecutorService executor = Executors
					.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
			executor.scheduleAtFixedRate(engine, 0, GameConstants.ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);

			logger.info("The loader has finished loading utility tasks.");
			logger.info("RuneTerra is now online on port " + NetworkConstants.GAME_PORT + "!");
		} catch (Exception ex) {
			logger.log(java.util.logging.Level.SEVERE, "Could not start RuneTerra! Program terminated.", ex);
			System.exit(1);
		}
	}

	public static CacheLoader getCache() {
		return cacheLoader;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setUpdating(boolean updating) {
		RuneTerra.updating = updating;
	}

	public static boolean isUpdating() {
		return RuneTerra.updating;
	}
}