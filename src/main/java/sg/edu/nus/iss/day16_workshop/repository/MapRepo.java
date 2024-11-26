package sg.edu.nus.iss.day16_workshop.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day16_workshop.model.BoardGame;
import sg.edu.nus.iss.day16_workshop.util.Utils;

@Repository
public class MapRepo {

    @Qualifier(Utils.template01)
    private final RedisTemplate<String, Object> template;

    public MapRepo(@Qualifier(Utils.template01) RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public void addBoardGame(String redisKey, String boardGameId, BoardGame boardGame) {
        String boardGameInfo = boardGame.toString();
        template.opsForHash().put(redisKey, boardGameId, boardGameInfo);
    }

    public Long numberOfGamesAdded(String redisKey) {
        return template.opsForHash().size(redisKey);
    }

    public Object getBoardGame(String redisKey, String boardGameId) {
        return template.opsForHash().get(redisKey, boardGameId);
    }

    public Map<Object, Object> getAllBoardGames(String redisKey) {
        return template.opsForHash().entries(redisKey);
    }

    public void saveBoardGame(String redisKey, String boardGameId, BoardGame boardGame) {
        String updatedBoardGameInfo = boardGame.toString();
        template.opsForHash().put(redisKey, boardGameId, updatedBoardGameInfo);
    }



    
    
}
