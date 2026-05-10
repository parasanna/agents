package com.traffic.simulation.utils;

import com.traffic.simulation.model.Cell;
import com.traffic.simulation.model.GridWorld;
import com.traffic.simulation.model.ZoneType;
import com.traffic.simulation.model.Position;
import com.traffic.simulation.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Αναλαμβάνει την αυτόματη δημιουργία της πόλης.
 * Χαράζει δρόμους και αναθέτει τύπους ζωνών στα οικοδομικά τετράγωνα.
 */
public class WorldGenerator {
    
    public static void generateCity(GridWorld world) {
        int width = world.getWidth();
        int height = world.getHeight();
        
        // Μέγεθος Οικοδομικού Τετραγώνου: 20x20 κελιά
        int blockSize = 20; 

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = world.getCellAt(x, y);
                if (cell == null) continue;

                // 1. Σχεδιασμός Δρόμων: Κάθε 20 κελιά φτιάχνουμε δρόμο πάχους 2 κελιών
                if (x % blockSize == 0 || x % blockSize == 1 || y % blockSize == 0 || y % blockSize == 1) {
                    cell.setType(ZoneType.ROAD);
                } else {
                    // 2. Ανάθεση Ζωνών στα Τετράγωνα
                    // Βρίσκουμε σε ποιο "μπλοκ" ανήκει το τρέχον κελί
                    int blockX = x / blockSize;
                    int blockY = y / blockSize;
                    
                    // Δίνουμε το ίδιο χρώμα/τύπο σε όλο το μπλοκ χρησιμοποιώντας ένα Random seed
                    Random blockRandom = new Random(blockX * 1000L + blockY); 
                    double randValue = blockRandom.nextDouble();
                    
                    // Αναθέτουμε τα ποσοστά ακριβώς όπως τα ζητάει το PDF (Σελίδα 4)
                    if (randValue < 0.25) {
                        cell.setType(ZoneType.RESIDENTIAL); // 25% Κατοικίες (Κίτρινο)
                    } else if (randValue < 0.50) {
                        cell.setType(ZoneType.OFFICES);     // 25% Γραφεία (Μπλε)
                    } else if (randValue < 0.70) {
                        cell.setType(ZoneType.MARKETS);     // 20% Αγορές (Πράσινο)
                    } else if (randValue < 0.90) {
                        cell.setType(ZoneType.LEISURE);     // 20% Διασκέδαση (Πορτοκαλί)
                    } else {
                        cell.setType(ZoneType.OTHER);       // 10% Λοιπές (Μωβ)
                    }
                }
            }
        }
    }
    /**
     * Ρίχνει τυχαία αυτοκίνητα πάνω στο οδικό δίκτυο για το πρώτο οπτικό τεστ.
     */
    public static void populateVehicles(GridWorld world, int numberOfVehicles) {
        List<Position> roadCells = new ArrayList<>();
        
        // Βρίσκουμε όλα τα κελιά που είναι δρόμοι
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCellAt(x, y);
                if (cell != null && cell.isRoad()) {
                    roadCells.add(new Position(x, y));
                }
            }
        }

        // Φτιάχνουμε τα οχήματα
        Random rand = new Random();
        for (int i = 0; i < numberOfVehicles; i++) {
            if (roadCells.isEmpty()) break;
            // Διαλέγουμε μια τυχαία θέση δρόμου
            Position startPos = roadCells.get(rand.nextInt(roadCells.size()));
            Vehicle v = new Vehicle("V" + i, Vehicle.VehicleCategory.INTERNAL, startPos);
            world.getVehicles().add(v);
        }
    }
}