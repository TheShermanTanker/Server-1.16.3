package io.netty.handler.codec.smtp;

import java.util.Collections;
import io.netty.util.internal.ObjectUtil;
import java.util.List;

public final class DefaultSmtpRequest implements SmtpRequest {
    private final SmtpCommand command;
    private final List<CharSequence> parameters;
    
    public DefaultSmtpRequest(final SmtpCommand command) {
        this.command = ObjectUtil.<SmtpCommand>checkNotNull(command, "command");
        this.parameters = (List<CharSequence>)Collections.emptyList();
    }
    
    public DefaultSmtpRequest(final SmtpCommand command, final CharSequence... parameters) {
        this.command = ObjectUtil.<SmtpCommand>checkNotNull(command, "command");
        this.parameters = SmtpUtils.toUnmodifiableList(parameters);
    }
    
    public DefaultSmtpRequest(final CharSequence command, final CharSequence... parameters) {
        this(SmtpCommand.valueOf(command), parameters);
    }
    
    DefaultSmtpRequest(final SmtpCommand command, final List<CharSequence> parameters) {
        this.command = ObjectUtil.<SmtpCommand>checkNotNull(command, "command");
        this.parameters = (List<CharSequence>)((parameters != null) ? Collections.unmodifiableList((List)parameters) : Collections.emptyList());
    }
    
    public SmtpCommand command() {
        return this.command;
    }
    
    public List<CharSequence> parameters() {
        return this.parameters;
    }
    
    public int hashCode() {
        return this.command.hashCode() * 31 + this.parameters.hashCode();
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof DefaultSmtpRequest)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        final DefaultSmtpRequest other = (DefaultSmtpRequest)o;
        return this.command().equals(other.command()) && this.parameters().equals(other.parameters());
    }
    
    public String toString() {
        return new StringBuilder().append("DefaultSmtpRequest{command=").append(this.command).append(", parameters=").append(this.parameters).append('}').toString();
    }
}
