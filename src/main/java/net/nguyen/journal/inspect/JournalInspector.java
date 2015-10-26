package net.nguyen.journal.inspect;

import java.io.File;
import java.util.List;

import org.hornetq.core.journal.RecordInfo;
import org.hornetq.core.journal.SequentialFileFactory;
import org.hornetq.core.journal.impl.JournalFile;
import org.hornetq.core.journal.impl.JournalImpl;
import org.hornetq.core.journal.impl.JournalReaderCallback;
import org.hornetq.core.journal.impl.NIOSequentialFileFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Entry point for inspecting journals. Offers methods to read various journal
 * types. Notifies record readers (e.g. AddRecordReader).
 * 
 * This class also knows that there is some statistics service and will use that
 * to get statistics about the journal after all journals has been processed.
 * 
 * @author fnguyen
 *
 */
@Component
public class JournalInspector {

    @Autowired
    private AddRecordReader addRecordHandler;
    
    @Autowired
    private JournalInspectorConfiguration config;
    

    public void inspect() throws Exception {
        inspectJournal(config.getJournalsPath().toString(), "hornetq-data", "hq",
                2, 10485760);
    }

    private void inspectJournal(final String directory,
            final String journalPrefix, final String journalSuffix,
            final int minFiles, final int fileSize) throws Exception {
        NIOSequentialFileFactory nio = new NIOSequentialFileFactory(directory,
                null);

        JournalImpl journal = new JournalImpl(fileSize, minFiles, 0, 0, nio,
                journalPrefix, journalSuffix, 1);

        List<JournalFile> files = journal.orderFiles();

        for (JournalFile file : files) {
            exportJournalFile(nio, file);
        }
    }

    public void exportJournalFile(final SequentialFileFactory fileFactory,
            final JournalFile file) throws Exception {
        JournalImpl.readJournalFile(fileFactory, file,
                new JournalReaderCallback() {

                    public void onReadUpdateRecordTX(final long transactionID,
                            final RecordInfo recordInfo) throws Exception {

                    }

                    public void onReadUpdateRecord(final RecordInfo recordInfo)
                            throws Exception {
                    }

                    public void onReadRollbackRecord(final long transactionID)
                            throws Exception {
                    }

                    public void onReadPrepareRecord(final long transactionID,
                            final byte[] extraData, final int numberOfRecords)
                                    throws Exception {
                    }

                    public void onReadDeleteRecordTX(final long transactionID,
                            final RecordInfo recordInfo) throws Exception {

                    }

                    public void onReadDeleteRecord(final long recordID)
                            throws Exception {
                    }

                    public void onReadCommitRecord(final long transactionID,
                            final int numberOfRecords) throws Exception {
                    }

                    public void onReadAddRecordTX(final long transactionID,
                            final RecordInfo recordInfo) throws Exception {
                        addRecordHandler.handle(recordInfo);
                    }

                    public void onReadAddRecord(final RecordInfo recordInfo)
                            throws Exception {
                        addRecordHandler.handle(recordInfo);
                    }

                    public void markAsDataFile(final JournalFile file) {
                    }
                });
    }

}
