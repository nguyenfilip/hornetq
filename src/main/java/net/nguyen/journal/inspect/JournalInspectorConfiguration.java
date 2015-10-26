package net.nguyen.journal.inspect;

import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

@Component
public class JournalInspectorConfiguration {

    private static Path path = null;

    public JournalInspectorConfiguration() {
        path = FileSystems.getDefault()
                .getPath("/home/fnguyen/candlepin/hornet/lutz");
    }

    public Path getLargeMessagesPath() {
        return path.resolve("largemsgs");
    }

    public Path getJournalsPath() {
        return path.resolve("journal");
    }

}
