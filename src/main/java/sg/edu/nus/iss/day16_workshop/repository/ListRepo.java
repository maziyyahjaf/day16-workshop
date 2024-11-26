package sg.edu.nus.iss.day16_workshop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day16_workshop.util.Utils;

@Repository
public class ListRepo {

    @Qualifier(Utils.template02)
    private final RedisTemplate<String, Object> template;

    public ListRepo(@Qualifier(Utils.template02) RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void rightPush(String redisKey, String value) {
        template.opsForList().rightPush(redisKey, value);
    }

    public List<Object> getList(String redisKey) {
        return template.opsForList().range(redisKey, 0, -1);
    }

    public Long listSize(String redisKey) {
        return template.opsForList().size(redisKey);
    }
    
}
