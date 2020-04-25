package de.dercoder.football.bukkit;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FootballPlugin extends JavaPlugin {
    @Inject
    private FootballConfigurationRepository footballConfigurationRepository;
    @Inject
    private FootballStadiumRepository footballStadiumRepository;
    @Inject
    private PluginManager pluginManager;
    @Inject
    private FootballCommand footballCommand;
    @Inject
    private FootballTrigger footballTrigger;

    @Override
    public void onEnable() {
        saveDefaultResources();
        var module = FootballModule.withPlugin(this);
        var injector = Guice.createInjector(module);
        injector.injectMembers(this);
        loadFootballConfiguration();
        loadStadiumConfiguration();
        registerCommands();
        registerListeners();
    }

    private void saveDefaultResources() {
        saveResource("configuration.yml", false);
        saveResource("stadium.yml", false);
    }

    private void loadFootballConfiguration() {
        footballConfigurationRepository.load();
    }

    private void loadStadiumConfiguration() {
        footballStadiumRepository.load();
    }

    private void registerCommands() {
        var footballPluginCommand = getCommand("football");
        footballPluginCommand.setExecutor(footballCommand);
    }

    private void registerListeners() {
        pluginManager.registerEvents(footballTrigger, this);
    }

    @Override
    public void onDisable() {
        saveStadiumConfiguration();
    }

    private void saveStadiumConfiguration() {
        footballStadiumRepository.save();
    }
}
