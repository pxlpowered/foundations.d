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

package io.github.pxlpowered.foundations.api.configuration;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.slf4j.Logger;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Collection;
import java.util.Optional;

/**
 * Marks a configuration class.
 *
 * <p>Contains methods for loading a configuration and getting the actual
 * {@link CommentedConfigurationNode}.
 */
public interface Configuration {

    /**
     * Loads the configuration into memory.
     */
    void load();

    /**
     * Gets the {@link CommentedConfigurationNode} for the configuration.
     *
     * @return The configuration node, {@link Optional#empty()} otherwise.
     */
    Optional<CommentedConfigurationNode> get();

    /**
     * The builder for {@link Configuration}.
     *
     * @param <T> The type of configuration.
     */
    interface Builder<T extends Configuration> extends ResettableBuilder<T, Builder<T>> {

        /**
         * Builds an instance of configuration.
         *
         * @param logger The logger.
         * @return configuration instance.
         */
        T build(Logger logger);

        /**
         * Builds an instance of configuration.
         *
         * @param container The plugin container..
         * @return configuration instance.
         */
        T build(PluginContainer container);

        /**
         * The asset to merge configuration nodes from.
         *
         * @param asset The asset.
         * @return The builder.
         */
        Builder<T> defaults(Asset asset);

        /**
         * The assets to merge configuration nodes from.
         *
         * @param assets The assets.
         * @return The builder.
         */
        Builder<T> defaults(Asset... assets);

        /**
         * The assets to merge configuration nodes from.
         *
         * @param assets The assets.
         * @return The builder.
         */
        Builder<T> defaults(Collection<Asset> assets);

    }

}
