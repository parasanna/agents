package com.traffic.simulation.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Αναπαριστά έναν δρόμο στον Grid-World.
 */
public class Road {
    
    public enum RoadType {
        R1_AVENUE,    // Μεγάλες Αρτηρίες με δύο λωρίδες ανά κατεύθυνση (15%) [cite: 32, 109]
        R2_STREET,    // Δρόμοι με μία λωρίδα ανά κατεύθυνση (70%) [cite: 33, 109]
        R3_ONE_WAY    // Μονόδρομοι (15%) [cite: 33, 109]
    }

    private String id;
    private RoadType type;
    private Set<Cell> roadCells; // Τα κελιά πλέγματος που ανήκουν σε αυτόν τον δρόμο [cite: 110]

    public Road(String id, RoadType type) {
        this.id = id;
        this.type = type;
        this.roadCells = new HashSet<>();
    }

    public void addCell(Cell cell) {
        if (cell != null) {
            roadCells.add(cell);
            // Ενημερώνουμε και το ίδιο το κελί ότι ανήκει σε αυτόν τον δρόμο
            // cell.setRoadId(this.id); // Αν το Cell υποστηρίζει setRoadId(String)
        }
    }

    /**
     * Ελέγχει αν ο δρόμος πληροί το ελάχιστο εμβαδό RAT (10 cells).
     */
    public boolean meetsMinimumSizeRequirement() {
        return roadCells.size() >= 10;
    }

    // Getters
    public String getId() { return id; }
    public RoadType getType() { return type; }
    public Set<Cell> getRoadCells() { return roadCells; }
}