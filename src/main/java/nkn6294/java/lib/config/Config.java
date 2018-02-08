package nkn6294.java.lib.config;

import java.util.Set;

public interface Config {
    public String getConfigValue(String key);
    public boolean addConfig(String key, String value);
    public Set<String> getKeys();
}
