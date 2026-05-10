package com.traffic.simulation.utils;

public class Constants {
    
    // Grid World
    public static final int GRID_WIDTH = 200;
    public static final int GRID_HEIGHT = 200;
    public static final int CELL_SIZE_PIXELS = 4;

    // Time
    public static final int TICK_DURATION_SECONDS = 3;
    public static final int SIMULATION_DURATION_TICKS = 7 * 24 * 60 * 20; // 1 εβδομάδα

    // Zones
    public static final int MAX_ZONES = 20;
    public static final double ZONE_PERCENTAGE_OF_GRID = 0.98;

    // Roads
    public static final double R1_PERCENTAGE = 0.15; // Μεγάλες Αρτηρίες (2 λωρίδες)
    public static final double R2_PERCENTAGE = 0.70; // Δρόμοι (1 λωρίδα)
    public static final double R3_PERCENTAGE = 0.15; // Μονόδρομοι

    // Vehicles
    public static final int MAX_VEHICLE_VELOCITY = 5; // cells/tick
    public static final int MIN_VEHICLE_VELOCITY = 0;

    // Logistics
    public static final int SUPPLY_VEHICLE_CAPACITY = 50;
    public static final int COLLECTION_VEHICLE_CAPACITY = 250;

    // Population
    public static final int RESIDENTIAL_POPULATION_P1 = 100000;
    public static final int INTERNAL_CIRCULATION_P2 = 24000;

    // UI Colors (Σε μορφή Hex για εύκολη χρήση στο JavaFX αργότερα)
    public static final String COLOR_RESIDENTIAL = "#FFFF00"; // Κίτρινο (Κτίριο Α) 
    public static final String COLOR_OFFICES = "#4169E1";     // Μπλε (Κτίριο Β) 
    public static final String COLOR_MARKETS = "#32CD32";     // Πράσινο (Κτίριο C) 
    public static final String COLOR_LEISURE = "#FFA500";     // Πορτοκαλί (Κτίριο D) 
    public static final String COLOR_OTHER = "#9370DB";       // Μωβ (Κτίριο Ε) 
    public static final String COLOR_EMPTY = "#ADFF2F";       // Λαχανί (Ελεύθερος χώρος) 
    public static final String COLOR_ROAD = "#000000";        // Μαύρο (Δρόμος/Οχήματα) [cite: 188]
    public static final String COLOR_SIDEWALK = "#008080";    // Πετρόλ (Πεζοδρόμιο) [cite: 186]
    public static final String COLOR_INTERSECTION = "#B8860B";// Σκούρο Κίτρινο (Διασταυρώσεις) [cite: 187]
}