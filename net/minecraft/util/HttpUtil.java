package net.minecraft.util;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Executors;
import net.minecraft.DefaultUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.net.ServerSocket;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.apache.logging.log4j.Logger;

public class HttpUtil {
    private static final Logger LOGGER;
    public static final ListeningExecutorService DOWNLOAD_EXECUTOR;
    
    public static int getAvailablePort() {
        try (final ServerSocket serverSocket1 = new ServerSocket(0)) {
            return serverSocket1.getLocalPort();
        }
        catch (IOException iOException1) {
            return 25564;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DOWNLOAD_EXECUTOR = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool(new ThreadFactoryBuilder().setDaemon(true).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(HttpUtil.LOGGER)).setNameFormat("Downloader %d").build()));
    }
}
