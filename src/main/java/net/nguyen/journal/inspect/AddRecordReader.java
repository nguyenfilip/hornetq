package net.nguyen.journal.inspect;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hornetq.api.core.HornetQBuffers;
import org.hornetq.core.journal.RecordInfo;
import org.hornetq.core.persistence.impl.journal.JournalRecordIds;
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

    @Autowired
    private LargeMessageReader largeMessageReader;

    public void handle(RecordInfo recordInfo) {
        String json = null;
        switch (recordInfo.getUserRecordType()) {
        case JournalRecordIds.ADD_MESSAGE:
            json = parseBytes(recordInfo.id, recordInfo.data);
            break;
        case JournalRecordIds.ADD_LARGE_MESSAGE:
            try {
                json = largeMessageReader.readJsonInsideFile(recordInfo.id);
            }
            catch (IllegalArgumentException ex){
                statsBuilder.reportMessageError(recordInfo.id);
                return;
            }
            break;
        default:
            statsBuilder.reportMessageError(recordInfo.id);
            return;

        }

        Map map;
        try {
            map = objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        statsBuilder.addEvent(map);

    }

    public String parseBytes(long id, byte[] data) {
        ServerMessageImpl serverMessage = new ServerMessageImpl(id, 50);
        serverMessage.decode(HornetQBuffers.wrappedBuffer(data));
        return serverMessage.getBodyBuffer().readString();
    }

}
