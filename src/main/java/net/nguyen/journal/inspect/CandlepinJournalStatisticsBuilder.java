package net.nguyen.journal.inspect;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Builds statistics from incoming json events 
 * @author fnguyen
 *
 */
@Component
public class CandlepinJournalStatisticsBuilder {
    private Logger log = LogManager
            .getLogger(CandlepinJournalStatisticsBuilder.class);

  
    private CandlepinJournalStats stats = new CandlepinJournalStats();
    
    
    public CandlepinJournalStats getJournalStatistics() {
        return stats;
    }

    /**
     * Adds a new Event to statistics
     * @param json Event in JSON format in java.util.Map
     */
    public void addEvent(Map json) {
        stats.incEventCount();
        EventType eventType= new EventType((String)json.get("type"),(String)json.get("target"));
        stats.incEventType(eventType);
        stats.incEventTypeSize(eventType, json.toString().length());
    }

    public void reportMessageError(long id) {
        stats.incErroredMessages();
    }
    

}
