package net.nguyen.journal.inspect;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LargeMessageReader {
    @Autowired
    private JournalInspectorConfiguration config;
    
    public String readJsonInsideFile(long id) {
        Path path = config.getLargeMessagesPath();
        
        File largeMsgFile = path.resolve(id+".msg").toFile();
        
        if (!largeMsgFile.exists()) {
            throw new IllegalArgumentException("File for message "+id+"doesn't exist");
        }
        
        try {
            byte[] bytes = FileUtils.readFileToByteArray(largeMsgFile);
            bytes = Arrays.copyOfRange(bytes,8,bytes.length);
            String decoded = new String(bytes, "UTF-16LE");
            return decoded;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
