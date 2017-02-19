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
     * The implementation for {@link TransientConfiguration.Builder}.
     */
    public static final class BuilderImpl extends AbstractBuilder<TransientConfiguration> implements TransientConfiguration.Builder {

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
