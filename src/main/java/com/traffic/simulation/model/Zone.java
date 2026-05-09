package com.traffic.simulation.model;

import java.util.*;

/**
 * Αναπαριστά μία ολόκληρη ζώνη (Zone)
 * 
 * Μια ζώνη είναι ένα ορθογώνιο εμβαδόν που περιέχει κελιά του ίδιου τύπου.
 * 
 * Παράδειγμα χρήσης:
 * Zone residential = new Zone(1, ZoneType.RESIDENTIAL, new Position(10, 10), 30, 40);
 * residential.setPopulation(5000);
 * residential.addCell(new Position(11, 11));
 */
public class Zone {
    private final int zoneId;
    private final ZoneType type;
    private final Position topLeftCorner;
    private final int width;
    private final int height;
    private final Set<Position> cells;
    private int population;
    private double foodSupply;
    private double pollution;
    
    /**
     * Δημιουργεί νέα ζώνη
     * 
     * @param zoneId Το μοναδικό ID της ζώνης
     * @param type Ο τύπος της ζώνης
     * @param topLeftCorner Η πάνω αριστερή γωνία
     * @param width Το πλάτος της ζώνης
     * @param height Το ύψος της ζώνης
     */
    public Zone(int zoneId, ZoneType type, Position topLeftCorner, int width, int height) {
        if (zoneId < 0) {
            throw new IllegalArgumentException("Το zoneId πρέπει να είναι ≥ 0");
        }
        if (type == null) {
            throw new IllegalArgumentException("Ο τύπος ζώνης δεν μπορεί να είναι null");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Το πλάτος και ύψος πρέπει να είναι > 0");
        }
        
        this.zoneId = zoneId;
        this.type = type;
        this.topLeftCorner = topLeftCorner;
        this.width = width;
        this.height = height;
        this.cells = new HashSet<>();
        this.population = 0;
        this.foodSupply = 0;
        this.pollution = 0;
    }
    
    /**
     * Επιστρέφει το ID της ζώνης
     */
    public int getZoneId() {
        return zoneId;
    }
    
    /**
     * Επιστρέφει τον τύπο της ζώνης
     */
    public ZoneType getType() {
        return type;
    }
    
    /**
     * Επιστρέφει την πάνω αριστερή γωνία
     */
    public Position getTopLeftCorner() {
        return topLeftCorner;
    }
    
    /**
     * Επιστρέφει τη κάτω δεξιά γωνία
     */
    public Position getBottomRightCorner() {
        return new Position(
            topLeftCorner.getX() + width - 1,
            topLeftCorner.getY() + height - 1
        );
    }
    
    /**
     * Επιστρέφει το πλάτος της ζώνης
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Επιστρέφει το ύψος της ζώνης
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Επιστρέφει το εμβαδόν της ζώνης (πλάτος × ύψος)
     */
    public int getArea() {
        return width * height;
    }
    
    /**
     * Ελέγχει αν μια θέση ανήκει σε αυτή τη ζώνη
     * 
     * @param position Η θέση προς έλεγχο
     * @return true αν η θέση είναι μέσα στη ζώνη
     */
    public boolean contains(Position position) {
        int x = position.getX();
        int y = position.getY();
        int minX = topLeftCorner.getX();
        int minY = topLeftCorner.getY();
        int maxX = minX + width - 1;
        int maxY = minY + height - 1;
        
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
    
    /**
     * Προσθέτει κελί στη ζώνη
     */
    public void addCell(Position position) {
        if (!contains(position)) {
            throw new IllegalArgumentException(
                String.format("Η θέση %s δεν ανήκει στη ζώνη %d", position, zoneId)
            );
        }
        cells.add(position);
    }
    
    /**
     * Αφαιρεί κελί από τη ζώνη
     */
    public void removeCell(Position position) {
        cells.remove(position);
    }
    
    /**
     * Επιστρέφει το σύνολο των κελιών της ζώνης
     */
    public Set<Position> getCells() {
        return new HashSet<>(cells);
    }
    
    /**
     * Επιστρέφει το πλήθος των κελιών
     */
    public int getCellCount() {
        return cells.size();
    }
    
    /**
     * Ελέγχει αν ένα κελί είναι μέρος της ζώνης
     */
    public boolean hasCell(Position position) {
        return cells.contains(position);
    }
    
    /**
     * Ορίζει το πληθυσμό της ζώνης
     * Χρησιμοποιείται μόνο για κατοικήσιμες ζώνες
     */
    public void setPopulation(int population) {
        if (population < 0) {
            throw new IllegalArgumentException("Ο πληθυσμός δεν μπορεί να είναι αρνητικός");
        }
        this.population = population;
    }
    
    /**
     * Επιστρέφει τον πληθυσμό της ζώνης
     */
    public int getPopulation() {
        return population;
    }
    
    /**
     * Ορίζει την εφοδιασμό τροφίμων (FOOD)
     */
    public void setFoodSupply(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Η εφοδιασμός δεν μπορεί να είναι αρνητική");
        }
        this.foodSupply = amount;
    }
    
    /**
     * Αυξάνει την εφοδιασμό τροφίμων
     */
    public void addFoodSupply(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική");
        }
        this.foodSupply += amount;
    }
    
    /**
     * Μειώνει την εφοδιασμό τροφίμων
     */
    public void consumeFoodSupply(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική");
        }
        this.foodSupply = Math.max(0, this.foodSupply - amount);
    }
    
    /**
     * Επιστρέφει την εφοδιασμό τροφίμων
     */
    public double getFoodSupply() {
        return foodSupply;
    }
    
    /**
     * Ορίζει τη ρύπανση (POLLUTION)
     */
    public void setPollution(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Η ρύπανση δεν μπορεί να είναι αρνητική");
        }
        this.pollution = amount;
    }
    
    /**
     * Αυξάνει τη ρύπανση
     */
    public void addPollution(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική");
        }
        this.pollution += amount;
    }
    
    /**
     * Μειώνει τη ρύπανση (καθαρισμός)
     */
    public void removePollution(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική");
        }
        this.pollution = Math.max(0, this.pollution - amount);
    }
    
    /**
     * Επιστρέφει τη ρύπανση
     */
    public double getPollution() {
        return pollution;
    }
    
    /**
     * Επιστρέφει το ποσοστό κατάληψης της ζώνης (κελιά/εμβαδόν)
     */
    public double getOccupancyPercentage() {
        return (double) cells.size() / getArea() * 100;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Zone{id=%d, type=%s, area=%d, cells=%d, pop=%d, food=%.1f, pollution=%.1f}",
            zoneId, type.getDisplayName(), getArea(), cells.size(), 
            population, foodSupply, pollution
        );
    }
}
