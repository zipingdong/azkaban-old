package azkaban.handler;

import azkaban.user.Group;
import azkaban.user.Permission;
import azkaban.user.Role;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DatabaseRoleHandler implements ResultSetHandler<Map<String, Role>> {

    @Override
    public Map<String, Role> handle(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.<String, Role>emptyMap();
        }

        HashMap<String, Role> roleMap = new HashMap<>();
        do {
            final String[] permissionSplit = rs.getString("permissions").split("\\s*,\\s*");

            final Permission perm = new Permission();
            for (final String permString : permissionSplit) {
                try {
                    final Permission.Type type = Permission.Type.valueOf(permString);
                    perm.addPermission(type);
                } catch (final IllegalArgumentException e) {
                    //logger.error("Error adding type " + permString + ". Permission doesn't exist.", e);
                }
            }

            final Role role = new Role(rs.getString("name"), perm);

            roleMap.put(rs.getString("name"), role);
        }
        while (rs.next());

        return roleMap;
    }
}


