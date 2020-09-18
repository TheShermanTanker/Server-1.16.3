package io.netty.handler.codec.serialization;

public interface ClassResolver {
    Class<?> resolve(final String string) throws ClassNotFoundException;
}
