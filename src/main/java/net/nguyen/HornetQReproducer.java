package net.nguyen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.api.core.client.ClientSessionFactory;
import org.hornetq.api.core.client.HornetQClient;
import org.hornetq.api.core.client.ServerLocator;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.JournalType;
import org.hornetq.core.server.embedded.EmbeddedHornetQ;
import org.hornetq.core.settings.impl.AddressFullMessagePolicy;
import org.hornetq.core.settings.impl.AddressSettings;

/**
 * Hello world!
 *
 */
public class HornetQReproducer {
    private static EmbeddedHornetQ hornetqServer;
    private static Logger log = LogManager.getLogger(HornetQReproducer.class);
    private static String PAGING_QUEUE_NAME = "jms.queue.pagingQueue";
    
    private static ClientSessionFactory sessionFactory;
    
    public static void main(String[] args) throws Exception {
        boolean enablePaging = args.length > 0 && args[0].equals("--paging");
        
        if (!enablePaging)
            log.info("Paging off. You can use --paging switch to enable paging");
        else 
            log.info("Paging enabled.");
        
        
        Configuration config = createConfigurationAndCleanJournals(enablePaging);
         
        hornetqServer = new EmbeddedHornetQ();
        hornetqServer.setConfiguration(config);
        hornetqServer.start();
        sessionFactory = createClientSessionFactory();
        
        //The problem is the same with transacted session
        //ClientSession session = sessionFactory.createTransactedSession();
        ClientSession session = sessionFactory.createSession();
        
        session.createQueue(PAGING_QUEUE_NAME, PAGING_QUEUE_NAME, true);
        session.createQueue("hornetq.notifications", "hornetq.notifications", true);
        ClientProducer producer = session.createProducer(PAGING_QUEUE_NAME);
        
        session.start();
        
        byte[] bytes = new byte[50 * 1024];
        
        for (long i = 0; i < 5500; i++) {
            ClientMessage message = session.createMessage(true);
            message.getBodyBuffer().writeBytes(bytes);
            
            producer.send(message);   
        }
        
        log.info("Producer sends done.");
        
        session.close();
    }

    private static Configuration createConfigurationAndCleanJournals(boolean enablePaging) throws IOException {
        Configuration config = new ConfigurationImpl();

        HashSet<TransportConfiguration> transports = new HashSet<TransportConfiguration>();
        transports.add(new TransportConfiguration(
                InVMAcceptorFactory.class.getName()));
        config.setAcceptorConfigurations(transports);

        // alter the default pass to silence log output
        config.setClusterUser(null);
        config.setClusterPassword(null);

        // in vm, who needs security?
        config.setSecurityEnabled(false);

        config.setJournalType(JournalType.NIO);

        config.setCreateBindingsDir(true);
        config.setCreateJournalDir(true);

        File bindingsFile = new File("bindings");
        File journalFile = new File("journal");
        File largeMsgsFile = new File("largemsgs");
        File pagingFile = new File("paging");
        List<File> directories = new ArrayList<File>();
        directories.add(bindingsFile);
        directories.add(journalFile);
        directories.add(largeMsgsFile);
        directories.add(pagingFile);

        for (File f : directories)
            FileUtils.deleteDirectory(f);

        config.setBindingsDirectory(bindingsFile.toString());
        config.setJournalDirectory(journalFile.toString());
        config.setLargeMessagesDirectory(largeMsgsFile.toString());
        config.setPagingDirectory(pagingFile.toString());

        if (enablePaging){
            Map<String, AddressSettings> settings = new HashMap<String, AddressSettings>();
            AddressSettings pagingConfig = new AddressSettings();
            pagingConfig.setMaxSizeBytes(1024*1024*6);
            pagingConfig.setPageSizeBytes(1024*1024*2);
            pagingConfig
                    .setAddressFullMessagePolicy(AddressFullMessagePolicy.PAGE);
            settings.put("#", pagingConfig);
            config.setAddressesSettings(settings);
        }
        return config;
    }

    protected static ClientSessionFactory createClientSessionFactory()
            throws Exception {
        ServerLocator locator = HornetQClient
                .createServerLocatorWithoutHA(new TransportConfiguration(
                        InVMConnectorFactory.class.getName()));
        // locator.setMinLargeMessageSize(largeMsgSize);
        return locator.createSessionFactory();
    }
}
