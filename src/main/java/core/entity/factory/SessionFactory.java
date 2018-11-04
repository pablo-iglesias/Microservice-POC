package core.entity.factory;

import java.util.Random;

import core.Server;
import core.entity.Session;

public class SessionFactory {

    /**
     * Create a new session with respective token, ensure that token is not repeated
     * 
     * @param uid
     * @return
     */
    public static Session create(int uid) {

        String sessionToken;

        do {
            Random rand = new Random(System.currentTimeMillis());
            sessionToken = Long.toHexString(rand.nextLong());
        } while (Server.getSession(sessionToken) != null);

        return new Session(uid, sessionToken);
    }
}
