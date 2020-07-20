package de.dercoder.football.bukkit;

import com.google.inject.Guice;
import com.google.inject.Inject;

import de.dercoder.football.bukkit.configuration.FootballConfigurationRepository;
import de.dercoder.football.bukkit.football.FootballTrigger;
import de.dercoder.football.bukkit.goal.FootballGoalTrigger;
import de.dercoder.football.bukkit.stadium.FootballStadiumRepository;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FootballPlugin extends JavaPlugin {
  @Inject private FootballConfigurationRepository footballConfigurationRepository;
  @Inject private FootballStadiumRepository footballStadiumRepository;
  @Inject private PluginManager pluginManager;
  @Inject private FootballCommand footballCommand;
  @Inject private FootballTrigger footballTrigger;
  @Inject private FootballGoalTrigger footballGoalTrigger;

  @Override
  public void onEnable() {
    saveDefaultResources();
    var module = FootballModule.withPlugin(this);
    var injector = Guice.createInjector(module);
    injector.injectMembers(this);
    loadFootballConfiguration();
    loadStadiums();
    registerCommands();
    registerListeners();
  }

  private void saveDefaultResources() {
    saveResource("configuration.yml", false);
    saveResource("stadiums.yml", false);
  }

  private void loadFootballConfiguration() {
    footballConfigurationRepository.load();
  }

  private void loadStadiums() {
    footballStadiumRepository.loadAll();
  }

  private void registerCommands() {
    var footballPluginCommand = getCommand("football");
    footballPluginCommand.setExecutor(footballCommand);
  }

  private void registerListeners() {
    pluginManager.registerEvents(footballTrigger, this);
    pluginManager.registerEvents(footballGoalTrigger, this);
  }

  @Override
  public void onDisable() {
    saveFootballConfiguration();
    saveStadiums();
  }

  private void saveFootballConfiguration() {
    footballConfigurationRepository.save();
  }

  private void saveStadiums() {
    footballStadiumRepository.saveAll();
  }
}
