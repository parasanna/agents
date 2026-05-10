/**
 * Υλοποίηση του αλγορίθμου A* (A-Star) για την εύρεση της συντομότερης
 * διαδρομής μεταξύ δύο σημείων στο GridWorld.
 */
package com.traffic.simulation.utils;

import com.traffic.simulation.model.Cell;
import com.traffic.simulation.model.GridWorld;
import com.traffic.simulation.model.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class PathFinder {

    private static class Node implements Comparable<Node> {
        Position position;
        Node parent;
        double gCost;
        double hCost;

        Node(Position position, Node parent, double gCost, double hCost) {
            this.position = position;
            this.parent = parent;
            this.gCost = gCost;
            this.hCost = hCost;
        }

        double getFCost() { return gCost + hCost; }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.getFCost(), other.getFCost());
        }
    }

    public static List<Position> findPath(GridWorld world, Position start, Position target) {
        if (!isRoadAt(world, start) || !isRoadAt(world, target)) return null;

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        // ΥΠΕΡ-ΟΠΤΙΜΟΠΟΙΗΣΗ: Χρησιμοποιούμε 2D πίνακα για ταχύτατο έλεγχο O(1) αντί για αργά HashSets!
        boolean[][] closedSet = new boolean[world.getWidth()][world.getHeight()];

        openSet.add(new Node(start, null, 0, start.manhattanDistance(target)));
        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            Position cPos = current.position;

            // Αν φτάσαμε στο στόχο, επιστρέφουμε τη διαδρομή
            if (cPos.equals(target)) {
                return retracePath(current);
            }

            // Αν έχουμε ήδη επισκεφτεί αυτό το κελί με καλύτερο τρόπο, το προσπερνάμε! (Αυτό σώζει τον επεξεργαστή)
            if (closedSet[cPos.getX()][cPos.getY()]) continue;
            closedSet[cPos.getX()][cPos.getY()] = true;

            // Ελέγχουμε τους γείτονες
            for (int[] dir : directions) {
                int nx = cPos.getX() + dir[0];
                int ny = cPos.getY() + dir[1];

                if (world.isValidPosition(nx, ny) && !closedSet[nx][ny]) {
                    Cell cell = world.getCellAt(nx, ny);
                    if (cell != null && cell.isRoad()) {
                        Position neighborPos = new Position(nx, ny);
                        Node neighborNode = new Node(
                            neighborPos,
                            current,
                            current.gCost + 1,
                            neighborPos.manhattanDistance(target)
                        );
                        openSet.add(neighborNode);
                    }
                }
            }
        }
        return null; // Αν δεν βρεθεί δρόμος (π.χ. είναι εγκλωβισμένο) επιστρέφει null ακαριαία!
    }

    private static boolean isRoadAt(GridWorld world, Position pos) {
        Cell cell = world.getCellAt(pos);
        return cell != null && cell.isRoad();
    }

    private static List<Position> retracePath(Node endNode) {
        List<Position> path = new ArrayList<>();
        Node current = endNode;
        while (current != null) {
            path.add(current.position);
            current = current.parent;
        }
        Collections.reverse(path);
        if (!path.isEmpty()) path.remove(0); // Βγάζουμε το αρχικό κελί
        return path;
    }
}