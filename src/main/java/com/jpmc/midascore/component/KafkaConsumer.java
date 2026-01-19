package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive; // Diese Klasse erstellen wir gleich
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KafkaConsumer {
    private final DatabaseConduit databaseConduit;
    private final RestTemplate restTemplate; // NEU: Werkzeug für API-Aufrufe

    // Konstruktor erweitert um RestTemplate
    public KafkaConsumer(DatabaseConduit databaseConduit, RestTemplate restTemplate) {
        this.databaseConduit = databaseConduit;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        UserRecord sender = databaseConduit.findById(transaction.getSenderId());
        UserRecord receiver = databaseConduit.findById(transaction.getRecipientId());

        System.err.println("Verarbeite Transaktion: " + transaction.getSenderId() + " -> " + transaction.getRecipientId());

        if (sender != null && receiver != null && sender.getBalance() >= transaction.getAmount()) {

            // --- NEU: API AUFRUF ---
            String url = "http://localhost:8080/incentive";
            // Wir schicken die Transaction an die API und erhalten ein Incentive-Objekt zurück
            Incentive incentiveResponse = restTemplate.postForObject(url, transaction, Incentive.class);
            float bonus = (incentiveResponse != null) ? incentiveResponse.getAmount() : 0f;
            // -----------------------

            // Kontostände anpassen
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            // WICHTIG: Empfänger kriegt Betrag PLUS Bonus
            receiver.setBalance(receiver.getBalance() + transaction.getAmount() + bonus);

            // Speichern
            databaseConduit.saveUser(sender);
            databaseConduit.saveUser(receiver);

            // TransactionRecord mit dem neuen Incentive-Feld speichern
            databaseConduit.saveTransaction(new TransactionRecord(sender, receiver, transaction.getAmount(), bonus));
            if (receiver.getName().equalsIgnoreCase("wilbur")) {
                System.err.println("!!! WILBUR AKTUELL: " + receiver.getBalance());
            }
        }
    }
}