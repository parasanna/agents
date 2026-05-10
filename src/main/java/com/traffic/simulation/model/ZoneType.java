package com.traffic.simulation.model;

import com.traffic.simulation.utils.Constants;

public enum ZoneType {
    RESIDENTIAL(0.25, Constants.COLOR_RESIDENTIAL, "Κατοικίες"),
    OFFICES(0.25, Constants.COLOR_OFFICES, "Γραφεία Επιχειρήσεων"),
    MARKETS(0.20, Constants.COLOR_MARKETS, "Αγορές"),
    LEISURE(0.20, Constants.COLOR_LEISURE, "Διασκέδαση"),
    OTHER(0.10, Constants.COLOR_OTHER, "Λοιπές"),
    ROAD(0.0, Constants.COLOR_ROAD, "Δρόμος"),
    EMPTY(0.0, Constants.COLOR_EMPTY, "Κενό");

    private final double percentage;
    private final String hexColor; 
    private final String displayName;

    ZoneType(double percentage, String hexColor, String displayName) {
        this.percentage = percentage;
        this.hexColor = hexColor;
        this.displayName = displayName;
    }

    public double getPercentage() { return percentage; }
    public String getHexColor() { return hexColor; }
    public String getDisplayName() { return displayName; }

    // Βοηθητικές μέθοδοι
    public boolean isInhabitable() { return this != ROAD && this != EMPTY; }
    public boolean requiresSupply() { return this == MARKETS || this == LEISURE; }         // Ο  εφοδιασμός γίνεται μόνο σε Αγορές (3) και Διασκέδαση (4)
    public boolean requiresWasteCollection() { return this != ROAD && this != EMPTY; } // Τα απορρίμματα μαζεύονται από τις περιοχές 1, 2, 3, 4, 5
}