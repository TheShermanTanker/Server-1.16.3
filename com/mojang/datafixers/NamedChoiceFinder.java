package com.mojang.datafixers;

import com.mojang.datafixers.types.templates.Tag;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Objects;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.types.Type;

final class NamedChoiceFinder<FT> implements OpticFinder<FT> {
    private final String name;
    private final Type<FT> type;
    
    public NamedChoiceFinder(final String name, final Type<FT> type) {
        this.name = name;
        this.type = type;
    }
    
    public Type<FT> type() {
        return this.type;
    }
    
    public <A, FR> Either<TypedOptic<A, ?, FT, FR>, Type.FieldNotFoundException> findType(final Type<A> containerType, final Type<FR> resultType, final boolean recurse) {
        return containerType.<FT, FR>findTypeCached(this.type, resultType, new Matcher<FT, FR>(this.name, this.type, resultType), recurse);
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NamedChoiceFinder)) {
            return false;
        }
        final NamedChoiceFinder<?> that = o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.type });
    }
    
    private static class Matcher<FT, FR> implements Type.TypeMatcher<FT, FR> {
        private final Type<FR> resultType;
        private final String name;
        private final Type<FT> type;
        
        public Matcher(final String name, final Type<FT> type, final Type<FR> resultType) {
            this.resultType = resultType;
            this.name = name;
            this.type = type;
        }
        
        public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(final Type<S> targetType) {
            if (targetType instanceof TaggedChoice.TaggedChoiceType) {
                final TaggedChoice.TaggedChoiceType<?> choiceType = (TaggedChoice.TaggedChoiceType)targetType;
                final Type<?> elementType = choiceType.types().get(this.name);
                if (elementType == null) {
                    return Either.right(new Type.Continue());
                }
                if (!Objects.equals(this.type, elementType)) {
                    return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>right(new Type.FieldNotFoundException(String.format("Type error for choice type \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, targetType, elementType })));
                }
                return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>left(TypedOptic.tagged(choiceType, this.name, this.type, this.resultType));
            }
            else {
                if (targetType instanceof Tag.TagType) {
                    return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>right(new Type.FieldNotFoundException("in tag"));
                }
                return Either.right(new Type.Continue());
            }
        }
        
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Matcher<?, ?> matcher = o;
            return Objects.equals(this.resultType, matcher.resultType) && Objects.equals(this.name, matcher.name) && Objects.equals(this.type, matcher.type);
        }
        
        public int hashCode() {
            return Objects.hash(new Object[] { this.resultType, this.name, this.type });
        }
    }
}
