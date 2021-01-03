package io.kimmking.cache.utils;

import io.kimmking.cache.sentinel.SentinelJedis;
import redis.clients.jedis.Jedis;

import java.util.Collections;

public class RedisTool {

    private static final String LOCK_SUCCESS = "OK";

    private static final String SET_IF_NOT_EXIST = "NX";

    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    private static final String STOCK_KEY = "MBP";

    /**
     * 尝试加锁
     * @param lockKey 锁的key
     * @param requestId 锁的值，用来解锁的时候判断是自己加的锁才去解锁
     * @param expireTime 超时时间，防止死锁影响业务
     * @return 成功还是失败
     */
    public static boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        Jedis jedis = SentinelJedis.getJedis();
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 解锁
     * @param lockKey 锁的key
     * @param requestId 锁的值，用来验证是否是自己加的锁
     * @return 成功还是失败
     */
    public static boolean releaseLock(String lockKey, String requestId) {
        Jedis jedis = SentinelJedis.getJedis();
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(luaScript, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    public static boolean initStock(String stockKey, int count) {
        Jedis jedis = SentinelJedis.getJedis();
        String set = jedis.set(stockKey, String.valueOf(count));
        return Integer.parseInt(set) == 1;
    }
    /**
     * 设置初始值为10，使用100个线程进行压测，
     * @param stockKey 计数器的key
     * @return 成功还是失败
     */
    public static boolean decreCount(String stockKey) {
        Jedis jedis = SentinelJedis.getJedis();
        String luaScript = "if (redis.call('exists', KEYS[1] == 1) then local stock = tonumber(redis.call('get', KEYS[1]));" +
                "if (stock == -1) then return 1;" +
                "end;" +
                "if (stock > 0) then redis.call(incrby, KEYS[1], -1); return stock;" +
                "end;" +
                "return 0;" +
                "end;" +
                "return -1;";
        Object result = jedis.eval(luaScript, Collections.singletonList(stockKey), Collections.emptyList());

        return true;
    }


}
