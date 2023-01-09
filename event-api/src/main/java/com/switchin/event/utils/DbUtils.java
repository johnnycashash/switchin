package com.switchin.event.utils;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class DbUtils {
    private static final String HOST_CONFIG = "datasource.host";
    private static final String PORT_CONFIG = "datasource.port";
    private static final String CERT_PATH = "datasource.certPath";
    private static final String SCHEME = "datasource.scheme";
    private static final String USERNAME_CONFIG = "datasource.username";
    private static final String PASSWORD_CONFIG = "datasource.password";

    private DbUtils() {

    }

    public static ElasticsearchAsyncClient buildElasticClient() {
        final Properties properties = ConfigUtils.getInstance().getProperties();

        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(properties.getProperty(USERNAME_CONFIG), System.getenv(properties.getProperty(PASSWORD_CONFIG))));
        // Create the low-level client
        RestClient httpClient = RestClient.builder(
                new HttpHost(properties.getProperty(HOST_CONFIG), Integer.parseInt(properties.getProperty(PORT_CONFIG)), properties.getProperty(SCHEME))
        ).setHttpClientConfigCallback(httpClientBuilder -> {
            try {
                return httpClientBuilder
                        .setSSLContext(TransportUtils.sslContextFromHttpCaCrt(new File(properties.getProperty(CERT_PATH))))
                        .setDefaultCredentialsProvider(credentialsProvider);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).build();

        // Create the Java API Client with the same low level client
        ElasticsearchTransport transport = new RestClientTransport(
                httpClient,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchAsyncClient(transport);
    }
}
