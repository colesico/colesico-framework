package colesico.framework.example.ioc.multiplugin;

import colesico.framework.ioc.Polysupplier;

import java.util.ArrayList;
import java.util.List;

public class MainBean {

    private final Polysupplier<PluginInterface> plugins;

    public MainBean(Polysupplier<PluginInterface> plugins) {
        this.plugins = plugins;
    }

    public List<String> getPluginsInfo() {
        if (plugins.isNotEmpty()) {
            final List<String> result = new ArrayList<>();
            plugins.forEach(plugin -> result.add(plugin.getInfo()), null);
            return result;
        } else {
            return null;
        }
    }
}
