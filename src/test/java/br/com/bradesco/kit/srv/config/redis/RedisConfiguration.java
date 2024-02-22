package br.com.bradesco.kit.srv.config.redis;

import br.com.bradesco.kit.srv.config.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {


	@Bean
	public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
		var clientConfigurationBuilder = LettuceClientConfiguration.builder();

		if (redisProperties.isUseSsl()) {
			clientConfigurationBuilder = clientConfigurationBuilder
					.useSsl()
					.and();
		}

		var clientConfiguration = clientConfigurationBuilder
				.commandTimeout(Duration.ofMillis(redisProperties.getTimeout()))
				.shutdownTimeout(Duration.ZERO)
				.build();

		var redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(),
				redisProperties.getPort());

		redisStandaloneConfiguration.setPassword(redisProperties.getPassword());

		var connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration,
				clientConfiguration);
		connectionFactory.afterPropertiesSet();

		return connectionFactory;
	}


	@Bean
	public RedisTemplate<?, ?> redisTemplate(LettuceConnectionFactory connectionFactory) {
		RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		return template;
	}
}