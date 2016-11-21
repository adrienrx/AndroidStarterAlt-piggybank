package net.sparkeek.piggybank.persistence.entities;

import android.os.Parcelable;

import java.io.Serializable;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public interface Cash extends Parcelable, Persistable, Serializable {
    @Key
    @Generated
    Integer getId();

    Float getCash();

    String getUser();
}
