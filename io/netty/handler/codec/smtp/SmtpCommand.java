package io.netty.handler.codec.smtp;

import java.util.HashMap;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.AsciiString;
import java.util.Map;

public final class SmtpCommand {
    public static final SmtpCommand EHLO;
    public static final SmtpCommand HELO;
    public static final SmtpCommand MAIL;
    public static final SmtpCommand RCPT;
    public static final SmtpCommand DATA;
    public static final SmtpCommand NOOP;
    public static final SmtpCommand RSET;
    public static final SmtpCommand EXPN;
    public static final SmtpCommand VRFY;
    public static final SmtpCommand HELP;
    public static final SmtpCommand QUIT;
    private static final Map<String, SmtpCommand> COMMANDS;
    private final AsciiString name;
    
    public static SmtpCommand valueOf(final CharSequence commandName) {
        ObjectUtil.<CharSequence>checkNotNull(commandName, "commandName");
        final SmtpCommand command = (SmtpCommand)SmtpCommand.COMMANDS.get(commandName.toString());
        return (command != null) ? command : new SmtpCommand(AsciiString.of(commandName));
    }
    
    private SmtpCommand(final AsciiString name) {
        this.name = name;
    }
    
    public AsciiString name() {
        return this.name;
    }
    
    void encode(final ByteBuf buffer) {
        ByteBufUtil.writeAscii(buffer, (CharSequence)this.name);
    }
    
    boolean isContentExpected() {
        return this.equals(SmtpCommand.DATA);
    }
    
    public int hashCode() {
        return this.name.hashCode();
    }
    
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof SmtpCommand && this.name.contentEqualsIgnoreCase((CharSequence)((SmtpCommand)obj).name()));
    }
    
    public String toString() {
        return new StringBuilder().append("SmtpCommand{name=").append(this.name).append('}').toString();
    }
    
    static {
        EHLO = new SmtpCommand(AsciiString.cached("EHLO"));
        HELO = new SmtpCommand(AsciiString.cached("HELO"));
        MAIL = new SmtpCommand(AsciiString.cached("MAIL"));
        RCPT = new SmtpCommand(AsciiString.cached("RCPT"));
        DATA = new SmtpCommand(AsciiString.cached("DATA"));
        NOOP = new SmtpCommand(AsciiString.cached("NOOP"));
        RSET = new SmtpCommand(AsciiString.cached("RSET"));
        EXPN = new SmtpCommand(AsciiString.cached("EXPN"));
        VRFY = new SmtpCommand(AsciiString.cached("VRFY"));
        HELP = new SmtpCommand(AsciiString.cached("HELP"));
        QUIT = new SmtpCommand(AsciiString.cached("QUIT"));
        (COMMANDS = (Map)new HashMap()).put(SmtpCommand.EHLO.name().toString(), SmtpCommand.EHLO);
        SmtpCommand.COMMANDS.put(SmtpCommand.HELO.name().toString(), SmtpCommand.HELO);
        SmtpCommand.COMMANDS.put(SmtpCommand.MAIL.name().toString(), SmtpCommand.MAIL);
        SmtpCommand.COMMANDS.put(SmtpCommand.RCPT.name().toString(), SmtpCommand.RCPT);
        SmtpCommand.COMMANDS.put(SmtpCommand.DATA.name().toString(), SmtpCommand.DATA);
        SmtpCommand.COMMANDS.put(SmtpCommand.NOOP.name().toString(), SmtpCommand.NOOP);
        SmtpCommand.COMMANDS.put(SmtpCommand.RSET.name().toString(), SmtpCommand.RSET);
        SmtpCommand.COMMANDS.put(SmtpCommand.EXPN.name().toString(), SmtpCommand.EXPN);
        SmtpCommand.COMMANDS.put(SmtpCommand.VRFY.name().toString(), SmtpCommand.VRFY);
        SmtpCommand.COMMANDS.put(SmtpCommand.HELP.name().toString(), SmtpCommand.HELP);
        SmtpCommand.COMMANDS.put(SmtpCommand.QUIT.name().toString(), SmtpCommand.QUIT);
    }
}
