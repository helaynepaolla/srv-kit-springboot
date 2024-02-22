package br.com.bradesco.kit.srv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {

	private final int port;
	private final int timeout;
	private final String host;
	private final String password;
	private final boolean isUseSsl;

	public RedisProperties(
			@Value("${spring.redis.port}") int port,
			@Value("${spring.redis.timeout}") int timeout,
			@Value("${spring.redis.host}") String host,
			@Value("${spring.redis.password}") String password,
			@Value("${spring.redis.ssl}") boolean ssl) {

		this.port = port;
		this.host = host;
		this.password = password;
		this.isUseSsl = ssl;
		this.timeout = timeout;
	}

	public int getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public int getTimeout() {
		return timeout;
	}
	public boolean isUseSsl() { return isUseSsl; }
}