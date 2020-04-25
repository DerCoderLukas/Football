package de.dercoder.football.bukkit;

public final class FootballConfiguration {
    private String footballTextureValue;
    private String footballTextureSignature;

    FootballConfiguration() {

    }

    FootballConfiguration(
            String footballTextureValue,
            String footballTextureSignature
    ) {
        this.footballTextureValue = footballTextureValue;
        this.footballTextureSignature = footballTextureSignature;
    }

    public String getFootballTextureValue() {
        return footballTextureValue;
    }

    public void setFootballTextureValue(
            String footballTextureValue
    ) {
        this.footballTextureValue = footballTextureValue;
    }

    public String getFootballTextureSignature() {
        return footballTextureSignature;
    }

    public void setFootballTextureSignature(
            String footballTextureSignature
    ) {
        this.footballTextureSignature = footballTextureSignature;
    }
}
