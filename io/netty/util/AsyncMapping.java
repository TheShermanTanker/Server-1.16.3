package io.netty.util;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface AsyncMapping<IN, OUT> {
    Future<OUT> map(final IN object, final Promise<OUT> promise);
}
