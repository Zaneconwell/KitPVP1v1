package rippin.bullyscraft.com.Configs;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import rippin.bullyscraft.com.KitPVP1v1;

import java.io.*;

public class PlayerDataConfig {
    private static KitPVP1v1 plugin;
    private static FileConfiguration config;
    private static File configFile;


    public static void setUp(KitPVP1v1 pluginInstance){
        plugin = pluginInstance;
        configFile = new File(plugin.getDataFolder(), "PlayerData.yml");
        config = YamlConfiguration.loadConfiguration(configFile);
        create();
    }

    public static void create(){
        if (!(getFile().exists())){
            plugin.getServer().getLogger().info("PlayerData.yml not found. Creating now...");
            try {
                getFile().createNewFile();
                reload(); //reload yml just in case

                saveFile();
                plugin.getServer().getLogger().info("PlayerData.yml has been created!");
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFile(){
        return configFile;
    }

    public static FileConfiguration getConfig(){
        return config;
    }

    public static void saveFile() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void reload(){
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
