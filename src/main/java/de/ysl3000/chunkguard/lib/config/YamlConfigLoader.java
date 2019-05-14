package de.ysl3000.chunkguard.lib.config;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlConfigLoader
{
    protected YamlConfiguration config;
    private File file;
    
    public YamlConfigLoader(final String path, final String file) {
        new File("./plugins/" + path + '/').mkdir();
        this.file = new File("./plugins/" + path + '/' + file);
        this.loadConfig();
    }
    
    public final void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }
    
    public final boolean saveConfig() {
        try {
            this.config.save(this.file);
            return true;
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    public void addDefault(final String path, final Object value) {
        if (!this.config.contains(path)) {
            this.config.set(path, value);
            this.saveConfig();
        }
    }


}
