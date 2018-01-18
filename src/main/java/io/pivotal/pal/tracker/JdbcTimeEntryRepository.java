package io.pivotal.pal.tracker;

import com.mysql.cj.api.mysqla.result.Resultset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private JdbcTemplate template;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }


    @Override
    public TimeEntry create(TimeEntry timeEntry) {


        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into time_entries (project_id, user_id, date, hours) values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setLong(4, timeEntry.getHours());

            return ps;
        }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());

        return timeEntry;


    }

    @Override
    public TimeEntry find(long id) {

        TimeEntry timeEntry = new TimeEntry();
        String sql = "Select * from time_entries where id =? " ;


        List<Map<String, Object>> maps = template.queryForList(sql, id);
        if (null!=maps && !maps.isEmpty()) {

            Map<String, Object> m =maps.get(0);

            timeEntry.setId((Long) m.get("id"));
            timeEntry.setProjectId((Long)m.get("project_id"));
            timeEntry.setUserId((Long)m.get("user_id"));
            timeEntry.setDate(((Date) m.get("date")).toLocalDate());
            timeEntry.setHours((Integer) m.get("hours"));

            return timeEntry;
        }
        else
            return null;
    }

    @Override
    public TimeEntry read(long id) {
        TimeEntry timeEntry = new TimeEntry();
        String sql = "Select * from time_entries where id =? " ;


        List<Map<String, Object>> maps = template.queryForList(sql, id);
        if (null!=maps && !maps.isEmpty()) {

            Map<String, Object> m =maps.get(0);

            timeEntry.setId((Long) m.get("id"));
            timeEntry.setProjectId((Long)m.get("project_id"));
            timeEntry.setUserId((Long)m.get("user_id"));
            timeEntry.setDate(((Date) m.get("date")).toLocalDate());
            timeEntry.setHours((Integer) m.get("hours"));

            return timeEntry;
        }
        else
            return null;
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> aList = new ArrayList<TimeEntry>();
        String sql = "Select * from time_entries" ;

        List<TimeEntry> lst = template.query(sql, new RowMapper<TimeEntry> (){
            @Override
                    public TimeEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
                TimeEntry t = new TimeEntry();
                t.setId (rs.getLong("id"));
                t.setProjectId(rs.getLong("project_id"));
                t.setUserId(rs.getLong("user_id"));
                t.setDate(rs.getDate("date").toLocalDate());
                t.setHours(rs.getInt("hours"));

               // aList.add(t);
                return t;
            }
        });

        return lst;
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {

        String sql = "Update time_entries set project_id=?," +
                "user_id =?," +
                "date=?, " +
                "hours = ?" +
                " where id =? " ;

        timeEntry.setId(id);
        template.update(sql, timeEntry.getProjectId(),
                timeEntry.getUserId(),Date.valueOf(timeEntry.getDate()), timeEntry.getHours(), id);


        return timeEntry;
    }

    @Override
    public TimeEntry delete(long id) {

        TimeEntry entry = read (id);

        String sql = "delete from time_entries where id =?" ;
        template.update(sql, entry.getId());

        return entry;
    }
}
