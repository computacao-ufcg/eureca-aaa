package br.edu.ufcg.computacao.eureca.as.core;

import br.edu.ufcg.computacao.eureca.as.constants.ConfigurationPropertyKeys;
import br.edu.ufcg.computacao.eureca.as.core.role.SystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.role.plugins.DefaultSystemRolePlugin;
import br.edu.ufcg.computacao.eureca.as.core.systemidp.SystemIdentityProviderPlugin;

public class PluginInstantiator {
    private static ClassFactory classFactory = new ClassFactory();

    public static SystemIdentityProviderPlugin getSystemIdentityProviderPlugin() {
        String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.SYSTEM_IDENTITY_PROVIDER_PLUGIN_CLASS_KEY);
        return (SystemIdentityProviderPlugin) PluginInstantiator.classFactory.createPluginInstance(className);
    }
    
    public static SystemRolePlugin getSystemRolePlugin() {
        if (PropertiesHolder.getInstance().getProperties().containsKey(ConfigurationPropertyKeys.SYSTEM_ROLE_PLUGIN_CLASS_KEY)) {
            String className = PropertiesHolder.getInstance().getProperty(ConfigurationPropertyKeys.SYSTEM_ROLE_PLUGIN_CLASS_KEY);
            return (SystemRolePlugin) PluginInstantiator.classFactory.createPluginInstance(className);
        } else {
            return new DefaultSystemRolePlugin();
        }
    }
}
