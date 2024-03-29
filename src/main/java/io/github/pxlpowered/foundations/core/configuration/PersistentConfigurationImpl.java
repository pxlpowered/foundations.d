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
import io.github.pxlpowered.foundations.api.configuration.PersistentConfiguration;
import io.github.pxlpowered.foundations.core.message.internal.InternalMessages;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * The implementation for {@link PersistentConfiguration}.
 */
public final class PersistentConfigurationImpl extends AbstractConfiguration implements PersistentConfiguration {

    private final Path path;

    @Nullable private ConfigurationLoader<CommentedConfigurationNode> loader;

    private PersistentConfigurationImpl(InternalMessages internalMessages, Set<URL> urls, Path path, Logger logger, UUID uuid) {
        super(internalMessages, urls, logger, uuid);
        this.path = path;
    }

    @Override
    public void load() {
        //noinspection ConstantConditions
        if (loader == null) {
            logger.debug(internalMessages.getLog("configuration.load.loader-null"), uuid);
            loader = HoconConfigurationLoader.builder().setPath(path).build();
        }

        try {
            logger.debug(internalMessages.getLog("configuration.load.attempt"), uuid);
            node = loader.load();
            // try catch prevents null
            assert node != null;

            urls.forEach(url -> {
                try {
                    logger.debug(internalMessages.getLog("configuration.asset.load.attempt"), url, uuid);
                    node.mergeValuesFrom(HoconConfigurationLoader.builder().setURL(url).build().load());
                } catch (IOException e) {
                    logger.error(internalMessages.getStringFormatted("configuration.asset.load.error.debug", url, uuid), e);
                }
            });

            logger.debug(internalMessages.getLog("configuration.load.success"), uuid);
        } catch (IOException e) {
            logger.error(internalMessages.getLog("configuration.load.error.debug"), e);
        }
    }

    @Override
    public void save() {
        if (!(loader == null)) {
            try {
                loader.save(node);
                logger.debug(internalMessages.getLog("configuration.save.success"), uuid);
            } catch (IOException e) {
                logger.error(internalMessages.getLog("configuration.save.error.debug"), e);
            }
        }
    }

    /**
     * The implementation for {@link io.github.pxlpowered.foundations.api.configuration.PersistentConfiguration.Builder}.
     */
    public static final class BuilderImpl extends AbstractBuilder<PersistentConfiguration>
            implements PersistentConfiguration.Builder<PersistentConfiguration> {

        @Nullable private Path path;

        /**
         * Constructs a new instance of {@link BuilderImpl}.
         *
         * @param internalMessages The internal messages instance.
         */
        public BuilderImpl(InternalMessages internalMessages) {
            super(internalMessages);
        }

        @Override
        public PersistentConfiguration build(Logger logger) {
            checkNotNull(logger, "logger");
            checkNotNull(path, path);

            return new PersistentConfigurationImpl(internalMessages, urls, path, logger, UUID.randomUUID());
        }

        @Override
        public PersistentConfiguration.Builder file(Path path) {
            checkNotNull(path, "path");

            this.path = path;
            return this;
        }

        @OverridingMethodsMustInvokeSuper
        @Override
        public Configuration.Builder<PersistentConfiguration> from(PersistentConfiguration value) {
            super.from(value);

            path = ((PersistentConfigurationImpl)value).path;
            return this;
        }

        @OverridingMethodsMustInvokeSuper
        @Override
        public Configuration.Builder<PersistentConfiguration> reset() {
            super.reset();

            path = null;
            return this;
        }

    }

}
