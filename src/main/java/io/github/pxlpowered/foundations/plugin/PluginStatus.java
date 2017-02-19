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

import io.github.pxlpowered.foundations.core.message.internal.InternalMessages;
import org.slf4j.Logger;

import javax.annotation.Nullable;

/**
 * Handles the global plugin status.
 */
public final class PluginStatus {

    /**
     * The internal messages are loaded.
     *
     * <p>The {@link InternalMessages} are first thing to load as all internal
     *     messages are handled through it.
     *
     * <p>Debug code: {@code I}.
     *
     * <p><b>NOTE: </b>A {@code byte} is used here. When doing bitwise
     *     operations, cast to a long or shift bits to compensate.
     *
     * @see #INTERNAL_MESSAGES_CODE
     */
    private static final byte INTERNAL_MESSAGES = 0x1;

    /**
     * The global config is loaded.
     *
     * <p>The global config is the second thing to load, before the
     * {@link Logger} wrappers.
     *
     * <p>Debug code: {@code G}.
     *
     * <p><b>NOTE: </b>A {@code byte} is used here. When doing bitwise
     *     operations, cast to a long or shift bits to compensate.
     *
     * @see #INTERNAL_MESSAGES_CODE
     */
    private static final byte GLOBAL_CONFIG = 0x2;

    /**
     * The main configs are loaded.
     *
     * <p>These configs are needed for any operation. Loads before FoMoLo
     *     starts.
     *
     * <p>Debug code: {@code C}.
     *
     * <p><b>NOTE: </b>A {@code byte} is used here. When doing bitwise
     *     operations, cast to a {@code long} or shift bits to compensate.
     *
     * @see #MAIN_CONFIGS_CODE
     */
    private static final byte MAIN_CONFIGS = 0x8;

    // We use bits to save memory
    private static long state = 0x1;
    private static boolean errored = false;

    public static final char INTERNAL_MESSAGES_CODE = 'I';
    public static final char GLOBAL_CONFIG_CODE = 'G';
    public static final char MAIN_CONFIGS_CODE = 'C';

    // Do not instantiate
    private PluginStatus() {
    }

    /**
     * Gets the state value.
     *
     * @return The state value.
     */
    public static long getState() {
        return state;
    }

    /**
     * Sets the internal messages bit.
     *
     * @param internalMessages {@code true} for set, {@code false} for unset.
     */
    public static void setInternalMessages(boolean internalMessages) {
        if (isInternalMessages() && !internalMessages) {
            state &= ~((long)INTERNAL_MESSAGES);
        }

        if (!isInternalMessages() && internalMessages) {
            state |= ((long)INTERNAL_MESSAGES);
        }
    }

    /**
     * Gets if the internal messages bit is set or not.
     *
     * @return {@code true} is set, {@code false} otherwise.
     */
    public static boolean isInternalMessages() {
        return (getState() & ((long)INTERNAL_MESSAGES)) != 0;
    }

    /**
     * Sets the internal messages bit.
     *
     * @param internalMessages {@code true} for set, {@code false} for unset.
     */
    public static void setGlobalConfig(boolean internalMessages) {
        if (isInternalMessages() && !internalMessages) {
            state &= ~((long)GLOBAL_CONFIG);
        }

        if (!isInternalMessages() && internalMessages) {
            state |= ((long)GLOBAL_CONFIG);
        }
    }

    /**
     * Gets if the internal messages bit is set or not.
     *
     * @return {@code true} is set, {@code false} otherwise.
     */
    public static boolean isGlobalConfig() {
        return (getState() & ((long)GLOBAL_CONFIG)) != 0;
    }

    /**
     * Gets if the plugin is in an error state.
     *
     * @return {@code true} if plugin is errored, false otherwise.
     */
    public static boolean isErrored() {
        return errored;
    }

    /**
     * Sets the plugin into an error state.
     *
     * @param errored {@code true} to mark errored, {@code false} to mark not errored.
     */
    public static void setErrored(boolean errored) {
        PluginStatus.errored = errored;
    }

    /**
     * Check if the the plugin is in error state and if it is, run the disable
     *     tasks.
     *
     * @param plugin The Foundations plugin.
     * @param throwable The exception thrown if any.
     */
    public static void checkForError(FoundationsPlugin plugin, @Nullable Throwable throwable) {
        // TODO cleanup stuff
        // TODO print exception iff there is one.
        final String status = "%s%s";
        final String errorMes = "A fatal error occurred. %s will now become ineffective. Status: %s";

        if (isErrored()) {

            plugin.getLogger().error(String.format(errorMes, PluginInfo.ID,
                    String.format(status,
                            PluginStatus.isInternalMessages() ? PluginStatus.INTERNAL_MESSAGES_CODE : '*',
                            PluginStatus.isGlobalConfig() ? PluginStatus.GLOBAL_CONFIG_CODE : '*')),
                    throwable);

            clearForError(plugin);
        }
    }

    private static void clearForError(FoundationsPlugin plugin) {
        // TODO actually do stuff here
        // TODO clear InternalMessages guice provider
    }

}
