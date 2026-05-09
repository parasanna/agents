package com.traffic.simulation.model;

import com.traffic.simulation.utils.Constants;

/**
 * Αναπαριστά μία θέση στο Grid-World
 * 
 * Παράδειγμα χρήσης:
 * Position p1 = new Position(10, 20);
 * Position p2 = new Position(15, 25);
 * int manhattan = p1.manhattanDistance(p2); // 10
 * int euclidean = p1.euclideanDistance(p2); // 7 (περίπου)
 */
public class Position {
    private final int x;
    private final int y;
    
    /**
     * Δημιουργεί νέα θέση στο grid
     * 
     * @param x Η x-συντεταγμένη (0 έως 199)
     * @param y Η y-συντεταγμένη (0 έως 199)
     * @throws IllegalArgumentException αν οι συντεταγμένες είναι έξω από τα όρια
     */
    public Position(int x, int y) {
        if (!isValidCoordinate(x, y)) {
            throw new IllegalArgumentException(
                String.format("Άκυρες συντεταγμένες: (%d, %d). Πρέπει να είναι 0-%d",
                    x, y, Constants.GRID_WIDTH - 1)
            );
        }
        this.x = x;
        this.y = y;
    }
    
    /**
     * Επιστρέφει τη x-συντεταγμένη
     */
    public int getX() {
        return x;
    }
    
    /**
     * Επιστρέφει τη y-συντεταγμένη
     */
    public int getY() {
        return y;
    }
    
    /**
     * Υπολογίζει τη Manhattan απόσταση (4-γειτονιά)
     * Χρησιμοποιείται για πραγματικές αποστάσεις στο δρόμο
     * 
     * @param other Η άλλη θέση
     * @return Η Manhattan απόσταση
     */
    public int manhattanDistance(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
    
    /**
     * Υπολογίζει τη Euclidean απόσταση (ευθύγραμμη)
     * Χρησιμοποιείται για heuristic στο A* pathfinding
     * 
     * @param other Η άλλη θέση
     * @return Η Euclidean απόσταση
     */
    public double euclideanDistance(Position other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Υπολογίζει τη Chebyshev απόσταση (8-γειτονιά)
     * 
     * @param other Η άλλη θέση
     * @return Η Chebyshev απόσταση
     */
    public int chebyshevDistance(Position other) {
        return Math.max(Math.abs(this.x - other.x), Math.abs(this.y - other.y));
    }
    
    /**
     * Ελέγχει αν είναι γειτονικές θέσεις (4-γειτονιά)
     * 
     * @param other Η άλλη θέση
     * @return true αν οι θέσεις είναι adjacent
     */
    public boolean isAdjacent(Position other) {
        return this.manhattanDistance(other) == 1;
    }
    
    /**
     * Ελέγχει αν είναι η ίδια θέση
     * 
     * @param other Η άλλη θέση
     * @return true αν είναι ίδιες
     */
    public boolean isSame(Position other) {
        return this.x == other.x && this.y == other.y;
    }
    
    /**
     * Ελέγχει αν η θέση είναι έγκυρη (μέσα στο grid)
     * 
     * @return true αν η θέση είναι έγκυρη
     */
    public boolean isValid() {
        return isValidCoordinate(this.x, this.y);
    }
    
    /**
     * Επιστρέφει τη γειτονική θέση σε συγκεκριμένη κατεύθυνση
     * 
     * @param direction NORTH, SOUTH, EAST, WEST
     * @return Η νέα Position ή null αν είναι έξω από τα όρια
     */
    public Position getNeighbor(Direction direction) {
        int newX = x;
        int newY = y;
        
        switch (direction) {
            case NORTH -> newY--;
            case SOUTH -> newY++;
            case EAST -> newX++;
            case WEST -> newX--;
        }
        
        if (isValidCoordinate(newX, newY)) {
            return new Position(newX, newY);
        }
        return null;
    }
    
    /**
     * Επιστρέφει ολόκληρη λίστα γειτονικών θέσεων (4-γειτονιά)
     * 
     * @return Πίνακας με έγκυρες γειτονικές θέσεις
     */
    public Position[] getNeighbors() {
        java.util.List<Position> neighbors = new java.util.ArrayList<>();
        
        for (Direction dir : Direction.values()) {
            Position neighbor = getNeighbor(dir);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }
        
        return neighbors.toArray(new Position[0]);
    }
    
    /**
     * Στατική μέθοδος για έλεγχο έγκυρης συντεταγμένης
     */
    private static boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < Constants.GRID_WIDTH &&
               y >= 0 && y < Constants.GRID_HEIGHT;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
    
    /**
     * Κατευθύνσεις κίνησης
     */
    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }
}
