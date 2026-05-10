package com.traffic.simulation;

import com.traffic.simulation.model.GridWorld;
import com.traffic.simulation.model.Cell;
import com.traffic.simulation.model.Position;
import com.traffic.simulation.model.Vehicle;
import com.traffic.simulation.utils.Constants;
import com.traffic.simulation.utils.PathFinder;
import com.traffic.simulation.utils.WorldGenerator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrafficSimulationApp extends Application {

    private GridWorld world;
    private Random rand = new Random();
    private List<Position> roadCells = new ArrayList<>();
    
    // --- Ρυθμίσεις Σταδιακής Εισαγωγής (Βάσει Προδιαγραφών) ---
    private final int TARGET_VEHICLES = 500;
    private int vehiclesSpawned = 0;

    @Override
    public void start(Stage primaryStage) {
        world = new GridWorld();
        
        // Φτιάχνουμε την πόλη
        WorldGenerator.generateCity(world);

        // Βρίσκουμε και αποθηκεύουμε όλα τα κελιά των δρόμων
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell c = world.getCellAt(x, y);
                if (c != null && c.isRoad()) {
                    roadCells.add(new Position(x, y));
                }
            }
        }

        int canvasWidth = Constants.GRID_WIDTH * Constants.CELL_SIZE_PIXELS;
        int canvasHeight = Constants.GRID_HEIGHT * Constants.CELL_SIZE_PIXELS;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Ζωγραφίζουμε το αρχικό (άδειο) πλέγμα για να μην έχουμε λευκή οθόνη
        drawWorld(gc);

        // Ο Κινητήρας του Παιχνιδιού (Game Loop)
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // Τρέχουμε ένα "tick" κάθε 100 milliseconds
                if (now - lastUpdate >= 100_000_000) {
                    
                    spawnVehiclesDynamically(); // 1. Γέννηση νέων οχημάτων
                    updateSimulation();         // 2. Υπολογισμός κίνησης / GPS
                    drawWorld(gc);              // 3. Ζωγραφική στην οθόνη
                    
                    lastUpdate = now;
                }
            }
        };

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setTitle("Σύστημα Ελέγχου Κυκλοφορίας - Ομαλή Ροή (500 Οχήματα)");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        timer.start(); // Εδώ ξεκινάει η προσομοίωση!
    }

    /**
     * Εισάγει σταδιακά τα αυτοκίνητα για να μην κρασάρει το σύστημα,
     * προσομοιώνοντας τους κατοίκους που βγαίνουν από τα σπίτια τους.
     */
    private void spawnVehiclesDynamically() {
        if (vehiclesSpawned >= TARGET_VEHICLES) return;

        int spawnRatePerTick = 2; // Γεννάμε 2 νέα αυτοκίνητα σε κάθε καρέ (20/δευτερόλεπτο)

        for (int i = 0; i < spawnRatePerTick; i++) {
            if (vehiclesSpawned >= TARGET_VEHICLES) break;
            
            Position startPos = roadCells.get(rand.nextInt(roadCells.size()));
            Vehicle v = new Vehicle("V" + vehiclesSpawned, Vehicle.VehicleCategory.INTERNAL, startPos);
            world.getVehicles().add(v);
            vehiclesSpawned++;
        }
    }

    /**
     * Ελέγχει την κίνηση των οχημάτων και βρίσκει διαδρομές με A*
     */
    /**
     * Ελέγχει την κίνηση των οχημάτων και βρίσκει διαδρομές με τον νέο, γρήγορο A*
     */
    private void updateSimulation() {
        int pathsCalculatedThisTick = 0; 
        
        for (Vehicle v : world.getVehicles()) {
            if (!v.hasPath()) {
                // Τώρα που ο A* είναι αστραπιαίος, επιτρέπουμε έως και 30 υπολογισμούς ανά 1/10 του δευτερολέπτου!
                if (pathsCalculatedThisTick < 30) {
                    Position currentPos = v.getCurrentPosition();
                    Position targetPos = roadCells.get(rand.nextInt(roadCells.size()));
                    
                    if (currentPos.equals(targetPos)) continue;
                    
                    List<Position> newPath = PathFinder.findPath(world, currentPos, targetPos);
                    
                    if (newPath != null && !newPath.isEmpty()) {
                        v.setPath(newPath);
                    }
                    pathsCalculatedThisTick++; 
                }
            } else {
                v.moveAlongPath(); 
            }
        }
    }

    private void drawWorld(GraphicsContext gc) {
        // Ζωγραφίζουμε Κτίρια και Δρόμους
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCellAt(x, y);
                if (cell != null) {
                    gc.setFill(Color.web(cell.getColorForVisualization()));
                    gc.fillRect(x * Constants.CELL_SIZE_PIXELS, y * Constants.CELL_SIZE_PIXELS, 
                                Constants.CELL_SIZE_PIXELS, Constants.CELL_SIZE_PIXELS);
                }
            }
        }

        // Ζωγραφίζουμε Οχήματα (Κόκκινες κουκκίδες)
        gc.setFill(Color.RED); 
        for (Vehicle v : world.getVehicles()) {
            Position p = v.getCurrentPosition();
            gc.fillOval(p.getX() * Constants.CELL_SIZE_PIXELS + 1, 
                        p.getY() * Constants.CELL_SIZE_PIXELS + 1, 
                        Constants.CELL_SIZE_PIXELS - 2, 
                        Constants.CELL_SIZE_PIXELS - 2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}