package org.pispeb.treff_server.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.pispeb.treff_server.Position;
import org.pispeb.treff_server.interfaces.PollOption;

public class PollOptionSQL extends SQLObject implements PollOption {

    PollOptionSQL(int id, MysqlDataSource dataSource) {
        super(id, dataSource);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public void setPosition(Position position) {

    }
}
