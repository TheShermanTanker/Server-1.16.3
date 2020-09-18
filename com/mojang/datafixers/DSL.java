package com.mojang.datafixers;

import com.google.common.collect.Maps;
import com.mojang.datafixers.types.constant.EmptyPartPassthrough;
import com.mojang.datafixers.types.constant.EmptyPart;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Func;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Triple;
import java.util.stream.Collector;
import java.util.function.Supplier;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Map;
import com.mojang.datafixers.types.templates.Tag;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.types.templates.Sum;
import com.mojang.datafixers.types.templates.RecursivePoint;
import org.apache.commons.lang3.ArrayUtils;
import com.mojang.datafixers.types.templates.Product;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.templates.Named;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.types.templates.Hook;
import com.mojang.datafixers.types.templates.Const;
import com.mojang.datafixers.types.templates.CompoundList;
import com.mojang.datafixers.types.templates.Check;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.Type;

public interface DSL {
    default Type<Boolean> bool() {
        return Instances.BOOL_TYPE;
    }
    
    default Type<Integer> intType() {
        return Instances.INT_TYPE;
    }
    
    default Type<Long> longType() {
        return Instances.LONG_TYPE;
    }
    
    default Type<Byte> byteType() {
        return Instances.BYTE_TYPE;
    }
    
    default Type<Short> shortType() {
        return Instances.SHORT_TYPE;
    }
    
    default Type<Float> floatType() {
        return Instances.FLOAT_TYPE;
    }
    
    default Type<Double> doubleType() {
        return Instances.DOUBLE_TYPE;
    }
    
    default Type<String> string() {
        return Instances.STRING_TYPE;
    }
    
    default TypeTemplate emptyPart() {
        return constType(Instances.EMPTY_PART);
    }
    
    default Type<Unit> emptyPartType() {
        return Instances.EMPTY_PART;
    }
    
    default TypeTemplate remainder() {
        return constType(Instances.EMPTY_PASSTHROUGH);
    }
    
    default Type<Dynamic<?>> remainderType() {
        return Instances.EMPTY_PASSTHROUGH;
    }
    
    default TypeTemplate check(final String name, final int index, final TypeTemplate element) {
        return new Check(name, index, element);
    }
    
    default TypeTemplate compoundList(final TypeTemplate element) {
        return compoundList(constType(string()), element);
    }
    
    default <V> CompoundList.CompoundListType<String, V> compoundList(final Type<V> value) {
        return DSL.<String, V>compoundList(string(), value);
    }
    
    default TypeTemplate compoundList(final TypeTemplate key, final TypeTemplate element) {
        return and(new CompoundList(key, element), remainder());
    }
    
    default <K, V> CompoundList.CompoundListType<K, V> compoundList(final Type<K> key, final Type<V> value) {
        return new CompoundList.CompoundListType<K, V>(key, value);
    }
    
    default TypeTemplate constType(final Type<?> type) {
        return new Const(type);
    }
    
    default TypeTemplate hook(final TypeTemplate template, final Hook.HookFunction preRead, final Hook.HookFunction postWrite) {
        return new Hook(template, preRead, postWrite);
    }
    
    default <A> Type<A> hook(final Type<A> type, final Hook.HookFunction preRead, final Hook.HookFunction postWrite) {
        return new Hook.HookType<A>(type, preRead, postWrite);
    }
    
    default TypeTemplate list(final TypeTemplate element) {
        return new List(element);
    }
    
    default <A> List.ListType<A> list(final Type<A> first) {
        return new List.ListType<A>(first);
    }
    
    default TypeTemplate named(final String name, final TypeTemplate element) {
        return new Named(name, element);
    }
    
    default <A> Type<Pair<String, A>> named(final String name, final Type<A> element) {
        return (Type<Pair<String, A>>)new Named.NamedType(name, (Type<Object>)element);
    }
    
    default TypeTemplate and(final TypeTemplate first, final TypeTemplate second) {
        return new Product(first, second);
    }
    
    default TypeTemplate and(final TypeTemplate first, final TypeTemplate... rest) {
        if (rest.length == 0) {
            return first;
        }
        TypeTemplate result = rest[rest.length - 1];
        for (int i = rest.length - 2; i >= 0; --i) {
            result = and(rest[i], result);
        }
        return and(first, result);
    }
    
    default TypeTemplate allWithRemainder(final TypeTemplate first, final TypeTemplate... rest) {
        return and(first, (TypeTemplate[])ArrayUtils.<TypeTemplate>add(rest, remainder()));
    }
    
    default <F, G> Type<Pair<F, G>> and(final Type<F> first, final Type<G> second) {
        return (Type<Pair<F, G>>)new Product.ProductType((Type<Object>)first, (Type<Object>)second);
    }
    
    default <F, G, H> Type<Pair<F, Pair<G, H>>> and(final Type<F> first, final Type<G> second, final Type<H> third) {
        return DSL.<F, Pair<G, H>>and(first, DSL.<G, H>and(second, third));
    }
    
    default <F, G, H, I> Type<Pair<F, Pair<G, Pair<H, I>>>> and(final Type<F> first, final Type<G> second, final Type<H> third, final Type<I> forth) {
        return DSL.<F, Pair<G, Pair<H, I>>>and(first, DSL.<G, Pair<H, I>>and(second, DSL.<H, I>and(third, forth)));
    }
    
    default TypeTemplate id(final int index) {
        return new RecursivePoint(index);
    }
    
    default TypeTemplate or(final TypeTemplate left, final TypeTemplate right) {
        return new Sum(left, right);
    }
    
    default <F, G> Type<Either<F, G>> or(final Type<F> first, final Type<G> second) {
        return (Type<Either<F, G>>)new Sum.SumType((Type<Object>)first, (Type<Object>)second);
    }
    
    default TypeTemplate field(final String name, final TypeTemplate element) {
        return new Tag(name, element);
    }
    
    default <A> Tag.TagType<A> field(final String name, final Type<A> element) {
        return new Tag.TagType<A>(name, element);
    }
    
    default <K> TaggedChoice<K> taggedChoice(final String name, final Type<K> keyType, final Map<K, TypeTemplate> templates) {
        return new TaggedChoice<K>(name, keyType, templates);
    }
    
    default <K> TaggedChoice<K> taggedChoiceLazy(final String name, final Type<K> keyType, final Map<K, Supplier<TypeTemplate>> templates) {
        return DSL.taggedChoice(name, (Type<Object>)keyType, (java.util.Map<Object, TypeTemplate>)templates.entrySet().stream().map(e -> Pair.of(e.getKey(), ((Supplier)e.getValue()).get())).collect((Collector)Pair.toMap()));
    }
    
    default <K> Type<Pair<K, ?>> taggedChoiceType(final String name, final Type<K> keyType, final Map<K, ? extends Type<?>> types) {
        return (Type<Pair<K, ?>>)Instances.TAGGED_CHOICE_TYPE_CACHE.computeIfAbsent(Triple.<String, Type<K>, Map<K, ? extends Type<?>>>of(name, keyType, types), k -> new TaggedChoice.TaggedChoiceType(k.getLeft(), (Type)k.getMiddle(), (Map)k.getRight()));
    }
    
    default <A, B> Type<Function<A, B>> func(final Type<A> input, final Type<B> output) {
        return (Type<Function<A, B>>)new Func((Type<Object>)input, (Type<Object>)output);
    }
    
    default <A> Type<Either<A, Unit>> optional(final Type<A> type) {
        return DSL.<A, Unit>or(type, emptyPartType());
    }
    
    default TypeTemplate optional(final TypeTemplate value) {
        return or(value, emptyPart());
    }
    
    default TypeTemplate fields(final String name1, final TypeTemplate element1) {
        return allWithRemainder(field(name1, element1));
    }
    
    default TypeTemplate fields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2) {
        return allWithRemainder(field(name1, element1), field(name2, element2));
    }
    
    default TypeTemplate fields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3) {
        return allWithRemainder(field(name1, element1), field(name2, element2), field(name3, element3));
    }
    
    default TypeTemplate fields(final String name, final TypeTemplate element, final TypeTemplate rest) {
        return and(field(name, element), rest);
    }
    
    default TypeTemplate fields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final TypeTemplate rest) {
        return and(field(name1, element1), field(name2, element2), rest);
    }
    
    default TypeTemplate fields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3, final TypeTemplate rest) {
        return and(field(name1, element1), field(name2, element2), field(name3, element3), rest);
    }
    
    default TypeTemplate optionalFields(final String name, final TypeTemplate element) {
        return allWithRemainder(optional(field(name, element)));
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2) {
        return allWithRemainder(optional(field(name1, element1)), optional(field(name2, element2)));
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3) {
        return allWithRemainder(optional(field(name1, element1)), optional(field(name2, element2)), optional(field(name3, element3)));
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3, final String name4, final TypeTemplate element4) {
        return allWithRemainder(optional(field(name1, element1)), optional(field(name2, element2)), optional(field(name3, element3)), optional(field(name4, element4)));
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3, final String name4, final TypeTemplate element4, final String name5, final TypeTemplate element5) {
        return allWithRemainder(optional(field(name1, element1)), optional(field(name2, element2)), optional(field(name3, element3)), optional(field(name4, element4)), optional(field(name5, element5)));
    }
    
    default TypeTemplate optionalFields(final String name, final TypeTemplate element, final TypeTemplate rest) {
        return and(optional(field(name, element)), rest);
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final TypeTemplate rest) {
        return and(optional(field(name1, element1)), optional(field(name2, element2)), rest);
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3, final TypeTemplate rest) {
        return and(optional(field(name1, element1)), optional(field(name2, element2)), optional(field(name3, element3)), rest);
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3, final String name4, final TypeTemplate element4, final TypeTemplate rest) {
        return and(optional(field(name1, element1)), optional(field(name2, element2)), optional(field(name3, element3)), optional(field(name4, element4)), rest);
    }
    
    default TypeTemplate optionalFields(final String name1, final TypeTemplate element1, final String name2, final TypeTemplate element2, final String name3, final TypeTemplate element3, final String name4, final TypeTemplate element4, final String name5, final TypeTemplate element5, final TypeTemplate rest) {
        return and(optional(field(name1, element1)), optional(field(name2, element2)), optional(field(name3, element3)), optional(field(name4, element4)), optional(field(name5, element5)), rest);
    }
    
    default OpticFinder<Dynamic<?>> remainderFinder() {
        return Instances.REMAINDER_FINDER;
    }
    
    default <FT> OpticFinder<FT> typeFinder(final Type<FT> type) {
        return new FieldFinder<FT>(null, type);
    }
    
    default <FT> OpticFinder<FT> fieldFinder(final String name, final Type<FT> type) {
        return new FieldFinder<FT>(name, type);
    }
    
    default <FT> OpticFinder<FT> namedChoice(final String name, final Type<FT> type) {
        return new NamedChoiceFinder<FT>(name, type);
    }
    
    default Unit unit() {
        return Unit.INSTANCE;
    }
    
    public interface TypeReference {
        String typeName();
        
        default TypeTemplate in(final Schema schema) {
            return schema.id(this.typeName());
        }
    }
    
    public static final class Instances {
        private static final Type<Boolean> BOOL_TYPE;
        private static final Type<Integer> INT_TYPE;
        private static final Type<Long> LONG_TYPE;
        private static final Type<Byte> BYTE_TYPE;
        private static final Type<Short> SHORT_TYPE;
        private static final Type<Float> FLOAT_TYPE;
        private static final Type<Double> DOUBLE_TYPE;
        private static final Type<String> STRING_TYPE;
        private static final Type<Unit> EMPTY_PART;
        private static final Type<Dynamic<?>> EMPTY_PASSTHROUGH;
        private static final OpticFinder<Dynamic<?>> REMAINDER_FINDER;
        private static final Map<Triple<String, Type<?>, Map<?, ? extends Type<?>>>, Type<? extends Pair<?, ?>>> TAGGED_CHOICE_TYPE_CACHE;
        
        static {
            BOOL_TYPE = new Const.PrimitiveType<Boolean>(Codec.BOOL);
            INT_TYPE = new Const.PrimitiveType<Integer>(Codec.INT);
            LONG_TYPE = new Const.PrimitiveType<Long>(Codec.LONG);
            BYTE_TYPE = new Const.PrimitiveType<Byte>(Codec.BYTE);
            SHORT_TYPE = new Const.PrimitiveType<Short>(Codec.SHORT);
            FLOAT_TYPE = new Const.PrimitiveType<Float>(Codec.FLOAT);
            DOUBLE_TYPE = new Const.PrimitiveType<Double>(Codec.DOUBLE);
            STRING_TYPE = new Const.PrimitiveType<String>(Codec.STRING);
            EMPTY_PART = new EmptyPart();
            EMPTY_PASSTHROUGH = new EmptyPartPassthrough();
            REMAINDER_FINDER = DSL.remainderType().finder();
            TAGGED_CHOICE_TYPE_CACHE = (Map)Maps.newConcurrentMap();
        }
    }
}
