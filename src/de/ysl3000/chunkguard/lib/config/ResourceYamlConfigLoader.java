package de.ysl3000.chunkguard.lib.config;

import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.*;
import java.io.*;

public class ResourceYamlConfigLoader extends YamlConfigLoader
{
    public ResourceYamlConfigLoader(final String path, final String file, final boolean has_resource_in_jar, final JavaPlugin pl) {
        super(path, file);
        if (has_resource_in_jar) {
            final InputStream defConfigStream = pl.getResource(file);
            if (defConfigStream != null) {
                final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
                this.config.setDefaults(defConfig);
                this.config.options().copyDefaults(has_resource_in_jar);
                this.saveConfig();
            }
        }
    }
}
