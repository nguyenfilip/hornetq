package net.nguyen.journal.inspect;

import java.util.Map;

import org.apache.log4j.Logger;
import org.hornetq.api.core.HornetQBuffers;
import org.hornetq.core.journal.RecordInfo;
import org.hornetq.core.server.impl.ServerMessageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Gets triggered for every add record from the journal
 * 
 * @author fnguyen
 *
 */
@Component
public class AddRecordReader {
    private Logger log = org.apache.log4j.LogManager
            .getLogger(AddRecordReader.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CandlepinJournalStatisticsBuilder statsBuilder;
    
    public void handle(RecordInfo recordInfo) {
        ServerMessageImpl serverMessage = new ServerMessageImpl(1l, 50);
        serverMessage.decode(HornetQBuffers.wrappedBuffer(recordInfo.data));
        String json = serverMessage.getBodyBuffer().readString();
//        log.info(json);
        Map map;
        try {
            map = objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        statsBuilder.addEvent(map);

    }

}
