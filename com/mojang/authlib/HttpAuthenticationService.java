package com.mojang.authlib;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.Charsets;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.lang3.Validate;
import java.net.Proxy;
import org.apache.logging.log4j.Logger;

public abstract class HttpAuthenticationService extends BaseAuthenticationService {
    private static final Logger LOGGER;
    private final Proxy proxy;
    
    protected HttpAuthenticationService(final Proxy proxy) {
        Validate.<Proxy>notNull(proxy);
        this.proxy = proxy;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    protected HttpURLConnection createUrlConnection(final URL url) throws IOException {
        Validate.<URL>notNull(url);
        HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Opening connection to ").append(url).toString());
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection(this.proxy);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        return connection;
    }
    
    public String performPostRequest(final URL url, final String post, final String contentType) throws IOException {
        Validate.<URL>notNull(url);
        Validate.<String>notNull(post);
        Validate.<String>notNull(contentType);
        final HttpURLConnection connection = this.createUrlConnection(url);
        final byte[] postAsBytes = post.getBytes(Charsets.UTF_8);
        connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
        connection.setRequestProperty("Content-Length", new StringBuilder().append("").append(postAsBytes.length).toString());
        connection.setDoOutput(true);
        HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Writing POST data to ").append(url).append(": ").append(post).toString());
        OutputStream outputStream = null;
        try {
            outputStream = connection.getOutputStream();
            IOUtils.write(postAsBytes, outputStream);
        }
        finally {
            IOUtils.closeQuietly(outputStream);
        }
        HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Reading data from ").append(url).toString());
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            final String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Successful read, server response was ").append(connection.getResponseCode()).toString());
            HttpAuthenticationService.LOGGER.debug("Response: " + result);
            return result;
        }
        catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream != null) {
                HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Reading error page from ").append(url).toString());
                final String result2 = IOUtils.toString(inputStream, Charsets.UTF_8);
                HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Successful read, server response was ").append(connection.getResponseCode()).toString());
                HttpAuthenticationService.LOGGER.debug("Response: " + result2);
                return result2;
            }
            HttpAuthenticationService.LOGGER.debug("Request failed", (Throwable)e);
            throw e;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public String performGetRequest(final URL url) throws IOException {
        Validate.<URL>notNull(url);
        final HttpURLConnection connection = this.createUrlConnection(url);
        HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Reading data from ").append(url).toString());
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            final String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Successful read, server response was ").append(connection.getResponseCode()).toString());
            HttpAuthenticationService.LOGGER.debug("Response: " + result);
            return result;
        }
        catch (IOException e) {
            IOUtils.closeQuietly(inputStream);
            inputStream = connection.getErrorStream();
            if (inputStream != null) {
                HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Reading error page from ").append(url).toString());
                final String result2 = IOUtils.toString(inputStream, Charsets.UTF_8);
                HttpAuthenticationService.LOGGER.debug(new StringBuilder().append("Successful read, server response was ").append(connection.getResponseCode()).toString());
                HttpAuthenticationService.LOGGER.debug("Response: " + result2);
                return result2;
            }
            HttpAuthenticationService.LOGGER.debug("Request failed", (Throwable)e);
            throw e;
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public static URL constantURL(final String url) {
        try {
            return new URL(url);
        }
        catch (MalformedURLException ex) {
            throw new Error("Couldn't create constant for " + url, (Throwable)ex);
        }
    }
    
    public static String buildQuery(final Map<String, Object> query) {
        if (query == null) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for (final Map.Entry<String, Object> entry : query.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            try {
                builder.append(URLEncoder.encode((String)entry.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException e) {
                HttpAuthenticationService.LOGGER.error("Unexpected exception building query", (Throwable)e);
            }
            if (entry.getValue() != null) {
                builder.append('=');
                try {
                    builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException e) {
                    HttpAuthenticationService.LOGGER.error("Unexpected exception building query", (Throwable)e);
                }
            }
        }
        return builder.toString();
    }
    
    public static URL concatenateURL(final URL url, final String query) {
        try {
            if (url.getQuery() != null && url.getQuery().length() > 0) {
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "&" + query);
            }
            return new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "?" + query);
        }
        catch (MalformedURLException ex) {
            throw new IllegalArgumentException("Could not concatenate given URL with GET arguments!", (Throwable)ex);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
