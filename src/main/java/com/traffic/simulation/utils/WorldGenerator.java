/**
 * Αναλαμβάνει την αυτόματη δημιουργία της πόλης.
 * Χαράζει δρόμους και αναθέτει τύπους ζωνών στα οικοδομικά τετράγωνα.
 */
package com.traffic.simulation.utils;

import com.traffic.simulation.model.Cell;
import com.traffic.simulation.model.GridWorld;
import com.traffic.simulation.model.Position;
import com.traffic.simulation.model.ZoneType;
import com.traffic.simulation.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenerator {

    private static final Random rand = new Random();

    public static void generateCity(GridWorld world) {
        int w = world.getWidth();
        int h = world.getHeight();

        // 1. Αρχικοποιούμε όλο τον χάρτη ως EMPTY (Λειτουργεί ως πεζοδρόμιο, τοίχος και κενός χώρος)
        fillRect(world, 0, 0, w, h, ZoneType.EMPTY);

        // 2. Χωρίζουμε τον κόσμο σε 8x8 'Οικόπεδα' των 25x25 κελιών (Συνολικά 64 Κτίρια)
        int parcelSize = 25;
        int numParcelsX = w / parcelSize;
        int numParcelsY = h / parcelSize;

        for (int row = 0; row < numParcelsY; row++) {
            for (int col = 0; col < numParcelsX; col++) {
                int startX = col * parcelSize;
                int startY = row * parcelSize;

                // Καθορισμός πάχους δρόμων:
                // Η 3η γραμμή και στήλη θα είναι η Κεντρική Λεωφόρος με 4 κελιά πλάτος.
                // Οι υπόλοιποι θα είναι Τοπικοί Δρόμοι με 2 κελιά πλάτος.
                boolean isMajorCol = (col == 3);
                boolean isMajorRow = (row == 3);
                int roadWidthX = isMajorCol ? 4 : 2;
                int roadWidthY = isMajorRow ? 4 : 2;

                int blockW = parcelSize - roadWidthX;
                int blockH = parcelSize - roadWidthY;

                // 3. Σχεδιασμός Δρόμων (ROAD) στα δεξιά και κάτω από το οικόπεδο
                fillRect(world, startX + blockW, startY, roadWidthX, parcelSize, ZoneType.ROAD); // Κάθετος
                fillRect(world, startX, startY + blockH, parcelSize, roadWidthY, ZoneType.ROAD); // Οριζόντιος

                // 4. Σχεδιασμός Κτιρίου (Αφήνουμε 1 κελί περιθώριο γύρω-γύρω για Πεζοδρόμιο)
                ZoneType bType = getRandomBuildingType();
                fillRect(world, startX + 1, startY + 1, blockW - 2, blockH - 2, bType);

                // 5. Σημείο Εισόδου/Εξόδου (Τοποθετείται τυχαία στο πεζοδρόμιο και βλέπει δρόμο)
                int entryX, entryY;
                if (rand.nextBoolean()) {
                    entryX = startX + blockW - 1; // Δεξιά πλευρά πεζοδρομίου
                    entryY = startY + 2 + rand.nextInt(blockH - 4);
                } else {
                    entryX = startX + 2 + rand.nextInt(blockW - 4);
                    entryY = startY + blockH - 1; // Κάτω πλευρά πεζοδρομίου
                }
                
                if (world.isValidPosition(entryX, entryY)) {
                    world.getCellAt(entryX, entryY).setType(ZoneType.ROAD);
                }
            }
        }

        // 6. Δημιουργία Τοίχου Περιμέτρου στα όρια του χάρτη 200x200
        for(int i = 0; i < w; i++) {
            world.getCellAt(i, 0).setType(ZoneType.EMPTY);
            world.getCellAt(i, h - 1).setType(ZoneType.EMPTY);
            world.getCellAt(0, i).setType(ZoneType.EMPTY);
            world.getCellAt(w - 1, i).setType(ZoneType.EMPTY);
        }

        // 7. Σπάσιμο του τοίχου για τα Σημεία Εισόδου/Εξόδου των Λεωφόρων (R1)
        int r1StartX = 3 * parcelSize + (parcelSize - 4); 
        int r1StartY = 3 * parcelSize + (parcelSize - 4); 
        
        for(int x = r1StartX; x < r1StartX + 4; x++) {
            world.getCellAt(x, 0).setType(ZoneType.ROAD);      // Πάνω πύλη
            world.getCellAt(x, h - 1).setType(ZoneType.ROAD);  // Κάτω πύλη
        }
        for(int y = r1StartY; y < r1StartY + 4; y++) {
            world.getCellAt(0, y).setType(ZoneType.ROAD);      // Αριστερή πύλη
            world.getCellAt(w - 1, y).setType(ZoneType.ROAD);  // Δεξιά πύλη
        }
    }

    /**
     * Επιστρέφει τυχαίο τύπο κτιρίου, διαβάζοντας τα ακριβή ποσοστά απευθείας από 
     * το αρχείο ZoneType.java (π.χ. 0.25 για Κατοικίες).
     */
    private static ZoneType getRandomBuildingType() {
        double r = rand.nextDouble();
        double cumulative = 0.0;
        
        for (ZoneType type : ZoneType.values()) {
            if (type.isInhabitable()) {
                cumulative += type.getPercentage();
                if (r <= cumulative) {
                    return type;
                }
            }
        }
        return ZoneType.RESIDENTIAL; // Ασφαλής επιλογή επιστροφής
    }

    /**
     * Γεννάει τα οχήματα μόνο πάνω στο οδικό δίκτυο (ROAD).
     */
    public static void populateVehicles(GridWorld world, int numberOfVehicles) {
        List<Position> roadCells = new ArrayList<>();
        
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCellAt(x, y);
                if (cell != null && cell.getType() == ZoneType.ROAD) {
                    roadCells.add(new Position(x, y));
                }
            }
        }
        
        for (int i = 0; i < numberOfVehicles; i++) {
            if (roadCells.isEmpty()) break;
            Position startPos = roadCells.get(rand.nextInt(roadCells.size()));
            Vehicle v = new Vehicle("V" + i, Vehicle.VehicleCategory.INTERNAL, startPos);
            world.getVehicles().add(v);
        }
    }

    /**
     * Βοηθητική μέθοδος για ασφαλές γέμισμα μιας περιοχής.
     */
    private static void fillRect(GridWorld world, int x, int y, int width, int height, ZoneType type) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                if (world.isValidPosition(i, j)) {
                    Cell c = world.getCellAt(i, j);
                    if (c != null) {
                        c.setType(type);
                    }
                }
            }
        }
    }
}