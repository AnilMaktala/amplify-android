/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amplifyframework.analytics;

import android.content.Context;
import android.support.annotation.NonNull;

import com.amplifyframework.core.category.Category;
import com.amplifyframework.core.category.CategoryType;
import com.amplifyframework.core.exception.ConfigurationException;
import com.amplifyframework.core.plugin.Plugin;
import com.amplifyframework.core.plugin.PluginException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Defines the Client API consumed by the application.
 * Internally routes the calls to the Analytics CategoryType
 * plugins registered.
 */
public class Analytics implements Category<AnalyticsPlugin, AnalyticsPluginConfiguration>, AnalyticsCategoryBehavior {

    static class PluginDetails {
        AnalyticsPlugin analyticsPlugin;
        AnalyticsPluginConfiguration analyticsPluginConfiguration;

        public AnalyticsPlugin getAnalyticsPlugin() {
            return analyticsPlugin;
        }

        public PluginDetails analyticsPlugin(AnalyticsPlugin analyticsPlugin) {
            this.analyticsPlugin = analyticsPlugin;
            return this;
        }

        public AnalyticsPluginConfiguration getAnalyticsPluginConfiguration() {
            return analyticsPluginConfiguration;
        }

        public PluginDetails analyticsPluginConfiguration(AnalyticsPluginConfiguration analyticsPluginConfiguration) {
            this.analyticsPluginConfiguration = analyticsPluginConfiguration;
            return this;
        }
    }

    /**
     * Map of the { pluginKey => plugin } object
     */
    private Map<String, PluginDetails> plugins;

    /**
     * By default collection and sending of Analytics events
     * are enabled.
     */
    private boolean enabled;

    /**
     * Protect enabling and disabling of Analytics event
     * collection and sending.
     */
    private static final Object LOCK = new Object();

    public Analytics() {
        this.plugins = new ConcurrentHashMap<String, PluginDetails>();
        this.enabled = true;
    }

    @Override
    public void disable() {
        synchronized (LOCK) {
            enabled = false;
        }
    }

    @Override
    public void enable() {
        synchronized (LOCK) {
            enabled = true;
        }
    }

    @Override
    public void recordEvent(@NonNull String eventName) throws AnalyticsException {
        if (enabled) {

        }
    }

    @Override
    public void recordEvent(@NonNull final AnalyticsEvent analyticsEvent) throws AnalyticsException {
        if (enabled) {

        }
    }

    @Override
    public void updateProfile(@NonNull AnalyticsProfile analyticsProfile) throws AnalyticsException {
        if (enabled) {

        }
    }

    /**
     * Read the configuration from amplifyconfiguration.json file
     *
     * @param context Android context required to read the contents of file
     * @throws ConfigurationException thrown when already configured
     * @throws PluginException        thrown when there is no plugin found for a configuration
     */
    @Override
    public void configure(@NonNull Context context) throws ConfigurationException, PluginException {
        if (!plugins.values().isEmpty()) {
            if (plugins.values().iterator().hasNext()) {
                PluginDetails pluginDetails = plugins.values().iterator().next();
                if (pluginDetails.analyticsPluginConfiguration == null) {
                    pluginDetails.analyticsPluginConfiguration(new AnalyticsPluginConfiguration(context));
                }

                pluginDetails.analyticsPlugin.configure(pluginDetails.analyticsPluginConfiguration);
            }
        }
    }

    /**
     * Read the configuration from amplifyconfiguration.json file
     *
     * @param context     Android context required to read the contents of file
     * @param environment specifies the name of the environment being operated on.
     *                    For example, "Default", "Custom", etc.
     * @throws ConfigurationException thrown when already configured
     * @throws PluginException        thrown when there is no plugin found for a configuration
     */
    @Override
    public void configure(@NonNull Context context, @NonNull String environment) throws ConfigurationException, PluginException {

    }

    /**
     * Register a plugin with Amplify
     *
     * @param plugin an implementation of a CATEGORY_TYPE that
     *               conforms to the {@link Plugin} interface.
     * @throws PluginException when a plugin cannot be registered for this CATEGORY_TYPE
     */
    @Override
    public void addPlugin(@NonNull AnalyticsPlugin plugin) throws PluginException {
        PluginDetails pluginDetails = new PluginDetails()
                .analyticsPlugin(plugin);

        try {
            if (plugins.put(plugin.getPluginKey(), pluginDetails) == null) {
                throw new PluginException.NoSuchPluginException();
            }
        } catch (Exception ex) {
            throw new PluginException.NoSuchPluginException();
        }
    }

    /**
     * Register a plugin with Amplify
     *
     * @param plugin              an implementation of a Category that
     *                            conforms to the {@link Plugin} interface.
     * @param pluginConfiguration configuration information for the plugin.
     * @throws PluginException when a plugin cannot be registered for this category
     */
    @Override
    public void addPlugin(@NonNull AnalyticsPlugin plugin, @NonNull AnalyticsPluginConfiguration pluginConfiguration) throws PluginException {
        PluginDetails pluginDetails = new PluginDetails()
                .analyticsPlugin(plugin)
                .analyticsPluginConfiguration(pluginConfiguration);

        try {
            if (plugins.put(plugin.getPluginKey(), pluginDetails) == null) {
                throw new PluginException.NoSuchPluginException();
            }
        } catch (Exception ex) {
            throw new PluginException.NoSuchPluginException();
        }
    }

    /**
     * Remove a registered plugin
     *
     * @param plugin an implementation of a CATEGORY_TYPE that
     *               conforms to the {@link Plugin} interface.
     */
    @Override
    public void removePlugin(@NonNull AnalyticsPlugin plugin) throws PluginException {
        try {
            if (plugins.remove(plugin.getPluginKey()) == null) {
                throw new PluginException.NoSuchPluginException();
            }
        } catch (Exception ex) {
            throw new PluginException.NoSuchPluginException();
        }
    }

    /**
     * Reset Amplify to state where it is not configured.
     * <p>
     * Remove all the plugins added.
     * Remove the configuration stored.
     */
    @Override
    public void reset() {

    }

    /**
     * Retrieve a plugin of CATEGORY_TYPE.
     *
     * @param pluginKey the key that identifies the plugin implementation
     * @return the plugin object
     */
    @Override
    public AnalyticsPlugin getPlugin(@NonNull String pluginKey) throws PluginException {
        if (plugins.containsKey(pluginKey)) {
            return plugins.get(pluginKey).analyticsPlugin;
        } else {
            throw new PluginException.NoSuchPluginException();
        }
    }

    @Override
    public CategoryType getCategoryType() {
        return CategoryType.ANALYTICS;
    }
}
