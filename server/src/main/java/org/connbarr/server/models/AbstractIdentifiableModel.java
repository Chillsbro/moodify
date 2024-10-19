package org.connbarr.server.models;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

//Whenever super is called, created this property on that class
//Any class extended from this will have an ID created for it.
@MappedSuperclass
public class AbstractIdentifiableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //Allows us to create any Id we want, program will know if they're different classes.
    @Override
    public boolean equals(Object o) { //if this is equal to the object being passed in, return 'true'.  If id class is attached to doesn't match, return false.
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractIdentifiableModel that = (AbstractIdentifiableModel) o;
        return id == that.id;
    }
}
