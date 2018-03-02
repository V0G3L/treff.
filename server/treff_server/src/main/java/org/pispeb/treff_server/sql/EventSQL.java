package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;
import org.pispeb.treff_server.sql.resultsethandler.DataObjectMapHandler;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventSQL extends SQLObject implements Event {

    private static final TableName TABLE_NAME = TableName.EVENTS;

    EventSQL(int id, SQLDatabase database,
               EntityManagerSQL entityManager, Properties config) {
        super(id, TABLE_NAME, database, entityManager, config);
    }

    @Override
    public void setTitle(String title) {
        setProperties(new AssignmentList()
                .put("title", title));
    }

    @Override
    public String getTitle() {
        return (String) getProperties("title").get("title");
    }

    @Override
    public void setPosition(Position position) {
        setProperties(new AssignmentList()
                .put("latitude", position.latitude)
                .put("longitude", position.longitude));
    }

    @Override
    public Position getPosition() {
        Map<String, Object> properties
                = getProperties("latitude", "longitude");
        return new Position((Double) properties.get("latitude"),
                (Double) properties.get("longitude"));
    }

    @Override
    public void setTimeStart(Date timeStart) {
        setProperties(new AssignmentList()
                .put("timestart", timeStart));
    }

    @Override
    public Date getTimeStart() {
        return (Date) getProperties("timestart").get("timestart");
    }

    @Override
    public void setTimeEnd(Date timeEnd) {
        setProperties(new AssignmentList()
                .put("timeend", timeEnd));
    }

    @Override
    public Date getTimeEnd() {
        return (Date) getProperties("timestart").get("timestart");
    }

    @Override
    public Account getCreator() {
        int id = (int) getProperties("creator").get("creator");
        return entityManager.getAccount(id);
    }

    @Override
    public void addParticipant(Account participant) {
        database.insert(
                "INSERT INTO %s(accountid,eventid) VALUES (?,?);",
                TableName.EVENTPARTICIPATIONS,
                (rs -> null),
                participant.getID(),
                this.id);
    }

    @Override
    public void removeParticipant(Account participant) {
        database.update(
                "DELETE FROM %s WHERE accountid=? AND eventid=?;",
                TableName.EVENTPARTICIPATIONS,
                participant.getID(),
                this.id);
    }

    @Override
    public Map<Integer, AccountSQL> getAllParticipants() {
        return database.query(
                "SELECT accountid FROM %s WHERE eventid=?;",
                TableName.EVENTPARTICIPATIONS,
                new DataObjectMapHandler<>(AccountSQL.class, entityManager),
                this.id);
    }

    @Override
    public void delete() {
        // remove all participants
        getAllParticipants().values().forEach(this::removeParticipant);
        database.update(
                "DELETE FROM %s WHERE id=?;",
                TableName.EVENTS,
                id);
        deleted = true;
    }

    @Override
    public int compareTo(Event o) {
        return this.id - o.getID();
    }
}
