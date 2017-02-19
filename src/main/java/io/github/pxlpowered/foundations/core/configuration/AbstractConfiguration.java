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

package io.github.pxlpowered.foundations.core.configuration;

import static com.google.common.base.Preconditions.checkNotNull;

import io.github.pxlpowered.foundations.api.configuration.Configuration;
import io.github.pxlpowered.foundations.core.message.internal.InternalMessages;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.slf4j.Logger;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * Abstract implementation for {@link Configuration}s.
 */
public abstract class AbstractConfiguration implements Configuration {

    final InternalMessages internalMessages;
    final Set<URL> urls = new HashSet<>();
    final Logger logger;
    final UUID uuid;

    @Nullable CommentedConfigurationNode node;

    /**
     * Constructs a new instance of a {@link AbstractConfiguration}.
     *
     * @param internalMessages The internal messages instance.
     * @param urls The asset urls.
     * @param logger The instance.
     * @param uuid The unique id for the configuration.
     */
    AbstractConfiguration(InternalMessages internalMessages, Set<URL> urls, Logger logger, UUID uuid) {
        this.internalMessages = internalMessages;
        this.urls.addAll(urls);
        this.logger = logger;
        this.uuid = uuid;
    }

    @Override
    public Optional<CommentedConfigurationNode> get() {
        return Optional.ofNullable(node);
    }

    /**
     * Abstract implementation for {@link Configuration.Builder}.
     *
     * @param <T> The type of configuration.
     */
    static abstract class AbstractBuilder<T extends Configuration> implements Builder<T> {

        final InternalMessages internalMessages;
        final Set<URL> urls = new HashSet<>();

        /**
         * Constructs a new instance of {@link AbstractConfiguration.AbstractBuilder}.
         *
         * @param internalMessages The internal messages instance.
         */
        AbstractBuilder(InternalMessages internalMessages) {
            checkNotNull(internalMessages, "internalMessages");

            this.internalMessages = internalMessages;
        }

        @Override
        public T build(PluginContainer container) {
            checkNotNull(container, "container");

            return build(container.getLogger());
        }

        @Override
        public Builder<T> defaults(Asset asset) {
            checkNotNull(asset, "asset");

            urls.add(asset.getUrl());
            return getThis();
        }

        @Override
        public Builder<T> defaults(Asset... assets) {
            checkNotNull(assets, "assets");

            return defaults(Arrays.stream(assets).collect(Collectors.toList()));
        }

        @Override
        public Builder<T> defaults(Collection<Asset> assets) {
            checkNotNull(assets, "assets");

            assets.forEach(getThis()::defaults);
            return getThis();
        }

        @Override
        @OverridingMethodsMustInvokeSuper
        public Builder<T> from(T value) {
            checkNotNull(value, "value");

            urls.clear();
            urls.addAll(((AbstractConfiguration)value).urls);
            return getThis();
        }

        @Override
        @OverridingMethodsMustInvokeSuper
        public Builder<T> reset() {
            urls.clear();
            return getThis();
        }

        private <R extends AbstractBuilder<T>> R getThis() {
            //noinspection unchecked
            return (R)this;
        }

    }

}
