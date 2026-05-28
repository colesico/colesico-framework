package colesico.framework.security.assist.authentication.simple;

import colesico.framework.config.Config;
import colesico.framework.config.FromSource;
import colesico.framework.config.UseFileSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Config
@UseFileSource(bindAll = false, file = "accounts.properties")
public class DefaultSimpleAccountStorage implements SimpleAccountStorage {

    protected static final String PASSWORD_SUFFIX = ".password";
    protected static final String ROLES_SUFFIX = ".roles";

    @FromSource
    private Properties accounts;
    private final Map<String, Entry> entries = new ConcurrentHashMap<>();

    @Override
    public SimpleAccountStorage.Account findAccount(String login, String passwordHashHex) {
        var entry = getEntry(login);
        if (entry == null) {
            return null;
        }
        if (!passwordHashHex.equals(entry.password)) {
            return null;
        }
        return new Account(login, entry.roles);
    }

    private Entry getEntry(String login) {
        var entry = entries.get(login);
        if (entry != null) {
            return entry;
        }
        String password = accounts.getProperty(login + PASSWORD_SUFFIX);
        if (password == null) {
            return null;
        }
        Set<String> roles;
        String rolesStr = accounts.getProperty(login + ROLES_SUFFIX);
        if (rolesStr != null) {
            roles = Set.of(rolesStr.split(","));
        } else {
            roles = Set.of();
        }
        entry = new Entry(login, password, roles);
        entries.put(login, entry);
        return entry;
    }

    public void setAccounts(Properties accounts) {
        this.accounts = accounts;
    }

    public record Entry(String login, String password, Set<String> roles) {
    }
}
