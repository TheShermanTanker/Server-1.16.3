package io.netty.channel;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import io.netty.util.ReferenceCounted;

public interface FileRegion extends ReferenceCounted {
    long position();
    
    @Deprecated
    long transfered();
    
    long transferred();
    
    long count();
    
    long transferTo(final WritableByteChannel writableByteChannel, final long long2) throws IOException;
    
    FileRegion retain();
    
    FileRegion retain(final int integer);
    
    FileRegion touch();
    
    FileRegion touch(final Object object);
}
