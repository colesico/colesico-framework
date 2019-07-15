package colesico.framework.asyncjob.dao;

import colesico.framework.asyncjob.JobServiceConfig;
import colesico.framework.asyncjob.JobQueueConfig;
import colesico.framework.asyncjob.JobDao;
import colesico.framework.asyncjob.JobRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.*;
import java.time.Duration;

/**
 * Default dao impl
 */
@Singleton
public class PostgreJobDao implements JobDao {
    public static final String CREATE_TABLE_SQL = "CREATE TABLE %s (\n" +
        "   id bigserial NOT NULL,\n" +
        "   created_at timestamp(3) with time zone NOT NULL,\n" +
        "   process_after timestamp(3) with time zone NOT NULL,\n" +
        "   payload varchar(512) NOT NULL,\n" +
        "   CONSTRAINT %s_pkey PRIMARY KEY (id)\n" +
        ")";

    public static final String PICK_SQL = "DELETE FROM %s WHERE id = (" +
        "   SELECT t.id FROM %s t" +
        "   WHERE t.process_after <= now()" +
        "   ORDER BY t.id FOR UPDATE SKIP LOCKED LIMIT 1" +
        " ) RETURNING id, created_at, process_after, payload";

    public static final String ENQUEUE_SQL = "INSERT INTO %s (created_at, process_after, payload)" +
        " VALUES(now(), now() + CAST(? AS INTERVAL), ?)" +
        " RETURNING id";

    private final JobServiceConfig srvConfig;

    @Inject
    public PostgreJobDao(JobServiceConfig srvConfig) {
        this.srvConfig = srvConfig;
    }

    @Override
    public Long enqueue(JobQueueConfig queueConfig, String payload, Duration delay) {
        String queryText = String.format(ENQUEUE_SQL, queueConfig.getTableName());
        try (PreparedStatement ps = srvConfig.getConnection().prepareStatement(queryText, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, (delay == null ? "0" : delay.toMillis()) + " milliseconds");
            ps.setString(2, payload);
            int ar = ps.executeUpdate();
            if (ar == 0) {
                throw new RuntimeException("Persisting job failed, no rows affected: " + queueConfig);
            }
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Enqueue job error: " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }

    @Override
    public JobRecord pick(JobQueueConfig queueConfig) {
        String queryText = String.format(PICK_SQL, queueConfig.getTableName(), queueConfig.getTableName());

        try (PreparedStatement ps = srvConfig.getConnection().prepareStatement(queryText)) {
            ps.execute();
            ResultSet rs = ps.getResultSet();
            if (rs.next() == false) {
                return null;
            } else {
                JobRecord job = new JobRecord();
                job.setId(rs.getLong("id"));
                job.setCreatedAt(rs.getDate("created_at"));
                job.setProcessAfter(rs.getDate("process_after"));
                job.setPayload(rs.getString("payload"));
                return job;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Pick job error: " + ExceptionUtils.getRootCauseMessage(e), e);
        }
    }
}