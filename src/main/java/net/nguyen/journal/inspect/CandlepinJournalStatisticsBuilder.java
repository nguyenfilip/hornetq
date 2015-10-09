package net.nguyen.journal.inspect;

import java.math.BigInteger;
import java.util.Map;

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

  
    private BigInteger eventCount = BigInteger.ZERO;
    
    public CandlepinJournalStats getJournalStatistics() {
        return new CandlepinJournalStats(eventCount);
    }

    /**
     * Adds a new Event to statistics
     * @param json Event in JSON format in java.util.Map
     */
    public void addEvent(Map json) {
        eventCount = eventCount.add(BigInteger.ONE);
        log.info(json);
    }

}
