package colesico.framework.asyncjob;

import java.util.Date;

public class JobRecord {

    private Long id;

    /**
     * Job enqueue time
     */
    private Date createdAt;

    /**
     * Job must be processed after this time
     */
    private Date processAfter;

    /**
     * Job's business data
     */
    private String payload;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getProcessAfter() {
        return processAfter;
    }

    public void setProcessAfter(Date processAfter) {
        this.processAfter = processAfter;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "JobRecord{" +
            "id=" + id +
            ", createdAt=" + createdAt +
            ", payload='" + payload + '\'' +
            '}';
    }
}
