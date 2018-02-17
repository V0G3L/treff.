package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.interfaces.Usergroup;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PollSQL extends SQLObject implements Poll {

    private static final TableName TABLE_NAME = TableName.POLLS;

    PollSQL(int id, SQLDatabase database,
               EntityManagerSQL entityManager, Properties config) {
        super(id, TABLE_NAME, database, entityManager, config);
    }

    @Override
    public String getQuestion() {
        return (String) getProperties("question").get("question");
    }

    @Override
    public void setQuestion(String question) {
        setProperties(new AssignmentList()
                .put("question", question));
    }

    @Override
    public Map<Integer, PollOption> getPollOptions() {
        try {
            return database.getQueryRunner().query(
                    "SELECT id FROM ? WHERE pollid=?;",
                    new ColumnListHandler<Integer>(),
                    TableName.POLLOPTIONS,
                    this.id)
                    .stream()
                    .collect(Collectors.toMap(Function.identity(),
                            entityManager::getPollOption));
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public PollOption addPollOption(Position position, Date timeStart, Date
            timeEnd) {
        int id;
        try {
            id = database.getQueryRunner().insert(
                    "INSERT INTO ?(latitude,longitude,timestart,timeend," +
                            "pollid) VALUES (?,?,?,?,?);",
                    new ScalarHandler<Integer>(),
                    TableName.POLLOPTIONS,
                    position.latitude,
                    position.longitude,
                    timeStart,
                    timeEnd,
                    this.id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return entityManager.getPollOption(id);
    }

    @Override
    public boolean isMultiChoice() {
        return (boolean) getProperties("multichoice").get("multichoice");
    }

    @Override
    public void setMultiChoice(boolean multiChoice) {
        setProperties(new AssignmentList()
                .put("multichoice", multiChoice));
    }

    @Override
    public Date getTimeVoteClose() {
        return (Date) getProperties("timevoteclose").get("timevoteclose");
    }

    @Override
    public void setTimeVoteClose(Date timeVoteClose) {
        setProperties(new AssignmentList()
                .put("timevoteclose", timeVoteClose));
    }

    @Override
    public Account getCreator() {
        int id = (int) getProperties("creator").get("creator");
        return entityManager.getAccount(id);
    }

    @Override
    public Event endPoll() {
        // if no polloptions were added before the poll ended,
        // just delete and return null
        if (getPollOptions().size() == 0) {
            delete();
            return null;
        }

        // lock all polloptions
        SortedSet<PollOption> pollOptions
                = new TreeSet<>(getPollOptions().values());
        pollOptions.forEach(pO -> pO.getReadWriteLock().writeLock().lock());
        try {
            // collect properties of most supported polloption TODO: tie-breaker
            PollOption bestOption = getPollOptions().values().stream()
                    .max(Comparator.naturalOrder())
                    .get();

            // create event
            int groupID = (int) getProperties("groupid").get("groupid");
            Usergroup group = entityManager.getUsergroup(groupID);
            Event event = group.createEvent(
                    getQuestion(),
                    bestOption.getPosition(),
                    bestOption.getTimeStart(),
                    bestOption.getTimeEnd(),
                    getCreator());

            // delete this poll
            delete();

            return event;
        } finally {
            pollOptions.forEach(pO
                    -> pO.getReadWriteLock().writeLock().unlock());
        }
    }

    @Override
    public int compareTo(Poll o) {
        return this.id - o.getID();
    }

    @Override
    public void delete() {
        // delete all polloptions
        SortedSet<PollOption> pollOptions
                = new TreeSet<>(getPollOptions().values());
        pollOptions.forEach(pO -> pO.getReadWriteLock().writeLock().lock());
        try {
            pollOptions.forEach(PollOption::delete);
        } finally {
            pollOptions.forEach(pO
                    -> pO.getReadWriteLock().writeLock().unlock());
        }

        try {
            database.getQueryRunner().update(
                    "DELETE FROM ? WHERE id=?;",
                    TableName.POLLS,
                    id);
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        deleted = true;
    }

}
