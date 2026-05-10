package com.traffic.simulation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Αναπαριστά ένα όχημα στην προσομοίωση.
 */
public class Vehicle {
    
    public enum VehicleCategory {
        INTERNAL,               // Εσωτερική κυκλοφορία (Κάτοικοι - P1)
        TRANSIT,                // Διερχόμενη κυκλοφορία (P2)
        FOOD_SUPPLY,            // Παροχή Προμηθειών (Logistics)
        POLLUTION_COLLECTION    // Καθαρισμός Μόλυνσης
    }

    public enum VehicleState {
        IDLE,           // Σε ακινησία / Παρκαρισμένο
        MOVING,         // Εν κινήσει
        BROKEN_DOWN,    // Σε βλάβη
        IN_ACCIDENT     // Εμπλεκόμενο σε ατύχημα
    }

    private String id;
    private VehicleCategory category;
    private VehicleState state;
    private Position currentPosition;
    private int currentSpeed; 
    
    // Στατιστικά για τον υπολογισμό των μετρικών
    private long entryTime;
    private long exitTime;
    private int cellsTraveled;

    // --- ΝΕΑ ΠΕΔΙΑ ΓΙΑ ΤΗΝ ΕΞΥΠΝΗ ΠΛΟΗΓΗΣΗ (A*) ---
    private List<Position> currentPath;
    private Position destination;

    private static final Random random = new Random();

    public Vehicle(String id, VehicleCategory category, Position currentPosition) {
        this.id = id;
        this.category = category;
        this.currentPosition = currentPosition;
        this.state = VehicleState.IDLE;
        this.cellsTraveled = 0;
        this.currentPath = new ArrayList<>();
        assignRandomSpeed();
    }

    /**
     * Αναθέτει τυχαία ταχύτητα στο όχημα (1 έως 5 cells/tick).
     */
    public void assignRandomSpeed() {
        this.currentSpeed = 1 + random.nextInt(5);
    }

    // --- ΜΕΘΟΔΟΙ ΠΛΟΗΓΗΣΗΣ ---

    public boolean hasPath() {
        return currentPath != null && !currentPath.isEmpty();
    }

    public void setPath(List<Position> path) {
        this.currentPath = path;
        if (path != null && !path.isEmpty()) {
            this.destination = path.get(path.size() - 1); // Ο τελικός προορισμός
            this.state = VehicleState.MOVING;
        } else {
            this.state = VehicleState.IDLE;
            this.destination = null;
        }
    }

    /**
     * Μετακινεί το όχημα πάνω στο μονοπάτι του, καταναλώνοντας τόσα κελιά όσα η ταχύτητά του.
     */
    public void moveAlongPath() {
        if (!hasPath() || state != VehicleState.MOVING) return;

        // Το όχημα κάνει τόσα βήματα όση είναι η ταχύτητά του, ή όσα έμειναν για τον προορισμό
        int stepsToTake = Math.min(currentSpeed, currentPath.size());

        for (int i = 0; i < stepsToTake; i++) {
            // Αφαιρούμε το επόμενο κελί από τη λίστα και πάμε σε αυτό
            Position nextStep = currentPath.remove(0);
            this.currentPosition = nextStep;
            this.cellsTraveled++; // Μετράμε το μήκος διαδρομής
        }

        // Αν φτάσαμε στο τέλος της διαδρομής
        if (currentPath.isEmpty()) {
            this.state = VehicleState.IDLE;
            this.destination = null;
            // Επιλέγουμε νέα τυχαία ταχύτητα για το επόμενο επεισόδιο κίνησης
            assignRandomSpeed(); 
        }
    }

    // Παραδοσιακή μέθοδος (μπορεί να χρησιμοποιηθεί για μεμονωμένα βήματα)
    public void moveTo(Position newPosition) {
        this.currentPosition = newPosition;
        this.cellsTraveled++;
    }

    // Getters and Setters
    public String getId() { return id; }
    public VehicleCategory getCategory() { return category; }
    public VehicleState getState() { return state; }
    public void setState(VehicleState state) { this.state = state; }
    public Position getCurrentPosition() { return currentPosition; }
    public int getCurrentSpeed() { return currentSpeed; }
    public int getCellsTraveled() { return cellsTraveled; }
    public Position getDestination() { return destination; }
}