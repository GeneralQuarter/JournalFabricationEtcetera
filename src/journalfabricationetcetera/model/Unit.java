package journalfabricationetcetera.model;

import java.text.DecimalFormat;

/**
 * Created by Quentin Gangler on 21/10/2016.
 *
 */
public class Unit {
    private int id;
    private String name;
    private String abr1000;
    private String abr1;

    public Unit(int id, String name, String abr1000, String abr1) {
        this.id = id;
        this.name = name;
        this.abr1000 = abr1000;
        this.abr1 = abr1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbr1000() {
        return abr1000;
    }

    public void setAbr1000(String abr1000) {
        this.abr1000 = abr1000;
    }

    public String getAbr1() {
        return abr1;
    }

    public void setAbr1(String abr1) {
        this.abr1 = abr1;
    }

    @Override
    public String toString() {
        return name;
    }
}
