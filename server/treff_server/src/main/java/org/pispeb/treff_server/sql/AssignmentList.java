package org.pispeb.treff_server.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tim
 */
class AssignmentList {

    private final Map<String, Object> map;

    AssignmentList() {
        this.map = new HashMap<>();
    }

    AssignmentList put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    Map<String, Object> getMap() {
        return map;
    }

}
