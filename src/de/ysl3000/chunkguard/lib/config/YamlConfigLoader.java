package de.ysl3000.chunkguard.lib.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Set;

public abstract class YamlConfigLoader
{
    protected YamlConfiguration config;
    private File file;
    
    public YamlConfigLoader(final String path, final String file) {
        new File("./plugins/" + path + "/").mkdir();
        this.file = new File("./plugins/" + path + "/" + file);
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


    public Inventory getInventoryFromConfigurationSection(String path) {

        String title = config.getString(path + ".title");

        ConfigurationSection configurationSection = config.getConfigurationSection(path + ".items");

        Set<String> itemKeys = configurationSection.getKeys(false);

        Inventory inventory = Bukkit.createInventory(null, itemKeys.size(), title);
        for (int i = 0; i < itemKeys.size(); i++) {
            inventory.setItem(i, configurationSection.getItemStack("" + i));
        }
        return inventory;
    }

    protected boolean setInventoryToConfigurationSection(String path, Inventory inventory) {

        ConfigurationSection configurationSection = config.createSection(path + ".items");

        String title = inventory.getTitle();
        config.set(path + ".title", title);

        int size = inventory.getSize();

        for (int index = 0; index < size; size++) {
            configurationSection.set("" + index, inventory.getItem(index));
        }
        return saveConfig();
    }



    protected Inventory getInventoryFromBase64String(String path) throws IOException, ClassNotFoundException {

        BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(Base64.getDecoder().decode(config.getString(path).getBytes()))));

        String name = bukkitObjectInputStream.readUTF();
        int size = bukkitObjectInputStream.readInt();

        Inventory inventory = Bukkit.createInventory(null, size, name);

        for (int index = 0; index < size; index++) {
            inventory.setItem(index, (ItemStack) bukkitObjectInputStream.readObject());
        }

        bukkitObjectInputStream.close();

        return inventory;
    }

    protected void setInventoryToBase64String(String path, Inventory inventory) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

        String name = inventory.getName();
        int size = inventory.getSize();

        bukkitObjectOutputStream.writeUTF(name);
        bukkitObjectOutputStream.writeInt(size);
        for (int index = 0; index < size; index++) {
            bukkitObjectOutputStream.writeObject(inventory.getItem(index));
        }
        bukkitObjectOutputStream.close();

        config.set(path, Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()));
    }






}
