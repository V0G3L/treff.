package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EventSQL extends SQLObject implements Event {

    private static final TableName TABLE_NAME = TableName.EVENTS;

    EventSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
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
        return EntityManagerSQL.getInstance().getAccount(id);
    }

    @Override
    public void addParticipant(Account participant) {
        try {
            database.getQueryRunner().insert(
                    "INSERT INTO ?(accountid,eventid) VALUES (?,?);",
                    (rs -> null),
                    TableName.EVENTPARTICIPATIONS.toString(),
                    participant.getID(),
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeParticipant(Account participant) {
        try {
            database.getQueryRunner().update(
                    "DELETE FROM ? WHERE accountid=? AND eventid=?;",
                    TableName.EVENTPARTICIPATIONS.toString(),
                    participant.getID(),
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Map<Integer, Account> getAllParticipants() {
        try {
            return database.getQueryRunner().query(
                    "SELECT accountid FROM ? WHERE eventid=?;",
                    new ColumnListHandler<Integer>(),
                    TableName.EVENTPARTICIPATIONS.toString(),
                    this.id)
                    .stream()
                    .collect(Collectors.toMap(Function.identity(),
                            EntityManagerSQL.getInstance()::getAccount));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete() {
        // remove all participants
        getAllParticipants().values().forEach(this::removeParticipant);
        try {
            database.getQueryRunner().update(
                    "DELETE FROM ? WHERE id=?;",
                    TableName.EVENTS,
                    id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        deleted = true;
    }

    @Override
    public int compareTo(Event o) {
        return this.id - o.getID();
    }
}
