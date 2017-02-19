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
