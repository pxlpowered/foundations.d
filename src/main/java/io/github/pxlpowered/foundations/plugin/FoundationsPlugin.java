/*
 * This file is part of foundations, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 - 2017 PxL Powered <https://pxlpowered.github.io/foundations>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.pxlpowered.foundations.plugin;

import com.google.inject.Inject;
import com.google.inject.Injector;
import io.github.pxlpowered.foundations.core.message.internal.InternalMessages;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

/**
 * The main class for Foundations plugin.
 */
@Plugin(id = PluginInfo.ID)
public final class FoundationsPlugin {

    private final PluginContainer container;

    @Inject private Injector injector;
    @Inject private Logger logger;

    private InternalMessages internalMessages;

    @Inject
    private FoundationsPlugin(PluginContainer container, Injector injector) {
        this.container = container;
        this.injector = injector;
    }

    /**
     * Pre Initialization tasks.
     *
     * @param event The {@link GamePreInitializationEvent}.
     */
    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        logger.info("Starting " + PluginInfo.ID);
        try {
            internalMessages = new InternalMessages(getContainer());
            getLogger().info(internalMessages.getLog("plugin.phase.enter"), event.getState());
        } catch (Exception e) {
            // TODO king: plugin states
            e.printStackTrace();
        }

        getLogger().info(internalMessages.getLog("plugin.phase.exit"));
    }

    /**
     * Gets the currently active {@link Logger} for the plugin.
     *
     * @return The logger.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets the {@link PluginContainer} for foundations plugin.
     *
     * @return The plugin container.
     */
    public PluginContainer getContainer() {
        return container;
    }

}
