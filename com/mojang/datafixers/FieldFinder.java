package com.mojang.datafixers;

import com.mojang.datafixers.optics.Proj1;
import com.mojang.datafixers.optics.profunctors.Cartesian;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.templates.Tag;
import com.mojang.datafixers.optics.Optic;
import com.mojang.datafixers.kinds.K1;
import com.google.common.reflect.TypeToken;
import com.mojang.datafixers.optics.Optics;
import com.mojang.datafixers.optics.profunctors.Profunctor;
import java.util.Objects;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.types.Type;
import javax.annotation.Nullable;

public final class FieldFinder<FT> implements OpticFinder<FT> {
    @Nullable
    private final String name;
    private final Type<FT> type;
    
    public FieldFinder(@Nullable final String name, final Type<FT> type) {
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
        if (!(o instanceof FieldFinder)) {
            return false;
        }
        final FieldFinder<?> that = o;
        return Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.type });
    }
    
    private static final class Matcher<FT, FR> implements Type.TypeMatcher<FT, FR> {
        private final Type<FR> resultType;
        @Nullable
        private final String name;
        private final Type<FT> type;
        
        public Matcher(@Nullable final String name, final Type<FT> type, final Type<FR> resultType) {
            this.resultType = resultType;
            this.name = name;
            this.type = type;
        }
        
        public <S> Either<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException> match(final Type<S> targetType) {
            if (this.name == null && this.type.equals(targetType, true, false)) {
                return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>left(new TypedOptic<S, Object, FT, FR>(Profunctor.Mu.TYPE_TOKEN, targetType, this.resultType, (Type<FT>)targetType, this.resultType, Optics.id()));
            }
            if (!(targetType instanceof Tag.TagType)) {
                if (targetType instanceof TaggedChoice.TaggedChoiceType) {
                    final TaggedChoice.TaggedChoiceType<FT> choiceType = (TaggedChoice.TaggedChoiceType<FT>)(TaggedChoice.TaggedChoiceType)targetType;
                    if (Objects.equals(this.name, choiceType.getName())) {
                        if (!Objects.equals(this.type, choiceType.getKeyType())) {
                            return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>right(new Type.FieldNotFoundException(String.format("Type error for field \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, this.type, choiceType.getKeyType() })));
                        }
                        if (!Objects.equals(this.type, this.resultType)) {
                            return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>right(new Type.FieldNotFoundException("TaggedChoiceType key type change is unsupported."));
                        }
                        return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>left(this.capChoice(choiceType));
                    }
                }
                return Either.right(new Type.Continue());
            }
            final Tag.TagType<S> tagType = (Tag.TagType<S>)(Tag.TagType)targetType;
            if (!Objects.equals(tagType.name(), this.name)) {
                return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>right(new Type.FieldNotFoundException(String.format("Not found: \"%s\" (in type: %s)", new Object[] { this.name, targetType })));
            }
            if (!Objects.equals(this.type, tagType.element())) {
                return Either.<TypedOptic<S, ?, FT, FR>, Type.FieldNotFoundException>right(new Type.FieldNotFoundException(String.format("Type error for field \"%s\": expected type: %s, actual type: %s)", new Object[] { this.name, this.type, tagType.element() })));
            }
            return Either.left(new TypedOptic<S, FR, FT, FR>(Profunctor.Mu.TYPE_TOKEN, tagType, DSL.<FR>field(tagType.name(), this.resultType), this.type, this.resultType, Optics.id()));
        }
        
        private <V> TypedOptic<Pair<FT, V>, ?, FT, FT> capChoice(final Type<?> choiceType) {
            return new TypedOptic<Pair<FT, V>, Object, FT, FT>(Cartesian.Mu.TYPE_TOKEN, (Type<Pair<FT, V>>)choiceType, choiceType, this.type, this.type, new Proj1<FT, V, FT>());
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
