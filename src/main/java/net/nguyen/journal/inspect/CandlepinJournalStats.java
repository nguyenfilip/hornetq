package net.nguyen.journal.inspect;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Statistics about Candlepin HornetQ Event journal.
 * Should be just a POJO with ability to pretty print itself.
 * 
 * @author fnguyen
 */
public class CandlepinJournalStats {

    private BigInteger numberOfEvents;
    private Map<EventType, BigInteger> counts = new HashMap<EventType, BigInteger>();
    private Map<EventType, BigInteger> sizeByEventType = new HashMap<EventType, BigInteger>();
    private BigInteger eventCount = BigInteger.ZERO;
    private BigInteger errored = BigInteger.ZERO;

    public CandlepinJournalStats() {
        super();
    }

    public void incEventCount() {
        eventCount=eventCount.add(BigInteger.ONE);        
    }
    public void incErroredMessages() {
        errored=errored.add(BigInteger.ONE);
    }
    
    
    
    @Override
    public String toString() {
        return "CandlepinJournalStats [numberOfEvents=" + numberOfEvents +
                ",\n counts=" + prettyPrintMap(counts) + ", sizeByEventType=" + prettyPrintMap(convertToPercentMap(sizeByEventType)) +
                ", eventCount=" + eventCount + ", errored=" + errored + "]";
    }

    private <T> Map<T, BigDecimal> convertToPercentMap(
            Map<T, BigInteger> mapOfNumbers) {
        BigDecimal total = BigDecimal.ZERO;
        for (Entry<?, BigInteger> entry: mapOfNumbers.entrySet()){
            total = total.add(new BigDecimal(entry.getValue()));
        }
        
        Map<T,BigDecimal> result = new HashMap<T,BigDecimal>();
        for (Entry<?, BigInteger> entry: mapOfNumbers.entrySet()){
            BigDecimal entryValue = new BigDecimal(entry.getValue());
            result.put((T)entry.getKey(), entryValue.divide(total, 5,BigDecimal.ROUND_HALF_DOWN));
        }
        return result;
        
    }

    private String prettyPrintMap(Map<?, ?> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Entry<?,?> entry : map.entrySet()){
            sb.append(entry.getKey()+" -> "+entry.getValue()+"\n");
        }
        return sb.toString();
    }

    /**
     * Counts number of occurences of keys
     * 
     * @param key
     */
    public void incEventType(EventType eventType) {
        if (!counts.containsKey(eventType))
            counts.put(eventType, BigInteger.ZERO);
        
        BigInteger newValue = counts.get(eventType).add(BigInteger.ONE);
        counts.replace(eventType, newValue);
    }
    
    public void incEventTypeSize(EventType eventType, long size) {
        if (!sizeByEventType.containsKey(eventType))
            sizeByEventType.put(eventType, BigInteger.ZERO);
        
        BigInteger newValue = sizeByEventType.get(eventType).add(BigInteger.valueOf(size));
        sizeByEventType.replace(eventType, newValue);
    }
}
