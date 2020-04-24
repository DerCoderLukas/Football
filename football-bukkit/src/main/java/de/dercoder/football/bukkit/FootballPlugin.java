package de.dercoder.football.bukkit;

import com.google.inject.Guice;
import com.google.inject.Inject;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FootballPlugin extends JavaPlugin {
    @Inject
    private FootballStadionRepository footballStadionRepository;
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
        loadStadions();
        registerCommands();
        registerListeners();
    }

    private void saveDefaultResources() {
        saveResource("stadion.yml", false);
    }

    private void loadStadions() {
        footballStadionRepository.loadAll();
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
        saveStadions();
    }

    private void saveStadions() {
        footballStadionRepository.saveAll();
    }
}
