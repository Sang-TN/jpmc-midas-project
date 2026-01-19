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
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit; // Wichtig für die Abfrage

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");
        for (String transactionLine : transactionLines) {
            kafkaProducer.send(transactionLine);
        }

        // Wir geben dem System 10 Sekunden Zeit, alle API-Anrufe und Kafka-Events zu verarbeiten
        Thread.sleep(10000);

        logger.info("----------------------------------------------------------");
        System.err.println(">>>> START DER AUSWERTUNG <<<<");

        // Wir suchen Wilbur in der Datenbank
        boolean found = false;
        for (long i = 1; i <= 20; i++) {
            UserRecord user = databaseConduit.findById(i);
            if (user != null && user.getName().equalsIgnoreCase("wilbur")) {
                System.err.println("!!!! GEFUNDEN: " + user.getName());
                System.err.println("!!!! AKTUELLE BALANCE: " + user.getBalance());
                System.err.println("!!!! DEINE QUIZ-LÖSUNG (abgerundet): " + (int) user.getBalance());
                found = true;
                break;
            }
        }

        if (!found) {
            System.err.println("!!!! FEHLER: Wilbur wurde nicht in der DB gefunden. Prüfe UserPopulator.");
        }

        System.err.println(">>>> ENDE DER AUSWERTUNG <<<<");
        logger.info("----------------------------------------------------------");

        // Die Schleife bleibt, damit der Test nicht sofort beendet wird
        int count = 0;
        while (count < 5) {
            Thread.sleep(5000);
            count++;
        }
    }
}