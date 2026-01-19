package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {
    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    public void populate() {
        String[] userLines = {
                "bernie, 1200.23", "grommit, 2215.37", "maria, 2774.14",
                "mario, 12.34", "waldorf, 444.55", "whosit, 888.90",
                "whatsit, 777.60", "howsit, 68.70", "wilbur, 3476.21",
                "antonio, 2121.54", "calypso, 779421.33"
        };

        for (String userLine : userLines) {
            String[] userData = userLine.split(", ");
            UserRecord user = new UserRecord(userData[0], Float.parseFloat(userData[1]));
            databaseConduit.saveUser(user);
        }
    }
}
