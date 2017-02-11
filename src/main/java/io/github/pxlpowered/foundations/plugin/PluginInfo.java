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

import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.List;

/**
 * General metadata about the plugin for easy access.
 */
public final class PluginInfo {

    private static final PluginContainer CONTAINER = Sponge.getPluginManager().getPlugin(PluginInfo.ID).get();
    private static final String UNKNOWN = "unknown";

    @SuppressWarnings("WeakerAccess")
    public static final String ID = "foundations";

    public static final String NAME = CONTAINER.getName();

    public static final String VERSION = CONTAINER.getVersion().orElse(PluginInfo.UNKNOWN);

    public static final String DESCRIPTION = CONTAINER.getDescription().orElse(PluginInfo.UNKNOWN);

    public static final String URL = CONTAINER.getUrl().orElse(PluginInfo.UNKNOWN);

    public static final List<String> AUTHORS = CONTAINER.getAuthors();

}
