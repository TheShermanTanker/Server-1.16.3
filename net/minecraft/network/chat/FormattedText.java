package net.minecraft.network.chat;

import net.minecraft.util.Unit;
import java.util.Optional;

public interface FormattedText {
    public static final Optional<Unit> STOP_ITERATION = Optional.of(Unit.INSTANCE);
    public static final FormattedText EMPTY = new FormattedText() {
        public <T> Optional<T> visit(final ContentConsumer<T> a) {
            return (Optional<T>)Optional.empty();
        }
    };
    
     <T> Optional<T> visit(final ContentConsumer<T> a);
    
    default FormattedText of(final String string) {
        return new FormattedText() {
            public <T> Optional<T> visit(final ContentConsumer<T> a) {
                return a.accept(string);
            }
        };
    }
    
    default String getString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        this.visit(string -> {
            stringBuilder2.append(string);
            return (java.util.Optional<Object>)Optional.empty();
        });
        return stringBuilder2.toString();
    }
    
    public interface ContentConsumer<T> {
        Optional<T> accept(final String string);
    }
}
