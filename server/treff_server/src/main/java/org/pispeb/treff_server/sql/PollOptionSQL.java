package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;
import org.pispeb.treff_server.sql.resultsethandler.DataObjectMapHandler;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PollOptionSQL extends SQLObject implements PollOption {

    private static final TableName TABLE_NAME = TableName.POLLOPTIONS;

    PollOptionSQL(int id, SQLDatabase database,
               EntityManagerSQL entityManager, Properties config) {
        super(id, TABLE_NAME, database, entityManager, config);
    }

    @Override
    public Position getPosition() {
        Map<String, Object> properties
                = getProperties("latitude", "longitude");
        return new Position(
                (Double) properties.get("latitude"),
                (Double) properties.get("longitude"));
    }

    @Override
    public void setPosition(Position position) {
        setProperties(new AssignmentList()
                .put("latitude", position.latitude)
                .put("longitude", position.longitude));
    }

    @Override
    public void setTimeStart(Date timeStart) {
        setProperty("timestart", timeStart);
    }

    @Override
    public Date getTimeStart() {
        return (Date) getProperty("timestart");
    }

    @Override
    public void setTimeEnd(Date timeEnd) {
        setProperty("timeend", timeEnd);
    }

    @Override
    public Date getTimeEnd() {
        return (Date) getProperty("timeend");
    }

    @Override
    public void addVoter(Account voter) {
        database.insert(
                "INSERT INTO %s(accountid,polloptionid) VALUES (?,?);",
                TableName.POLLVOTES,
                (rs -> null),
                voter.getID(),
                this.id);
    }

    @Override
    public void removeVoter(Account voter) {
        database.update(
                "DELETE FROM %s WHERE accountid=? AND polloptionid=?;",
                TableName.POLLVOTES,
                voter.getID(),
                this.id);
    }

    @Override
    public Map<Integer, AccountSQL> getVoters() {
        return database.query(
                "SELECT accountid FROM %s WHERE polloptionid=?;",
                TableName.POLLVOTES,
                new DataObjectMapHandler<>(AccountSQL.class, entityManager),
                this.id);
    }

    @Override
    public void delete() {
        // remove all votes
        getVoters().values().forEach(this::removeVoter);

        database.update(
                "DELETE FROM %s WHERE id=?;",
                TableName.POLLS,
                this.id);

        deleted = true;
    }

    @Override
    public int compareTo(PollOption o) {
        return this.id - o.getID();
    }
}
