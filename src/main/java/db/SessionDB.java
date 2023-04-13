package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Map;
import java.util.UUID;

public class SessionDB {
    private static final Map<UUID, User> session = Maps.newHashMap();

    public static UUID addSession(User user) {
        UUID uuid = UUID.randomUUID();
        session.put(uuid, user);
        return uuid;
    }

    public static boolean findSession(UUID uuid) {
        return session.get(uuid) != null;
    }
}
