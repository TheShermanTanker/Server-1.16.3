package net.minecraft.commands;

import java.util.function.Function;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;

public class BrigadierExceptions implements BuiltInExceptionProvider {
    private static final Dynamic2CommandExceptionType DOUBLE_TOO_SMALL;
    private static final Dynamic2CommandExceptionType DOUBLE_TOO_BIG;
    private static final Dynamic2CommandExceptionType FLOAT_TOO_SMALL;
    private static final Dynamic2CommandExceptionType FLOAT_TOO_BIG;
    private static final Dynamic2CommandExceptionType INTEGER_TOO_SMALL;
    private static final Dynamic2CommandExceptionType INTEGER_TOO_BIG;
    private static final Dynamic2CommandExceptionType LONG_TOO_SMALL;
    private static final Dynamic2CommandExceptionType LONG_TOO_BIG;
    private static final DynamicCommandExceptionType LITERAL_INCORRECT;
    private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE;
    private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE;
    private static final DynamicCommandExceptionType READER_INVALID_ESCAPE;
    private static final DynamicCommandExceptionType READER_INVALID_BOOL;
    private static final DynamicCommandExceptionType READER_INVALID_INT;
    private static final SimpleCommandExceptionType READER_EXPECTED_INT;
    private static final DynamicCommandExceptionType READER_INVALID_LONG;
    private static final SimpleCommandExceptionType READER_EXPECTED_LONG;
    private static final DynamicCommandExceptionType READER_INVALID_DOUBLE;
    private static final SimpleCommandExceptionType READER_EXPECTED_DOUBLE;
    private static final DynamicCommandExceptionType READER_INVALID_FLOAT;
    private static final SimpleCommandExceptionType READER_EXPECTED_FLOAT;
    private static final SimpleCommandExceptionType READER_EXPECTED_BOOL;
    private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL;
    private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND;
    private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT;
    private static final SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION;
    
    public Dynamic2CommandExceptionType doubleTooLow() {
        return BrigadierExceptions.DOUBLE_TOO_SMALL;
    }
    
    public Dynamic2CommandExceptionType doubleTooHigh() {
        return BrigadierExceptions.DOUBLE_TOO_BIG;
    }
    
    public Dynamic2CommandExceptionType floatTooLow() {
        return BrigadierExceptions.FLOAT_TOO_SMALL;
    }
    
    public Dynamic2CommandExceptionType floatTooHigh() {
        return BrigadierExceptions.FLOAT_TOO_BIG;
    }
    
    public Dynamic2CommandExceptionType integerTooLow() {
        return BrigadierExceptions.INTEGER_TOO_SMALL;
    }
    
    public Dynamic2CommandExceptionType integerTooHigh() {
        return BrigadierExceptions.INTEGER_TOO_BIG;
    }
    
    public Dynamic2CommandExceptionType longTooLow() {
        return BrigadierExceptions.LONG_TOO_SMALL;
    }
    
    public Dynamic2CommandExceptionType longTooHigh() {
        return BrigadierExceptions.LONG_TOO_BIG;
    }
    
    public DynamicCommandExceptionType literalIncorrect() {
        return BrigadierExceptions.LITERAL_INCORRECT;
    }
    
    public SimpleCommandExceptionType readerExpectedStartOfQuote() {
        return BrigadierExceptions.READER_EXPECTED_START_OF_QUOTE;
    }
    
    public SimpleCommandExceptionType readerExpectedEndOfQuote() {
        return BrigadierExceptions.READER_EXPECTED_END_OF_QUOTE;
    }
    
    public DynamicCommandExceptionType readerInvalidEscape() {
        return BrigadierExceptions.READER_INVALID_ESCAPE;
    }
    
    public DynamicCommandExceptionType readerInvalidBool() {
        return BrigadierExceptions.READER_INVALID_BOOL;
    }
    
    public DynamicCommandExceptionType readerInvalidInt() {
        return BrigadierExceptions.READER_INVALID_INT;
    }
    
    public SimpleCommandExceptionType readerExpectedInt() {
        return BrigadierExceptions.READER_EXPECTED_INT;
    }
    
    public DynamicCommandExceptionType readerInvalidLong() {
        return BrigadierExceptions.READER_INVALID_LONG;
    }
    
    public SimpleCommandExceptionType readerExpectedLong() {
        return BrigadierExceptions.READER_EXPECTED_LONG;
    }
    
    public DynamicCommandExceptionType readerInvalidDouble() {
        return BrigadierExceptions.READER_INVALID_DOUBLE;
    }
    
    public SimpleCommandExceptionType readerExpectedDouble() {
        return BrigadierExceptions.READER_EXPECTED_DOUBLE;
    }
    
    public DynamicCommandExceptionType readerInvalidFloat() {
        return BrigadierExceptions.READER_INVALID_FLOAT;
    }
    
    public SimpleCommandExceptionType readerExpectedFloat() {
        return BrigadierExceptions.READER_EXPECTED_FLOAT;
    }
    
    public SimpleCommandExceptionType readerExpectedBool() {
        return BrigadierExceptions.READER_EXPECTED_BOOL;
    }
    
    public DynamicCommandExceptionType readerExpectedSymbol() {
        return BrigadierExceptions.READER_EXPECTED_SYMBOL;
    }
    
    public SimpleCommandExceptionType dispatcherUnknownCommand() {
        return BrigadierExceptions.DISPATCHER_UNKNOWN_COMMAND;
    }
    
    public SimpleCommandExceptionType dispatcherUnknownArgument() {
        return BrigadierExceptions.DISPATCHER_UNKNOWN_ARGUMENT;
    }
    
    public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
        return BrigadierExceptions.DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    }
    
    public DynamicCommandExceptionType dispatcherParseException() {
        return BrigadierExceptions.DISPATCHER_PARSE_EXCEPTION;
    }
    
    static {
        final TranslatableComponent translatableComponent;
        DOUBLE_TOO_SMALL = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.double.low", new Object[] { object2, object1 });
            return translatableComponent;
        });
        final TranslatableComponent translatableComponent2;
        DOUBLE_TOO_BIG = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.double.big", new Object[] { object2, object1 });
            return translatableComponent2;
        });
        final TranslatableComponent translatableComponent3;
        FLOAT_TOO_SMALL = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.float.low", new Object[] { object2, object1 });
            return translatableComponent3;
        });
        final TranslatableComponent translatableComponent4;
        FLOAT_TOO_BIG = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.float.big", new Object[] { object2, object1 });
            return translatableComponent4;
        });
        final TranslatableComponent translatableComponent5;
        INTEGER_TOO_SMALL = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.integer.low", new Object[] { object2, object1 });
            return translatableComponent5;
        });
        final TranslatableComponent translatableComponent6;
        INTEGER_TOO_BIG = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.integer.big", new Object[] { object2, object1 });
            return translatableComponent6;
        });
        final TranslatableComponent translatableComponent7;
        LONG_TOO_SMALL = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.long.low", new Object[] { object2, object1 });
            return translatableComponent7;
        });
        final TranslatableComponent translatableComponent8;
        LONG_TOO_BIG = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("argument.long.big", new Object[] { object2, object1 });
            return translatableComponent8;
        });
        LITERAL_INCORRECT = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("argument.literal.incorrect", new Object[] { object })));
        READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType(new TranslatableComponent("parsing.quote.expected.start"));
        READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType(new TranslatableComponent("parsing.quote.expected.end"));
        READER_INVALID_ESCAPE = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.quote.escape", new Object[] { object })));
        READER_INVALID_BOOL = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.bool.invalid", new Object[] { object })));
        READER_INVALID_INT = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.int.invalid", new Object[] { object })));
        READER_EXPECTED_INT = new SimpleCommandExceptionType(new TranslatableComponent("parsing.int.expected"));
        READER_INVALID_LONG = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.long.invalid", new Object[] { object })));
        READER_EXPECTED_LONG = new SimpleCommandExceptionType(new TranslatableComponent("parsing.long.expected"));
        READER_INVALID_DOUBLE = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.double.invalid", new Object[] { object })));
        READER_EXPECTED_DOUBLE = new SimpleCommandExceptionType(new TranslatableComponent("parsing.double.expected"));
        READER_INVALID_FLOAT = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.float.invalid", new Object[] { object })));
        READER_EXPECTED_FLOAT = new SimpleCommandExceptionType(new TranslatableComponent("parsing.float.expected"));
        READER_EXPECTED_BOOL = new SimpleCommandExceptionType(new TranslatableComponent("parsing.bool.expected"));
        READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("parsing.expected", new Object[] { object })));
        DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType(new TranslatableComponent("command.unknown.command"));
        DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType(new TranslatableComponent("command.unknown.argument"));
        DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType(new TranslatableComponent("command.expected.separator"));
        DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("command.exception", new Object[] { object })));
    }
}
