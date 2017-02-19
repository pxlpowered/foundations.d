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

package io.github.pxlpowered.foundations.core.message.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextParseException;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.text.MessageFormat;
import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the plugin's internal messages.
 *
 * <p>Internal messages are for developers' understanding, and as such are
 * only provided in {@link Locale#US} for simplicity.
 */
public final class InternalMessages {

    private static final String KEY_MISSING = "The key %s is missing";
    private static final String MALFORMED_TEXT = "The text for key %s is malformed";

    private final ConfigurationNode node = SimpleConfigurationNode.root();

    /**
     * Constructs a new InternalMessages instance for plugin messages.
     *
     * @param plugin The plugin instance.
     * @throws Exception Thrown if the asset was not found or was not able to be loaded.
     */
    public InternalMessages(PluginContainer plugin) throws Exception {
        checkNotNull(plugin);

        Optional<Asset> assetOptional = plugin.getAsset("internal-messages.properties");

        if (assetOptional.isPresent()) {
            node.mergeValuesFrom(HoconConfigurationLoader.builder().setURL(assetOptional.get().getUrl()).build().load());
        } else {
            throw new RuntimeException("Internal Messages asset assets/foundations/messages/internal.properties is missing.");
        }
    }

    /**
     * Gets a string value for logging purposes.
     *
     * @param key The key for the value.
     * @return The log message.
     */
    public String getLog(String key) {
        checkNotNull(key, "key");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            return String.format(KEY_MISSING, key);
        }

        return tmp.getString();
    }

    /**
     * Gets a string value.
     *
     * @param key The key for the value.
     * @return The string message.
     */
    public String getPlain(String key) {
        checkNotNull(key, "key");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            return String.format(KEY_MISSING, key);
        }

        return tmp.getString();
    }

    /**
     * Gets a formatted string value.
     *
     * <p>Uses the {@link Formatter} for formatting, {@code String.format(String,Object[])}.
     *
     * @param key The key for the value.
     * @param args The args for formatting.
     * @return The string message.
     */
    public String getStringFormatted(String key, Object... args) {
        checkNotNull(key, "key");
        checkNotNull(args, "args");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            return String.format(KEY_MISSING, key);
        }

        return String.format(tmp.getString(), args);
    }

    /**
     * Gets a formatted string value.
     *
     * <p>Uses the {@link MessageFormat} for formatting, {@code MessageFormat.format(String,Object[])}.
     *
     * @param key The key for the value.
     * @param args The args for formatting.
     * @return The string message.
     */
    public String getTextFormatted(String key, Object... args) {
        checkNotNull(key, "key");
        checkNotNull(args, "args");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            return String.format(KEY_MISSING, key);
        }

        return MessageFormat.format(tmp.getString(), args);
    }

    /**
     * Gets a json string as a {@link Text} object.
     *
     * @param key The key for the value.
     * @return The text message.
     */
    public Text getJsonText(String key) {
        checkNotNull(key, "key");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            //noinspection MalformedFormatString
            return Text.of(TextColors.RED, TextStyles.BOLD, String.format(KEY_MISSING, key));
        }

        try {
            return tmp.getValue(TypeToken.of(Text.class));
        } catch (ObjectMappingException e) {
            return Text.of(TextColors.RED, TextStyles.BOLD, String.format(MALFORMED_TEXT, key));
        }
    }

    /**
     * Gets a formatting code string as a {@link Text} object.
     *
     * @param key The key for the value.
     * @return The text message.
     */
    public Text getFormatCodeText(String key) {
        checkNotNull(key, "key");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            //noinspection MalformedFormatString
            return Text.of(TextColors.RED, TextStyles.BOLD, String.format(KEY_MISSING, key));
        }

        try {
            return TextSerializers.FORMATTING_CODE.deserialize(tmp.getString());
        } catch (TextParseException e) {
            return Text.of(TextColors.RED, TextStyles.BOLD, String.format(MALFORMED_TEXT, key));
        }
    }

    /**
     * Gets a text template json string as a {@link Text} object.
     *
     * @param key The key for the value.
     * @param arguments The arguments for
     * @return The text message.
     */
    public Text getTemplateText(String key, Map<String, TextElement> arguments) {
        checkNotNull(key, "key");
        checkNotNull(arguments, "arguments");

        ConfigurationNode tmp = node.getNode((Object[]) key.split("\\."));

        if (tmp.isVirtual()) {
            return Text.of(TextColors.RED, TextStyles.BOLD, String.format(KEY_MISSING, key));
        }

        TextTemplate template;

        try {
            template = tmp.getValue(TypeToken.of(TextTemplate.class));
        } catch (ObjectMappingException e) {
            return Text.of(TextColors.RED, TextStyles.BOLD, String.format("The text template for key %s is malformed", key));
        }

        return template.apply(arguments).build();
    }

}
