package azkaban.handler;

import azkaban.database.Job;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseJobHandler implements ResultSetHandler<List<Job>> {

    @Override
    public List<Job> handle(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.<Job>emptyList();
        }

        List<Job> jobs = new ArrayList<Job>();
        do {
            Job job = new Job(rs.getString("project_name"), rs.getString("job_name"),
                    rs.getLong("create_time"), rs.getLong("update_time"),
                    rs.getString("create_user"), rs.getString("update_user"),
                    rs.getString("type"), rs.getString("command"), rs.getString("dependencies"),
                    rs.getByte("warn_level"), rs.getShort("max_elapse"),
                    rs.getByte("retries"), rs.getInt("retries_interval"),
                    rs.getString("notify_user"), rs.getString("proxy_user"));
            jobs.add(job);
        }
        while (rs.next());

        return jobs;
    }
}

