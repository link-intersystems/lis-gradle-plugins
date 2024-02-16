package com.link_intersystems.gradle.distribution.task.download;

import com.link_intersystems.net.http.HttpMethod;
import com.link_intersystems.net.http.HttpRequestFactory;
import com.link_intersystems.net.http.JavaHttpRequestImplementor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class BugfixJavaHttpRequestImplementor extends JavaHttpRequestImplementor {
    public BugfixJavaHttpRequestImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory) {
        super(method, httpRequestFactory);
    }

    @Override
    protected HttpURLConnection createConnection(URL url, boolean withOutput) throws IOException {
        HttpURLConnection connection = super.createConnection(url, withOutput);
        return new HttpURLConnectionDelegate(connection) {
            @Override
            public Map<String, List<String>> getHeaderFields() {
                Map<String, List<String>> headerFields = super.getHeaderFields();
                HashMap<String, List<String>> filteredHeaderFields = new HashMap<>();
                headerFields.entrySet().stream().filter(entry -> entry.getKey() != null).forEach(
                        entry -> {
                            filteredHeaderFields.put(entry.getKey(), entry.getValue());
                        }
                );
                return filteredHeaderFields;
            }
        };
    }
}
