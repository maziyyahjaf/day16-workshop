package sg.edu.nus.iss.day16_workshop.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day16_workshop.util.Utils;

@Repository
public class ValueRepo {

    @Qualifier(Utils.template03)
    private final RedisTemplate<String, Integer> template;

    public ValueRepo(@Qualifier(Utils.template03) RedisTemplate<String, Integer> template) {
        this.template = template;
    }

    public void increaseUpdateCounter(String redisKey) {
        template.opsForValue().increment(redisKey);
    }

    public Integer getUpdateCounter(String redisKey) {
        return template.opsForValue().get(redisKey);
    }

    
    
}
