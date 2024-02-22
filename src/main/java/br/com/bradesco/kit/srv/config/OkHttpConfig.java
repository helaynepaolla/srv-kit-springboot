package br.com.bradesco.kit.srv.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.tls.Certificates;
import okhttp3.tls.HandshakeCertificates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Realiza configuração do OkHttpClient para ser reusado
 */
@Configuration
public class OkHttpConfig implements Supplier<ClientHttpRequestFactory> {

    /**
     * Criando um OkHttpClient para ser utilizado comumente
     * <p>
     * ok-http-client - connectTimeout - default - 10 seg
     * ok-http-client - readTimeout - default - 10 seg
     * ok-http-client - writeTimeout - default - 10 seg
     * ok-http-client - retryOnConnectionFailure - default - true
     *
     * @return ClientHttpRequestFactory objeto factory de HTTPClient
     * @see <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/">...</a>
     * @see <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/-builder/">...</a>
     */

    public static final String BEGIN_CERTIFICATE = "-----BEGIN CERTIFICATE-----";
    private static final Logger LOGGER_TECNICO = LoggerFactory.getLogger(OkHttpConfig.class);
    @Value("${okhttp-configuracao-geral.connect-timeout-millis}")
    private long connectTimeout;
    @Value("${okhttp-configuracao-geral.read-timeout-millis}")
    private long readTimeout;
    @Value("${okhttp-configuracao-geral.write-timeout-millis}")
    private long writeTimeout;
    @Value("${okhttp-configuracao-geral.retryOnConnectionFailure}")
    private boolean retryOnConnectionFailure;
    /**
     * The maximum number of idle connections for each address
     * okhttp - maxIdleConnections - default 5
     *
     * @see okhttp3.internal.connection.RealConnectionPool
     */
    @Value("${okhttp-connection-pool.max-idle-connections}")
    private int maxIdleConnections;
    /**
     * okhttp - keepAliveTime - default 60seg
     */
    @Value("${okhttp-connection-pool.keep-alive-duration-segundos}")
    private int keepAliveDurationSegundos;
    @Value("${certificados.path.certificado1}")
    private String path;

    @Override
    public ClientHttpRequestFactory get() {
        try {
            return new OkHttp3ClientHttpRequestFactory(okHttpClient());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public OkHttpClient okHttpClient() throws IOException {
        var builder = new OkHttpClient.Builder();
        var connectionPool = connectionPool();
        builder.connectionPool(connectionPool)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        builder.retryOnConnectionFailure(retryOnConnectionFailure);

        if (path != null) {
            try {
                Path pathCertificado1 = Path.of(path);
                String readPem = new String(Files.readAllBytes(pathCertificado1), Charset.defaultCharset());
                String trimmedCertificate = getCertificateWithoutPrivateKey(readPem);
                final X509Certificate letsEncryptCertificateAuthority = Certificates.decodeCertificatePem(trimmedCertificate);
                HandshakeCertificates certificates = new HandshakeCertificates.Builder()
                        .addTrustedCertificate(letsEncryptCertificateAuthority)
                        .build();
                builder.sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager());

            } catch (java.nio.file.NoSuchFileException e) {
                LOGGER_TECNICO.error("Path '{}' não encontrado. Certificado SSL nao foi carregado.", path, e);
            }
        }else {
            LOGGER_TECNICO.debug("Path para certificado nulo, certificado nao foi carregado");
        }
        return builder.build();
    }

    /**
     * Configurando o Pool de Conexões
     *
     * @return ConnectionPool bean ConnectionPool do OkHttp
     * @see <a href="https://square.github.io/okhttp/3.x/okhttp/okhttp3/ConnectionPool.html">...</a>
     * @see <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-connection-pool/">...</a>
     */
    private ConnectionPool connectionPool() {
        return new ConnectionPool(maxIdleConnections,
                keepAliveDurationSegundos, TimeUnit.MILLISECONDS);
    }

    private String getCertificateWithoutPrivateKey(String clientCertificateEncoded) {
        return clientCertificateEncoded.substring(clientCertificateEncoded.indexOf(BEGIN_CERTIFICATE));
    }
}
