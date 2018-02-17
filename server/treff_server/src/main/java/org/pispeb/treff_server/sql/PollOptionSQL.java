package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

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
    public String getTitle() {
        return (String) getProperty("title");
    }

    @Override
    public void setTitle(String title) {
        setProperty("title", title);
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
        try {
            database.getQueryRunner().insert(
                    "INSERT INTO ?(accountid,polloptionid) VALUES (?,?);",
                    (rs -> null),
                    TableName.POLLVOTES.toString(),
                    voter.getID(),
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void removeVoter(Account voter) {
        try {
            database.getQueryRunner().update(
                    "DELETE FROM ? WHERE accountid=? AND polloptionid=?;",
                    TableName.POLLVOTES.toString(),
                    voter.getID(),
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Map<Integer, Account> getVoters() {
        try {
            return database.getQueryRunner().query(
                    "SELECT FROM ? WHERE polloptionid=?;",
                    new ColumnListHandler<Integer>(),
                    TableName.POLLVOTES.toString(),
                    this.id)
                    .stream()
                    .collect(Collectors.toMap(Function.identity(),
                            entityManager::getAccount));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete() {
        // remove all votes
        getVoters().values().forEach(this::removeVoter);

        try {
            database.getQueryRunner().update(
                    "DELETE FROM ? WHERE id=?;",
                    TableName.POLLS.toString(),
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        deleted = true;
    }

    @Override
    public int compareTo(PollOption o) {
        return this.id - o.getID();
    }
}
