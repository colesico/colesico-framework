/*
 * Copyright 20014-2019 Vladlen V. Larionov and others as noted.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
