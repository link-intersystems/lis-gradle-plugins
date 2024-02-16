package com.link_intersystems.gradle.distribution.task.download;

import com.link_intersystems.net.http.HttpMethod;
import com.link_intersystems.net.http.HttpRequestFactory;
import com.link_intersystems.net.http.HttpRequestImplementor;

class BugfixHttpRequestFactory extends HttpRequestFactory {
    @Override
    protected HttpRequestImplementor createImplementor(HttpMethod method, HttpRequestFactory httpRequestFactory) {

        return new BugfixJavaHttpRequestImplementor(method, httpRequestFactory);
    }
}
