package nkn6294.java.config;

import java.io.File;
import java.util.*;

public class Config {
    Config(String template) {
        mapConfig = new HashMap<>();
        this.template = template;
    }
    
    public void loadConfig(String file) {
        Map<String, String> temp = ConfigUtil.importConfig(file);
        for(String key : temp.keySet()) {
            mapConfig.put(key, temp.get(key));
        }
    }
    public void exportConfig(String file) {
        ConfigUtil.exportConfig(mapConfig, new File(template), new File(file));
    }
    
    public String getValue(String key) {
        return mapConfig.get(key);
    }
    public Object setValue(String key, Object value) {
        return mapConfig.put(key, value.toString());
    }
    public Set<String> getKeys() {
        return this.mapConfig.keySet();
    }
    public boolean containsKey(String key) {
        return mapConfig.containsKey(key);
    }
    protected Map<String, String> mapConfig;
    protected String template;
}
