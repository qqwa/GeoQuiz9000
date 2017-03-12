package no.ntnu.tdt4240.geoquiz9000.models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Id;

/**
 * Template for creating a model class. (Generated annotations are generated automatically)
 * TODO delete
 */
@Entity
public class Test extends AbstractModel {

    @Id
    private long id;

    private String text;

    @Generated(hash = 1880097528)
    public Test(long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Generated(hash = 372557997)
    public Test() {
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(long id) {
        this.id = id;
    }
}
