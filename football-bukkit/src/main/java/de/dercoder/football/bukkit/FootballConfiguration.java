package de.dercoder.football.bukkit;

public final class FootballConfiguration {
    private String footballTextureValue;
    private String footballTextureSignature;
    private String intermediateScoreMessage;
    private String scoreMainMessage;
    private String scoreSecondaryMessage;

    FootballConfiguration() {

    }

    FootballConfiguration(
            String footballTextureValue,
            String footballTextureSignature,
            String intermediateScoreMessage,
            String scoreMainMessage,
            String scoreSecondaryMessage
    ) {
        this.footballTextureValue = footballTextureValue;
        this.footballTextureSignature = footballTextureSignature;
        this.intermediateScoreMessage = intermediateScoreMessage;
        this.scoreMainMessage = scoreMainMessage;
        this.scoreSecondaryMessage = scoreSecondaryMessage;
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

    public String getIntermediateScoreMessage() {
        return intermediateScoreMessage;
    }

    public String parseIntermediateScoreMessage(
            int firstScore,
            int secondScore
    ) {
        return parseScoreMessage(
                intermediateScoreMessage,
                "",
                firstScore,
                secondScore
        );
    }

    public void setIntermediateScoreMessage(String intermediateScoreMessage) {
        this.intermediateScoreMessage = intermediateScoreMessage;
    }

    public String getScoreMainMessage() {
        return scoreMainMessage;
    }

    public String parseScoreMainMessage(
            String scorer,
            int firstScore,
            int secondScore
    ) {
        return parseScoreMessage(
                scoreMainMessage,
                scorer,
                firstScore,
                secondScore
        );
    }

    public void setScoreMainMessage(String scoreMainMessage) {
        this.scoreMainMessage = scoreMainMessage;
    }

    public String getScoreSecondaryMessage() {
        return scoreSecondaryMessage;
    }

    public String parseScoreSecondaryMessage(
            String scorer,
            int firstScore,
            int secondScore
    ) {
        return parseScoreMessage(
                scoreSecondaryMessage,
                scorer,
                firstScore,
                secondScore
        );
    }

    public void setScoreSecondaryMessage(String scoreSecondaryMessage) {
        this.scoreSecondaryMessage = scoreSecondaryMessage;
    }

    private String parseScoreMessage(
            String message,
            String scorer,
            int firstScore,
            int secondScore
    ) {
        return message
                .replace("&", "§")
                .replace("%scorer%", scorer)
                .replace("%firstScore%", String.valueOf(firstScore))
                .replace("%secondScore%", String.valueOf(secondScore));
    }
}
