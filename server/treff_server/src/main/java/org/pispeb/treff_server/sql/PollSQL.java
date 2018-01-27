package org.pispeb.treff_server.sql;

import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Event;
import org.pispeb.treff_server.interfaces.Poll;
import org.pispeb.treff_server.interfaces.PollOption;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PollSQL extends SQLObject implements Poll {

    private static final TableName TABLE_NAME = TableName.POLLS;

    PollSQL(int id, SQLDatabase database, Properties config) {
        super(id, database, config, TABLE_NAME);
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
                            EntityManagerSQL.getInstance()::getPollOption));
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
        return EntityManagerSQL.getInstance().getPollOption(id);
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
    public Event endPoll() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

    @Override
    public int compareTo(Poll o) {
        return this.id - o.getID();
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException(); // TODO: implement
    }

}
