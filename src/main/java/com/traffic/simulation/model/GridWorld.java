package com.traffic.simulation.model;

import com.traffic.simulation.utils.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κύρια κλάση που αναπαριστά τον 2D κόσμο της προσομοίωσης.
 * Διαχειρίζεται το πλέγμα 200x200 σύμφωνα με τις προδιαγραφές του PDF.
 */
public class GridWorld {

    private Cell[][] grid;
    private int width;
    private int height;
    private List<Vehicle> vehicles;

    public GridWorld() {
        this.width = Constants.GRID_WIDTH;
        this.height = Constants.GRID_HEIGHT;
        this.grid = new Cell[width][height];
        this.vehicles = new ArrayList<>();
        initializeGrid();
    }

    /**
     * Αρχικοποιεί το πλέγμα γεμίζοντάς το με "Κενά" (EMPTY) κελιά.
     * Αργότερα ο WorldGenerator θα αλλάξει αυτούς τους τύπους σε Δρόμους και Ζώνες.
     */
    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Δημιουργούμε τη θέση και φτιάχνουμε ένα νέο κελί τύπου EMPTY
                Position pos = new Position(x, y);
                // Υποθέτουμε ότι ο κατασκευαστής του Cell παίρνει (Position, ZoneType)
                grid[x][y] = new Cell(pos, ZoneType.EMPTY); 
            }
        }
    }

    /**
     * Επιστρέφει το κελί σε μια συγκεκριμένη συντεταγμένη (x, y).
     * @return Το Cell ή null αν η συντεταγμένη είναι εκτός χάρτη
     */
    public Cell getCellAt(int x, int y) {
        if (isValidPosition(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    /**
     * Επιστρέφει το κελί σε μια συγκεκριμένη Θέση (Position).
     */
    public Cell getCellAt(Position position) {
        if (position != null && isValidPosition(position.getX(), position.getY())) {
            return grid[position.getX()][position.getY()];
        }
        return null;
    }

    /**
     * Ελέγχει αν μια συντεταγμένη βρίσκεται μέσα στα όρια 200x200 του χάρτη.
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    // --- Βασικοί Getters ---
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Cell[][] getGrid() { return grid; }
    public List<Vehicle> getVehicles() { return vehicles; }
}