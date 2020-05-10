package com.runeterra.script;

import com.runeterra.script.api.Behavior;
import com.runeterra.script.api.ObjectBehavior;
import com.sun.nio.file.SensitivityWatchEventModifier;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.*;

public class ScriptManager {

    public static final Logger logger = Logger.getLogger(ScriptManager.class.getName());

    public static final ObjectBehavior objectBehavior = new ObjectBehavior();

    private static final Behavior[] behaviors = {
            objectBehavior
    };

    public static void init(boolean watchForChanges) {
        File scriptsDirectory = new File("scripts/dist");
        if (!scriptsDirectory.exists()) {
            logger.warning("No scripts directory found at '" + scriptsDirectory.getAbsolutePath() + "'.");
            return;
        }

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        engine.put("object", objectBehavior);

        if (watchForChanges) {
            watchScriptChanges(scriptsDirectory.toPath(), () -> loadScripts(scriptsDirectory.toPath(), engine));
        } else {
            loadScripts(scriptsDirectory.toPath(), engine);
        }
    }

    private static void watchScriptChanges(Path root, Runnable onChange) {
        try {
            WatchService watchService = root.getFileSystem().newWatchService();
            walkAndRegisterDirectories(watchService, root);
            onChange.run();

            new Thread(() -> {
                while (true) {
                    WatchKey key = watchService.poll();
                    if (key == null)
                        continue;

                    boolean directoryAdded = false;
                    boolean scriptChanged = false;
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == ENTRY_CREATE) directoryAdded = true;
                        if (event.kind() == ENTRY_DELETE || event.context().toString().toLowerCase().endsWith(".js"))
                            scriptChanged = true;
                    }

                    if (directoryAdded) walkAndRegisterDirectories(watchService, root);
                    if (scriptChanged) onChange.run();

                    key.reset();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void walkAndRegisterDirectories(WatchService watchService, Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attrs) throws IOException {
                    watchDirectory(watchService, directory);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void watchDirectory(WatchService watchService, Path directory) throws IOException {
        directory.register(
                watchService,
                new WatchEvent.Kind[]{ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE},
                SensitivityWatchEventModifier.HIGH
        );
    }

    private static void loadScripts(Path root, ScriptEngine engine) {
        long start = System.currentTimeMillis();
        AtomicInteger numberOfScripts = new AtomicInteger(0);

        Arrays.stream(behaviors).forEach(Behavior::initialize);

        try {
            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        if (file.toString().toLowerCase().endsWith(".js")) {
                            engine.eval(new FileReader(file.toFile()));
                            numberOfScripts.incrementAndGet();
                        }
                    } catch (FileNotFoundException | ScriptException e) {
                        logger.warning("Could not load script: '" + file + "'");
                        e.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Loaded " + numberOfScripts.get() + " script(s) - took " + (System.currentTimeMillis() - start) + "ms.");
    }
}
