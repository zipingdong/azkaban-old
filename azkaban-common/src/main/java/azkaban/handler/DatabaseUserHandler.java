package azkaban.handler;

import azkaban.user.User;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DatabaseUserHandler implements ResultSetHandler<List<User>> {

    @Override
    public List<User> handle(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.<User>emptyList();
        }

        List<User> users = new ArrayList<User>();
        do {
            User user = new User(rs.getString("user_id"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setPhone(rs.getString("phone"));

            if (StringUtils.isNotBlank(rs.getString("groups")))
                user.setGroups(Arrays.asList(rs.getString("groups").split(",")));

            user.setRoles(Arrays.asList(rs.getString("roles")));
            users.add(user);
        }
        while (rs.next());

        return users;
    }
}


