package io.github.pxlpowered.foundations.api.configuration;

import java.nio.file.Path;

/**
 * A persistent variant of {@link Configuration}.
 *
 * <p>The configuration can be saved to disk and can persist over restarts.
 */
public interface PersistentConfiguration extends Configuration {

    /**
     * Saves the configuration to disk.
     */
    void save();

    /**
     * The builder for {@link PersistentConfiguration}.
     */
    interface Builder<P extends PersistentConfiguration> extends Configuration.Builder<P> {

        /**
         * The path of the file to save the configuration to when
         *     {@link PersistentConfiguration#save()} is called.
         *
         * @param path The file path.
         * @return The builder.
         */
        Builder file(Path path);

    }

}
