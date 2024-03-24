package com.link_intersystems.gradle.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PluginProperties {

    private Properties properties = new Properties();

    public PluginProperties(String pluginName) {

        String pluginPropertyResourceName = pluginName + ".properties";
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(pluginPropertyResourceName)) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(pluginPropertyResourceName + " can not be loaded.", e);
        }
    }

    public String getId(){
        return properties.getProperty("id");
    }
}
