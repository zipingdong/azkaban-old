package azkaban.handler;

import azkaban.user.Group;
import azkaban.user.User;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseGroupHandler implements ResultSetHandler<Map<String, Group>> {

    @Override
    public Map<String, Group> handle(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.<String, Group>emptyMap();
        }

        Map<String, Group> groupMap = new HashMap<String, Group>();
        do {
            Group group = new Group(rs.getString("name"));

            final String[] roleSplit = rs.getString("roles").split("\\s*,\\s*");
            for (final String role : roleSplit) {
                group.addRole(role);
            }

            groupMap.put(rs.getString("name"), group);
        }
        while (rs.next());

        return groupMap;
    }
}


