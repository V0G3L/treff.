package org.pispeb.treff_server.sql;

import org.pispeb.treff_server.exceptions.DatabaseException;
import org.pispeb.treff_server.interfaces.Account;
import org.pispeb.treff_server.interfaces.Update;
import org.pispeb.treff_server.sql.SQLDatabase.TableName;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class UpdateSQL extends SQLObject implements Update {

    private static final TableName TABLE_NAME = TableName.UPDATES;

    UpdateSQL(int id, SQLDatabase database,
               EntityManagerSQL entityManager, Properties config) {
        super(id, TABLE_NAME, database, entityManager, config);
    }

    @Override
    public Date getTime()  {
        return (Date) getProperty("time");
    }

    @Override
    public UpdateType getType()  {
        return UpdateType.valueOf((String) getProperty("type"));
    }

    @Override
    public JsonObject getUpdate()  {
        String updatestring = (String) getProperty("updatestring");
       try (JsonReader jsonReader
                = Json.createReader(new StringReader(updatestring))) {
           return jsonReader.readObject();
       }
    }

    @Override
    public boolean removeAffectedAccount(Account account) {
        account.markUpdateAsDelivered(this);
        if (this.getAffectedAccounts().size() == 0) {
            delete();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<Integer, Account> getAffectedAccounts()  {
        return null;
    }

    @Override
    public int compareTo(Update o) {
        return this.getTime().compareTo(o.getTime());
    }

    /**
     * Deletes this {@link Update}.
     * Will automatically be called upon removal of the last affected Account.
     * Should not be called manually!
     */
    @Override
    public void delete() {
        assert getAffectedAccounts().size() == 0;

        database.update(
                "DELETE FROM %s WHERE id=?;",
                TableName.UPDATES,
                this.id);

        deleted = true;
    }

}
