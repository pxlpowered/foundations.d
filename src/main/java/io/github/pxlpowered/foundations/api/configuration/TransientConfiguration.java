package io.github.pxlpowered.foundations.api.configuration;

/**
 * A configuration that only exists in memory and cannot persist over restarts.
 */
public interface TransientConfiguration extends Configuration {

    /**
     * The builder for {@link TransientConfiguration}.
     */
    interface Builder extends Configuration.Builder<TransientConfiguration> {

    }

}
