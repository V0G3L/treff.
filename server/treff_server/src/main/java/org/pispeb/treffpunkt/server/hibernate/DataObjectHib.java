package org.pispeb.treffpunkt.server.hibernate;

import org.pispeb.treffpunkt.server.interfaces.DataObject;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

@MappedSuperclass
public abstract class DataObjectHib implements DataObject {

    protected static Properties config;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Override
    public ReadWriteLock getReadWriteLock() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public boolean isDeleted() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass().equals(obj.getClass())
                && this.getID() == ((DataObjectHib) obj).getID();
    }

    protected <T extends DataObjectHib> Map<Integer, T> toMap(Collection<T> coll) {
        return Collections.unmodifiableMap(coll.stream()
                .collect(Collectors.toMap(DataObjectHib::getID, Function.identity())));
    }

    public void setProperties(Properties config) {
        DataObjectHib.config = config;
    }
}
