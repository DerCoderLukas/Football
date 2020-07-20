package de.dercoder.football.bukkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import com.google.common.base.Preconditions;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import de.dercoder.football.bukkit.game.FootballGameRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Singleton;

import java.nio.file.Path;

public final class FootballModule extends AbstractModule {
  private final JavaPlugin plugin;

  private FootballModule(JavaPlugin plugin) {
    this.plugin = plugin;
  }

  @Provides
  @Singleton
  PluginManager providePluginManager() {
    return plugin.getServer().getPluginManager();
  }

  private static final String FOOTBALL_PREFIX = "§8[§dFootball§8] §7";

  @Provides
  @Singleton
  @Named("prefix")
  String providePrefix() {
    return FOOTBALL_PREFIX;
  }

  @Provides
  @Singleton
  YAMLFactory provideYamlFactory() {
    return YAMLFactory.builder()
      .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
      .build();
  }

  @Provides
  @Singleton
  ObjectMapper provideObjectMapper(YAMLFactory yamlFactory) {
    return new ObjectMapper(yamlFactory);
  }

  private static final String STADIUMS_REPOSITORY_PATH = "stadiums.yml";

  @Provides
  @Singleton
  @Named("stadiumsPath")
  Path provideStadiumsPath() {
    return Path.of(
      plugin.getDataFolder().getAbsolutePath(),
      STADIUMS_REPOSITORY_PATH
    );
  }

  private static final String FOOTBALL_CONFIGURATION_PATH = "configuration.yml";

  @Provides
  @Singleton
  @Named("configurationPath")
  Path provideConfigurationPath() {
    return Path.of(
      plugin.getDataFolder().getAbsolutePath(),
      FOOTBALL_CONFIGURATION_PATH
    );
  }

  @Provides
  @Singleton
  FootballGameRegistry provideFootballGameRegistry() {
    return FootballGameRegistry.empty();
  }

  public static FootballModule withPlugin(JavaPlugin plugin) {
    Preconditions.checkNotNull(plugin);
    return new FootballModule(plugin);
  }
}
