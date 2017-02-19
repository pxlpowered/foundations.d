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

import io.github.pxlpowered.foundations.api.configuration.TransientConfiguration;
import io.github.pxlpowered.foundations.core.message.internal.InternalMessages;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

/**
 * The implementation for {@link TransientConfiguration}.
 */
public final class TransientConfigurationImpl extends AbstractConfiguration implements TransientConfiguration {

    private TransientConfigurationImpl(InternalMessages internalMessages, Set<URL> urls, Logger logger, UUID uuid) {
        super(internalMessages, urls, logger, uuid);
    }

    @Override
    public void load() {
        logger.debug(internalMessages.getLog("configuration.load.attempt"), uuid);
        node = SimpleCommentedConfigurationNode.root();

        urls.forEach(url -> {
            try {
                node.mergeValuesFrom(HoconConfigurationLoader.builder().setURL(url).build().load());
            } catch (IOException e) {
                logger.error(internalMessages.getStringFormatted("configuration.asset.load.error.debug", url, uuid), e);
            }
        });

        logger.debug(internalMessages.getLog("configuration.load.success"), uuid);
    }

    /**
     * The implementation for {@link io.github.pxlpowered.foundations.api.configuration.TransientConfiguration.Builder}.
     */
    public static final class BuilderImpl extends AbstractBuilder<TransientConfiguration> implements TransientConfiguration.Builder {

        /**
         * Constructs a new instance of {@link TransientConfigurationImpl}.
         *
         * @param internalMessages The internal messages instance.
         */
        public BuilderImpl(InternalMessages internalMessages) {
            super(internalMessages);
        }

        @Override
        public TransientConfigurationImpl build(Logger logger) {
            checkNotNull(logger, "logger");

            return new TransientConfigurationImpl(internalMessages, urls, logger, UUID.randomUUID());
        }

    }

}
