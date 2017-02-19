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

/**
 *
 */
public abstract class AbstractConfiguration implements Configuration {

    protected final InternalMessages internalMessages;
    protected final Set<URL> urls = new HashSet<>();
    protected final Logger logger;
    protected final UUID uuid;

    @Nullable protected CommentedConfigurationNode node;

    protected AbstractConfiguration(InternalMessages internalMessages, Set<URL> urls, Logger logger, UUID uuid) {
        this.internalMessages = internalMessages;
        this.urls.addAll(urls);
        this.logger = logger;
        this.uuid = uuid;
    }

    @Override
    public Optional<CommentedConfigurationNode> get() {
        return Optional.ofNullable(node);
    }

    protected static abstract class AbstractBuilder<T extends Configuration> implements Builder<T> {

        protected final InternalMessages internalMessages;
        protected final Set<URL> urls = new HashSet<>();

        protected AbstractBuilder(InternalMessages internalMessages) {
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
        public final Builder<T> from(T value) {
            checkNotNull(value, "value");

            urls.clear();
            urls.addAll(((AbstractConfiguration)value).urls);
            return getThis();
        }

        @Override
        public final Builder<T> reset() {

            return getThis();
        }

        private <R extends AbstractBuilder<T>> R getThis() {
            //noinspection unchecked
            return (R)this;
        }

    }

}
