package com.mojang.authlib;

import java.util.StringJoiner;

public interface Environment {
    String getAuthHost();
    
    String getAccountsHost();
    
    String getSessionHost();
    
    String getName();
    
    String asString();
    
    default Environment create(final String auth, final String account, final String session, final String name) {
        return new Environment() {
            public String getAuthHost() {
                return auth;
            }
            
            public String getAccountsHost() {
                return account;
            }
            
            public String getSessionHost() {
                return session;
            }
            
            public String getName() {
                return name;
            }
            
            public String asString() {
                return new StringJoiner(", ", "", "").add((CharSequence)("authHost='" + this.getAuthHost() + "'")).add((CharSequence)("accountsHost='" + this.getAccountsHost() + "'")).add((CharSequence)("sessionHost='" + this.getSessionHost() + "'")).add((CharSequence)("name='" + this.getName() + "'")).toString();
            }
        };
    }
}
