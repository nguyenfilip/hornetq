package net.nguyen.journal.inspect;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Statistics about Candlepin HornetQ Event journal. 
 * Should be just a POJO with ability to pretty print itself.
 * @author fnguyen
 *
 */
public class CandlepinJournalStats {
    private BigInteger numberOfEvents;

    
    
    public CandlepinJournalStats(BigInteger numberOfEvents) {
        super();
        this.numberOfEvents = numberOfEvents;
    }



    @Override
    public String toString() {
        return "CandlepinJournalStats [numberOfEvents=" + numberOfEvents + "]";
    }
    
    
    
    
}
