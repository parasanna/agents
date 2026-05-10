package com.traffic.simulation.model;

import com.traffic.simulation.utils.Constants;

/**
 * Αναπαριστά ένα μεμονωμένο κελί του Grid-World
 * 
 * Κάθε κελί έχει:
 * - Θέση (Position)
 * - Τύπο ζώνης (ZoneType)
 * - Κατάσταση (δρόμος, κενό, κατοικήσιμο)
 * - ID του δρόμου (αν είναι δρόμος)
 * 
 * Το κελί χρησιμοποιείται ως βασική δομή για:
 * - Pathfinding αλγορίθμους
 * - Κίνηση οχημάτων
 * - Ανάθεση πόρων (φαγητό, απόρρυψη)
 */
public class Cell {
    /** Θέση του κελιού στο grid */
    private final Position position;
    
    /** Τύπος ζώνης που βρίσκεται το κελί */
    private ZoneType type;
    
    /** Αν είναι δρόμος */
    private boolean isRoad;
    
    /** ID του δρόμου αν isRoad == true */
    private int roadId;
    
    /** ID της ζώνης που ανήκει αυτό το κελί (αν δεν είναι δρόμος) */
    private int zoneId;
    
    /**
     * Κατασκευαστής κελιού
     * 
     * @param position η θέση του κελιού
     * @param type ο τύπος ζώνης
     */
    public Cell(Position position, ZoneType type) {
        this.position = position;
        this.type = type;
        this.isRoad = (type == ZoneType.ROAD);
        this.roadId = -1;
        this.zoneId = -1;
    }
    
    /**
     * Επιστρέφει τη θέση του κελιού
     * 
     * @return η θέση
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Επιστρέφει τον τύπο ζώνης του κελιού
     * 
     * @return ο τύπος ζώνης
     */
    public ZoneType getType() {
        return type;
    }
    
    /**
     * Θέτει το νέο τύπο ζώνης του κελιού
     * 
     * @param type ο νέος τύπος ζώνης
     */
    public void setType(ZoneType type) {
        this.type = type;
        this.isRoad = (type == ZoneType.ROAD);
    }
    
    /**
     * Ελέγχει αν το κελί είναι δρόμος
     * 
     * @return true αν είναι δρόμος
     */
    public boolean isRoad() {
        return isRoad;
    }
    
    /**
     * Επιστρέφει το ID του δρόμου
     * 
     * @return το ID του δρόμου, ή -1 αν δεν είναι δρόμος
     */
    public int getRoadId() {
        return roadId;
    }
    
    /**
     * Θέτει το ID του δρόμου
     * 
     * @param roadId το νέο ID του δρόμου
     */
    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }
    
    /**
     * Επιστρέφει το ID της ζώνης
     * 
     * @return το ID της ζώνης, ή -1 αν δεν έχει ανατεθεί
     */
    public int getZoneId() {
        return zoneId;
    }
    
    /**
     * Θέτει το ID της ζώνης
     * 
     * @param zoneId το νέο ID της ζώνης
     */
    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }
    
    /**
     * Ελέγχει αν το κελί είναι κενό (άδειο)
     * 
     * @return true αν είναι κενό
     */
    public boolean isEmpty() {
        return type == ZoneType.EMPTY;
    }
    
    /**
     * Ελέγχει αν το κελί είναι διαβάσιμο από οχήματα
     * (δρόμος ή κενό χώρο)
     * 
     * @return true αν μπορεί να περάσει όχημα
     */
    public boolean isWalkable() {
        return isRoad || type == ZoneType.EMPTY;
    }
    
    /**
     * Ελέγχει αν το κελί είναι κατοικήσιμο
     * (περιέχει ζώνη που απαιτεί υπηρεσίες)
     * 
     * @return true αν είναι κατοικήσιμο
     */
    public boolean isInhabitable() {
        return type.isInhabitable();
    }
    
    /**
     * Ελέγχει αν το κελί απαιτεί συλλογή απορριμμάτων
     * 
     * @return true αν απαιτείται συλλογή
     */
    public boolean requiresWasteCollection() {
        return type.requiresWasteCollection();
    }
    
    /**
     * Ελέγχει αν το κελί απαιτεί εφοδιασμό
     * 
     * @return true αν απαιτείται εφοδιασμός
     */
    public boolean requiresSupply() {
        return type.requiresSupply();
    }
    
    /**
     * Επιστρέφει το χρώμα του κελιού για visualization
     * 
     * @return το RGB χρώμα
     */
    public String getColorForVisualization() {
        switch (type) {
            case RESIDENTIAL:
                return Constants.COLOR_RESIDENTIAL;
            case OFFICES:
                return Constants.COLOR_OFFICES;
            case MARKETS:
                return Constants.COLOR_MARKETS;
            case LEISURE:
                return Constants.COLOR_LEISURE;
            case OTHER:
                return Constants.COLOR_OTHER;
            case ROAD:
                return Constants.COLOR_ROAD;
            case EMPTY:
                return Constants.COLOR_EMPTY;
            default:
                return Constants.COLOR_EMPTY;
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Cell cell = (Cell) o;
        return position.equals(cell.position);
    }
    
    @Override
    public int hashCode() {
        return position.hashCode();
    }
    
    @Override
    public String toString() {
        return String.format("Cell{pos=%s, type=%s, isRoad=%b, roadId=%d, zoneId=%d}",
                position, type, isRoad, roadId, zoneId);
    }
}
