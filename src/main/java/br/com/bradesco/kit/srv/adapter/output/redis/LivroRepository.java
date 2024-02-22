package br.com.bradesco.kit.srv.adapter.output.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;


@Repository
public class LivroRepository {
    private final ValueOperations<String, Object> valueOps;

    public LivroRepository(final RedisTemplate<String, Object> redisTemplate) {
        valueOps = redisTemplate.opsForValue();
    }

    public void saveToCache(final String key, final Object data) {
        valueOps.set(key, data);
    }

    public Object getCachedValue(final String key) {
        return valueOps.get(key);
    }

    public void deleteCachedValue(final String key) {
        valueOps.getOperations().delete(key);
    }
}
