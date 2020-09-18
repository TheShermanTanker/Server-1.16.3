package com.mojang.authlib.minecraft;

import java.util.UUID;
import com.mojang.authlib.GameProfile;
import java.util.Calendar;
import java.util.Date;

public class InsecureTextureException extends RuntimeException {
    public InsecureTextureException(final String message) {
        super(message);
    }
    
    public static class OutdatedTextureException extends InsecureTextureException {
        private final Date validFrom;
        private final Calendar limit;
        
        public OutdatedTextureException(final Date validFrom, final Calendar limit) {
            super(new StringBuilder().append("Decrypted textures payload is too old (").append(validFrom).append(", but we need it to be at least ").append(limit).append(")").toString());
            this.validFrom = validFrom;
            this.limit = limit;
        }
    }
    
    public static class WrongTextureOwnerException extends InsecureTextureException {
        private final GameProfile expected;
        private final UUID resultId;
        private final String resultName;
        
        public WrongTextureOwnerException(final GameProfile expected, final UUID resultId, final String resultName) {
            super(new StringBuilder().append("Decrypted textures payload was for another user (expected ").append(expected.getId()).append("/").append(expected.getName()).append(" but was for ").append(resultId).append("/").append(resultName).append(")").toString());
            this.expected = expected;
            this.resultId = resultId;
            this.resultName = resultName;
        }
    }
    
    public static class MissingTextureException extends InsecureTextureException {
        public MissingTextureException() {
            super("No texture information found");
        }
    }
}
