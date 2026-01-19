package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }


        Thread.sleep(5000);

        logger.info("----------------------------------------------------------");
        logger.info("ERGEBNIS-CHECK:");

        for (long i = 1; i <= 15; i++) {
            UserRecord user = databaseConduit.findById(i);
            if (user != null && user.getName().equalsIgnoreCase("waldorf")) {
                logger.info("GEFUNDEN! Name: " + user.getName() + " | Balance: " + user.getBalance());
                logger.info("DEINE LÖSUNG FÜR DAS QUIZ: " + (int) user.getBalance());
            }
        }

        logger.info("----------------------------------------------------------");

        // Der Test bleibt kurz offen, damit du die Zahl lesen kannst
        Thread.sleep(10000);
    }
}