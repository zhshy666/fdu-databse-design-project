package project.backend.Entity;

public class Bed {
    private int bed_id;
    private int is_occupied;
    private int treatment_region_level;

    public Bed(){}

    public int getBed_id() {
        return bed_id;
    }

    public void setBed_id(int bed_id) {
        this.bed_id = bed_id;
    }

    public int getIs_occupied() {
        return is_occupied;
    }

    public void setIs_occupied(int is_occupied) {
        this.is_occupied = is_occupied;
    }

    public int getTreatment_region_level() {
        return treatment_region_level;
    }

    public void setTreatment_region_level(int treatment_region_level) {
        this.treatment_region_level = treatment_region_level;
    }
}
