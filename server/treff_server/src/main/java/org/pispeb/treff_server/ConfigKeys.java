package org.pispeb.treff_server;

/**
 * @author tim
 */
public enum ConfigKeys {
    DB_ADDRESS("db_address"),
    DB_PORT("db_port"),
    DB_USER("db_user"),
    DB_PASS("db_pass"),
    DB_DBNAME("db_dbname"),

    USER_NAME_LENGTH_MAX("user_name_length_max"),
    EMAIL_LENGTH_MAX("email_length_max"),
    PASSWORD_SALT_BYTES("password_salt_bytes"),
    PASSWORD_HASH_ALG("password_hash_alg"),
    LOGIN_TOKEN_BYTES("login_token_bytes"),

    USERGROUP_NAME_LENGTH_MAX("usergroup_name_length_max"),
    POLL_QUESTION_LENGTH_MAX("poll_question_length_max"),
    EVENT_TITLE_LENGTH_MAX("event_title_length_max");

    private final String key;

    ConfigKeys(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}
