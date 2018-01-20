package org.pispeb.treff_server.interfaces;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * @author tim
 */
public interface DataObject {

    ReadWriteLock getReadWriteLock();

}
