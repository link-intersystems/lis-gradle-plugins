package com.link_intersystems.gradle.distribution.task.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.security.Permission;
import java.util.List;
import java.util.Map;

public class HttpURLConnectionDelegate extends HttpURLConnection {

    private HttpURLConnection target;

    protected HttpURLConnectionDelegate(HttpURLConnection target) {
        super(null);
        this.target = target;
    }


    @Override
    public void setAuthenticator(Authenticator auth) {
        target.setAuthenticator(auth);
    }

    @Override
    public String getHeaderFieldKey(int n) {
        return target.getHeaderFieldKey(n);
    }

    @Override
    public void setFixedLengthStreamingMode(int contentLength) {
        target.setFixedLengthStreamingMode(contentLength);
    }

    @Override
    public void setFixedLengthStreamingMode(long contentLength) {
        target.setFixedLengthStreamingMode(contentLength);
    }

    @Override
    public void setChunkedStreamingMode(int chunklen) {
        target.setChunkedStreamingMode(chunklen);
    }

    @Override
    public String getHeaderField(int n) {
        return target.getHeaderField(n);
    }

    public static void setFollowRedirects(boolean set) {
        HttpURLConnection.setFollowRedirects(set);
    }

    public static boolean getFollowRedirects() {
        return HttpURLConnection.getFollowRedirects();
    }

    @Override
    public void setInstanceFollowRedirects(boolean followRedirects) {
        target.setInstanceFollowRedirects(followRedirects);
    }

    @Override
    public boolean getInstanceFollowRedirects() {
        return target.getInstanceFollowRedirects();
    }

    @Override
    public void setRequestMethod(String method) throws ProtocolException {
        target.setRequestMethod(method);
    }

    @Override
    public String getRequestMethod() {
        return target.getRequestMethod();
    }

    @Override
    public int getResponseCode() throws IOException {
        return target.getResponseCode();
    }

    @Override
    public String getResponseMessage() throws IOException {
        return target.getResponseMessage();
    }

    @Override
    public long getHeaderFieldDate(String name, long Default) {
        return target.getHeaderFieldDate(name, Default);
    }

    @Override
    public void disconnect() {
        target.disconnect();
    }

    @Override
    public boolean usingProxy() {
        return target.usingProxy();
    }

    @Override
    public Permission getPermission() throws IOException {
        return target.getPermission();
    }

    @Override
    public InputStream getErrorStream() {
        return target.getErrorStream();
    }

    public static FileNameMap getFileNameMap() {
        return URLConnection.getFileNameMap();
    }

    public static void setFileNameMap(FileNameMap map) {
        URLConnection.setFileNameMap(map);
    }

    @Override
    public void connect() throws IOException {
        target.connect();
    }

    @Override
    public void setConnectTimeout(int timeout) {
        target.setConnectTimeout(timeout);
    }

    @Override
    public int getConnectTimeout() {
        return target.getConnectTimeout();
    }

    @Override
    public void setReadTimeout(int timeout) {
        target.setReadTimeout(timeout);
    }

    @Override
    public int getReadTimeout() {
        return target.getReadTimeout();
    }

    @Override
    public URL getURL() {
        return target.getURL();
    }

    @Override
    public int getContentLength() {
        return target.getContentLength();
    }

    @Override
    public long getContentLengthLong() {
        return target.getContentLengthLong();
    }

    @Override
    public String getContentType() {
        return target.getContentType();
    }

    @Override
    public String getContentEncoding() {
        return target.getContentEncoding();
    }

    @Override
    public long getExpiration() {
        return target.getExpiration();
    }

    @Override
    public long getDate() {
        return target.getDate();
    }

    @Override
    public long getLastModified() {
        return target.getLastModified();
    }

    @Override
    public String getHeaderField(String name) {
        return target.getHeaderField(name);
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return target.getHeaderFields();
    }

    @Override
    public int getHeaderFieldInt(String name, int Default) {
        return target.getHeaderFieldInt(name, Default);
    }

    @Override
    public long getHeaderFieldLong(String name, long Default) {
        return target.getHeaderFieldLong(name, Default);
    }

    @Override
    public Object getContent() throws IOException {
        return target.getContent();
    }

    @Override
    public Object getContent(Class<?>[] classes) throws IOException {
        return target.getContent(classes);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return target.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return target.getOutputStream();
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public void setDoInput(boolean doinput) {
        target.setDoInput(doinput);
    }

    @Override
    public boolean getDoInput() {
        return target.getDoInput();
    }

    @Override
    public void setDoOutput(boolean dooutput) {
        target.setDoOutput(dooutput);
    }

    @Override
    public boolean getDoOutput() {
        return target.getDoOutput();
    }

    @Override
    public void setAllowUserInteraction(boolean allowuserinteraction) {
        target.setAllowUserInteraction(allowuserinteraction);
    }

    @Override
    public boolean getAllowUserInteraction() {
        return target.getAllowUserInteraction();
    }

    public static void setDefaultAllowUserInteraction(boolean defaultallowuserinteraction) {
        URLConnection.setDefaultAllowUserInteraction(defaultallowuserinteraction);
    }

    public static boolean getDefaultAllowUserInteraction() {
        return URLConnection.getDefaultAllowUserInteraction();
    }

    @Override
    public void setUseCaches(boolean usecaches) {
        target.setUseCaches(usecaches);
    }

    @Override
    public boolean getUseCaches() {
        return target.getUseCaches();
    }

    @Override
    public void setIfModifiedSince(long ifmodifiedsince) {
        target.setIfModifiedSince(ifmodifiedsince);
    }

    @Override
    public long getIfModifiedSince() {
        return target.getIfModifiedSince();
    }

    @Override
    public boolean getDefaultUseCaches() {
        return target.getDefaultUseCaches();
    }

    @Override
    public void setDefaultUseCaches(boolean defaultusecaches) {
        target.setDefaultUseCaches(defaultusecaches);
    }

    public static void setDefaultUseCaches(String protocol, boolean defaultVal) {
        URLConnection.setDefaultUseCaches(protocol, defaultVal);
    }

    public static boolean getDefaultUseCaches(String protocol) {
        return URLConnection.getDefaultUseCaches(protocol);
    }

    @Override
    public void setRequestProperty(String key, String value) {
        target.setRequestProperty(key, value);
    }

    @Override
    public void addRequestProperty(String key, String value) {
        target.addRequestProperty(key, value);
    }

    @Override
    public String getRequestProperty(String key) {
        return target.getRequestProperty(key);
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
        return target.getRequestProperties();
    }

    @Deprecated
    public static void setDefaultRequestProperty(String key, String value) {
        URLConnection.setDefaultRequestProperty(key, value);
    }

    @Deprecated
    public static String getDefaultRequestProperty(String key) {
        return URLConnection.getDefaultRequestProperty(key);
    }

    public static void setContentHandlerFactory(ContentHandlerFactory fac) {
        URLConnection.setContentHandlerFactory(fac);
    }

    public static String guessContentTypeFromName(String fname) {
        return URLConnection.guessContentTypeFromName(fname);
    }

    public static String guessContentTypeFromStream(InputStream is) throws IOException {
        return URLConnection.guessContentTypeFromStream(is);
    }
}
