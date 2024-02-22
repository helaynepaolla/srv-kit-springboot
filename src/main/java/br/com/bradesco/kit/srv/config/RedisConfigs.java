package br.com.bradesco.kit.srv.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.time.Duration;


@Configuration
@EnableRedisRepositories
public class RedisConfigs {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfigs.class);

	@Bean
	public LettuceConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
		var clientConfigurationBuilder = LettuceClientConfiguration.builder();


		LOGGER.debug("REDIS HOST: [{}]", redisProperties.getHost());
		LOGGER.debug("REDIS PORT: [{}]", redisProperties.getPort());
		LOGGER.debug("REDIS TIMEOUT: [{}]", redisProperties.getTimeout());
		LOGGER.debug("REDIS CONECTAR USANDO SSL: [{}]", redisProperties.isUseSsl());

		if (redisProperties.isUseSsl()) {
			LOGGER.debug("REDIS CONECTANDO COM SSL.");

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

	/**
	 * para utilização com Cluster Redis
	 * public LettuceConnectionFactory redisConnectionFactory() {
	 * RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration()
	 * .clusterNode("127.0.0.1", 6379);
	 * //.clusterNode("127.0.0.1", 6380);
	 * <p>
	 * return new LettuceConnectionFactory(redisClusterConfiguration);
	 * }
	 */

	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	@Primary
	RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.afterPropertiesSet();
		redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
		redisTemplate.setKeySerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
	}
}

