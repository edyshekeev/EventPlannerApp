package com.example.eventplannerapp.security;

import com.example.eventplannerapp.SpringAppContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; // 10 DAYS
    public static final String TOKEN_PREFIX = "Ghost ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
    public static final String TOKEN_SECRET = "VODM9W5MEYiqexk4hmDe1DExsFYcX5ZmQZvZtTz43mSXKf0O1617BrwxwoOL7gXK";

    public static String getTokenSecret() {
        Environment environment = (Environment) SpringAppContext.getBean("environment");
        return environment.getProperty("secretToken");
    }
}
