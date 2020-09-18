package com.mojang.serialization;

import java.util.function.UnaryOperator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.StreamSupport;
import java.util.Map;
import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import com.mojang.datafixers.util.Pair;
import java.util.stream.Stream;
import com.google.common.collect.Lists;
import java.util.List;
import java.math.BigDecimal;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;

public class JsonOps implements DynamicOps<JsonElement> {
    public static final JsonOps INSTANCE;
    public static final JsonOps COMPRESSED;
    private final boolean compressed;
    
    protected JsonOps(final boolean compressed) {
        this.compressed = compressed;
    }
    
    public JsonElement empty() {
        return JsonNull.INSTANCE;
    }
    
    public <U> U convertTo(final DynamicOps<U> outOps, final JsonElement input) {
        if (input instanceof JsonObject) {
            return this.<U>convertMap(outOps, input);
        }
        if (input instanceof JsonArray) {
            return this.<U>convertList(outOps, input);
        }
        if (input instanceof JsonNull) {
            return outOps.empty();
        }
        final JsonPrimitive primitive = input.getAsJsonPrimitive();
        if (primitive.isString()) {
            return outOps.createString(primitive.getAsString());
        }
        if (primitive.isBoolean()) {
            return outOps.createBoolean(primitive.getAsBoolean());
        }
        final BigDecimal value = primitive.getAsBigDecimal();
        try {
            final long l = value.longValueExact();
            if ((byte)l == l) {
                return outOps.createByte((byte)l);
            }
            if ((short)l == l) {
                return outOps.createShort((short)l);
            }
            if ((int)l == l) {
                return outOps.createInt((int)l);
            }
            return outOps.createLong(l);
        }
        catch (ArithmeticException e) {
            final double d = value.doubleValue();
            if ((float)d == d) {
                return outOps.createFloat((float)d);
            }
            return outOps.createDouble(d);
        }
    }
    
    public DataResult<Number> getNumberValue(final JsonElement input) {
        if (input instanceof JsonPrimitive) {
            if (input.getAsJsonPrimitive().isNumber()) {
                return DataResult.<Number>success(input.getAsNumber());
            }
            if (input.getAsJsonPrimitive().isBoolean()) {
                return DataResult.<Number>success((Number)(int)(input.getAsBoolean() ? 1 : 0));
            }
            if (this.compressed && input.getAsJsonPrimitive().isString()) {
                try {
                    return DataResult.<Number>success((Number)Integer.parseInt(input.getAsString()));
                }
                catch (NumberFormatException e) {
                    return DataResult.<Number>error(new StringBuilder().append("Not a number: ").append(e).append(" ").append(input).toString());
                }
            }
        }
        if (input instanceof JsonPrimitive && input.getAsJsonPrimitive().isBoolean()) {
            return DataResult.<Number>success((Number)(int)(input.getAsJsonPrimitive().getAsBoolean() ? 1 : 0));
        }
        return DataResult.<Number>error(new StringBuilder().append("Not a number: ").append(input).toString());
    }
    
    public JsonElement createNumeric(final Number i) {
        return new JsonPrimitive(i);
    }
    
    public DataResult<Boolean> getBooleanValue(final JsonElement input) {
        if (input instanceof JsonPrimitive) {
            if (input.getAsJsonPrimitive().isBoolean()) {
                return DataResult.<Boolean>success(input.getAsBoolean());
            }
            if (input.getAsJsonPrimitive().isNumber()) {
                return DataResult.<Boolean>success(input.getAsNumber().byteValue() != 0);
            }
        }
        return DataResult.<Boolean>error(new StringBuilder().append("Not a boolean: ").append(input).toString());
    }
    
    public JsonElement createBoolean(final boolean value) {
        return new JsonPrimitive(value);
    }
    
    public DataResult<String> getStringValue(final JsonElement input) {
        if (input instanceof JsonPrimitive && (input.getAsJsonPrimitive().isString() || (input.getAsJsonPrimitive().isNumber() && this.compressed))) {
            return DataResult.<String>success(input.getAsString());
        }
        return DataResult.<String>error(new StringBuilder().append("Not a string: ").append(input).toString());
    }
    
    public JsonElement createString(final String value) {
        return new JsonPrimitive(value);
    }
    
    public DataResult<JsonElement> mergeToList(final JsonElement list, final JsonElement value) {
        if (!(list instanceof JsonArray) && list != this.empty()) {
            return DataResult.<JsonElement>error(new StringBuilder().append("mergeToList called with not a list: ").append(list).toString(), list);
        }
        final JsonArray result = new JsonArray();
        if (list != this.empty()) {
            result.addAll(list.getAsJsonArray());
        }
        result.add(value);
        return DataResult.success(result);
    }
    
    public DataResult<JsonElement> mergeToList(final JsonElement list, final List<JsonElement> values) {
        if (!(list instanceof JsonArray) && list != this.empty()) {
            return DataResult.<JsonElement>error(new StringBuilder().append("mergeToList called with not a list: ").append(list).toString(), list);
        }
        final JsonArray result = new JsonArray();
        if (list != this.empty()) {
            result.addAll(list.getAsJsonArray());
        }
        values.forEach(result::add);
        return DataResult.success(result);
    }
    
    public DataResult<JsonElement> mergeToMap(final JsonElement map, final JsonElement key, final JsonElement value) {
        if (!(map instanceof JsonObject) && map != this.empty()) {
            return DataResult.<JsonElement>error(new StringBuilder().append("mergeToMap called with not a map: ").append(map).toString(), map);
        }
        if (!(key instanceof JsonPrimitive) || (!key.getAsJsonPrimitive().isString() && !this.compressed)) {
            return DataResult.<JsonElement>error(new StringBuilder().append("key is not a string: ").append(key).toString(), map);
        }
        final JsonObject output = new JsonObject();
        if (map != this.empty()) {
            map.getAsJsonObject().entrySet().forEach(entry -> output.add((String)entry.getKey(), (JsonElement)entry.getValue()));
        }
        output.add(key.getAsString(), value);
        return DataResult.success(output);
    }
    
    public DataResult<JsonElement> mergeToMap(final JsonElement map, final MapLike<JsonElement> values) {
        if (!(map instanceof JsonObject) && map != this.empty()) {
            return DataResult.<JsonElement>error(new StringBuilder().append("mergeToMap called with not a map: ").append(map).toString(), map);
        }
        final JsonObject output = new JsonObject();
        if (map != this.empty()) {
            map.getAsJsonObject().entrySet().forEach(entry -> output.add((String)entry.getKey(), (JsonElement)entry.getValue()));
        }
        final List<JsonElement> missed = Lists.newArrayList();
        values.entries().forEach(entry -> {
            final JsonElement key = entry.getFirst();
            if (!(key instanceof JsonPrimitive) || (!key.getAsJsonPrimitive().isString() && !this.compressed)) {
                missed.add(key);
                return;
            }
            output.add(key.getAsString(), (JsonElement)entry.getSecond());
        });
        if (!missed.isEmpty()) {
            return DataResult.error(new StringBuilder().append("some keys are not strings: ").append(missed).toString(), output);
        }
        return DataResult.success(output);
    }
    
    public DataResult<Stream<Pair<JsonElement, JsonElement>>> getMapValues(final JsonElement input) {
        if (!(input instanceof JsonObject)) {
            return DataResult.<Stream<Pair<JsonElement, JsonElement>>>error(new StringBuilder().append("Not a JSON object: ").append(input).toString());
        }
        return DataResult.<Stream<Pair<JsonElement, JsonElement>>>success(input.getAsJsonObject().entrySet().stream().map(entry -> Pair.<JsonPrimitive, JsonElement>of(new JsonPrimitive((String)entry.getKey()), (JsonElement)((entry.getValue() instanceof JsonNull) ? null : entry.getValue()))));
    }
    
    public DataResult<Consumer<BiConsumer<JsonElement, JsonElement>>> getMapEntries(final JsonElement input) {
        if (!(input instanceof JsonObject)) {
            return DataResult.<Consumer<BiConsumer<JsonElement, JsonElement>>>error(new StringBuilder().append("Not a JSON object: ").append(input).toString());
        }
        return DataResult.<Consumer<BiConsumer<JsonElement, JsonElement>>>success((Consumer<BiConsumer<JsonElement, JsonElement>>)(c -> {
            for (final Map.Entry<String, JsonElement> entry : input.getAsJsonObject().entrySet()) {
                c.accept(this.createString((String)entry.getKey()), ((entry.getValue() instanceof JsonNull) ? null : entry.getValue()));
            }
        }));
    }
    
    public DataResult<MapLike<JsonElement>> getMap(final JsonElement input) {
        if (!(input instanceof JsonObject)) {
            return DataResult.<MapLike<JsonElement>>error(new StringBuilder().append("Not a JSON object: ").append(input).toString());
        }
        final JsonObject object = input.getAsJsonObject();
        return DataResult.success(new MapLike<JsonElement>() {
            @Nullable
            public JsonElement get(final JsonElement key) {
                final JsonElement element = object.get(key.getAsString());
                if (element instanceof JsonNull) {
                    return null;
                }
                return element;
            }
            
            @Nullable
            public JsonElement get(final String key) {
                final JsonElement element = object.get(key);
                if (element instanceof JsonNull) {
                    return null;
                }
                return element;
            }
            
            public Stream<Pair<JsonElement, JsonElement>> entries() {
                return (Stream<Pair<JsonElement, JsonElement>>)object.entrySet().stream().map(e -> Pair.<JsonPrimitive, Object>of(new JsonPrimitive((String)e.getKey()), e.getValue()));
            }
            
            public String toString() {
                return new StringBuilder().append("MapLike[").append(object).append("]").toString();
            }
        });
    }
    
    public JsonElement createMap(final Stream<Pair<JsonElement, JsonElement>> map) {
        final JsonObject result = new JsonObject();
        map.forEach(p -> result.add(p.getFirst().getAsString(), (JsonElement)p.getSecond()));
        return result;
    }
    
    public DataResult<Stream<JsonElement>> getStream(final JsonElement input) {
        if (input instanceof JsonArray) {
            return DataResult.<Stream<JsonElement>>success(StreamSupport.stream(input.getAsJsonArray().spliterator(), false).map(e -> (e instanceof JsonNull) ? null : e));
        }
        return DataResult.<Stream<JsonElement>>error(new StringBuilder().append("Not a json array: ").append(input).toString());
    }
    
    public DataResult<Consumer<Consumer<JsonElement>>> getList(final JsonElement input) {
        if (input instanceof JsonArray) {
            return DataResult.<Consumer<Consumer<JsonElement>>>success((Consumer<Consumer<JsonElement>>)(c -> {
                for (final JsonElement element : input.getAsJsonArray()) {
                    c.accept(((element instanceof JsonNull) ? null : element));
                }
            }));
        }
        return DataResult.<Consumer<Consumer<JsonElement>>>error(new StringBuilder().append("Not a json array: ").append(input).toString());
    }
    
    public JsonElement createList(final Stream<JsonElement> input) {
        final JsonArray result = new JsonArray();
        input.forEach(result::add);
        return result;
    }
    
    public JsonElement remove(final JsonElement input, final String key) {
        if (input instanceof JsonObject) {
            final JsonObject result = new JsonObject();
            input.getAsJsonObject().entrySet().stream().filter(entry -> !Objects.equals(entry.getKey(), key)).forEach(entry -> result.add((String)entry.getKey(), (JsonElement)entry.getValue()));
            return result;
        }
        return input;
    }
    
    public String toString() {
        return "JSON";
    }
    
    public ListBuilder<JsonElement> listBuilder() {
        return new ArrayBuilder();
    }
    
    public boolean compressMaps() {
        return this.compressed;
    }
    
    public RecordBuilder<JsonElement> mapBuilder() {
        return new JsonRecordBuilder();
    }
    
    static {
        INSTANCE = new JsonOps(false);
        COMPRESSED = new JsonOps(true);
    }
    
    private static final class ArrayBuilder implements ListBuilder<JsonElement> {
        private DataResult<JsonArray> builder;
        
        private ArrayBuilder() {
            this.builder = DataResult.<JsonArray>success(new JsonArray(), Lifecycle.stable());
        }
        
        public DynamicOps<JsonElement> ops() {
            return JsonOps.INSTANCE;
        }
        
        public ListBuilder<JsonElement> add(final JsonElement value) {
            this.builder = this.builder.<JsonArray>map((java.util.function.Function<? super JsonArray, ? extends JsonArray>)(b -> {
                b.add(value);
                return b;
            }));
            return this;
        }
        
        public ListBuilder<JsonElement> add(final DataResult<JsonElement> value) {
            this.builder = this.builder.<JsonElement, JsonArray>apply2stable((java.util.function.BiFunction<JsonArray, JsonElement, JsonArray>)((b, element) -> {
                b.add(element);
                return b;
            }), value);
            return this;
        }
        
        public ListBuilder<JsonElement> withErrorsFrom(final DataResult<?> result) {
            this.builder = this.builder.<JsonArray>flatMap((java.util.function.Function<? super JsonArray, ? extends DataResult<JsonArray>>)(r -> result.map(v -> r)));
            return this;
        }
        
        public ListBuilder<JsonElement> mapError(final UnaryOperator<String> onError) {
            this.builder = this.builder.mapError(onError);
            return this;
        }
        
        public DataResult<JsonElement> build(final JsonElement prefix) {
            final DataResult<JsonElement> result = this.builder.<JsonElement>flatMap((java.util.function.Function<? super JsonArray, ? extends DataResult<JsonElement>>)(b -> {
                if (!(prefix instanceof JsonArray) && prefix != this.ops().empty()) {
                    return DataResult.<JsonElement>error(new StringBuilder().append("Cannot append a list to not a list: ").append(prefix).toString(), prefix);
                }
                final JsonArray array = new JsonArray();
                if (prefix != this.ops().empty()) {
                    array.addAll(prefix.getAsJsonArray());
                }
                array.addAll(b);
                return DataResult.<JsonArray>success(array, Lifecycle.stable());
            }));
            this.builder = DataResult.<JsonArray>success(new JsonArray(), Lifecycle.stable());
            return result;
        }
    }
    
    private class JsonRecordBuilder extends RecordBuilder.AbstractStringBuilder<JsonElement, JsonObject> {
        protected JsonRecordBuilder() {
            super(JsonOps.this);
        }
        
        @Override
        protected JsonObject initBuilder() {
            return new JsonObject();
        }
        
        @Override
        protected JsonObject append(final String key, final JsonElement value, final JsonObject builder) {
            builder.add(key, value);
            return builder;
        }
        
        @Override
        protected DataResult<JsonElement> build(final JsonObject builder, final JsonElement prefix) {
            if (prefix == null || prefix instanceof JsonNull) {
                return DataResult.success(builder);
            }
            if (prefix instanceof JsonObject) {
                final JsonObject result = new JsonObject();
                for (final Map.Entry<String, JsonElement> entry : prefix.getAsJsonObject().entrySet()) {
                    result.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
                for (final Map.Entry<String, JsonElement> entry : builder.entrySet()) {
                    result.add((String)entry.getKey(), (JsonElement)entry.getValue());
                }
                return DataResult.success(result);
            }
            return DataResult.<JsonElement>error(new StringBuilder().append("mergeToMap called with not a map: ").append(prefix).toString(), prefix);
        }
    }
}
