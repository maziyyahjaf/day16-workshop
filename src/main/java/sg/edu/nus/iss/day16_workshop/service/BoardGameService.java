package sg.edu.nus.iss.day16_workshop.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day16_workshop.model.BoardGame;
import sg.edu.nus.iss.day16_workshop.repository.MapRepo;
import sg.edu.nus.iss.day16_workshop.repository.ValueRepo;

@Service
public class BoardGameService {

    private final MapRepo mapRepo;
    private final ValueRepo valueRepo;

    public BoardGameService(MapRepo mapRepo, ValueRepo valueRepo) {
        this.mapRepo = mapRepo;
        this.valueRepo = valueRepo;
    }

    public void addBoardGame(String redisKey, BoardGame boardGame) {
        String boardGameId = boardGame.getId();
        mapRepo.addBoardGame(redisKey, boardGameId, boardGame);
    }

    public Long numberOfGamesAdded(String redisKey) {
        return mapRepo.numberOfGamesAdded(redisKey);
    }

    public Optional<BoardGame> getBoardGameById(String redisKey, String boardGameId) {
        Object bg = mapRepo.getBoardGame(redisKey, boardGameId);

        // how to account when bg is null
        if (bg == null) {
            return Optional.empty();
        }

        try {
            // Convert to String and parse
            String bgToString = bg.toString();

            // Split data using regex, accounting for nested arrays
            String[] rawData = bgToString.split(",(?![^\\[\\]]*[\\]])");

            // Validate the number of fields
            if (rawData.length < 8) {
                return Optional.empty(); // Unexpected structure
            }

            // Extract and parse fields
            String id = rawData[0].trim();
            String name = rawData[1].trim();
            Integer players = Integer.parseInt(rawData[2].trim());
            String type = rawData[3].trim();
            String publisher = rawData[4].trim();
            Integer releaseYear = Integer.parseInt(rawData[5].trim());
            Double rating = Double.parseDouble(rawData[6].trim());

            // Parse the tags array
            String tagsRaw = rawData[7].trim();
            tagsRaw = tagsRaw.substring(1, tagsRaw.length() - 1); // Remove square brackets
            String[] tags = tagsRaw.split(",\\s*"); // Split by commas and trim spaces

            BoardGame boardGame = new BoardGame(id, name, players, type, publisher, releaseYear, rating, tags);
            return Optional.of(boardGame);

        } catch (Exception e) {
            // Log the error and return Optional.empty() on any parsing issues
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public Boolean updateBoardGame(String redisKey, String boardGameId, BoardGame updatedGame, String valueRedisKey,
            boolean upsert) {

        Optional<BoardGame> existingGame = getBoardGameById(redisKey, boardGameId);

        // i dont need to account if board game is an Optional.Empty
        if (existingGame.isEmpty()) {
            if (upsert) {
                // if upsert is true, create a new board game
                mapRepo.saveBoardGame(redisKey, boardGameId, updatedGame);
                updateGameSuccessCount(valueRedisKey);
                return true;
            }
            return false;
        }

        BoardGame boardGame = existingGame.get();

        // validate IDs
        if (!boardGame.getId().equals(updatedGame.getId())) {
            return false;
        }

        // Update fields
        boardGame.setName(updatedGame.getName());
        boardGame.setPlayers(updatedGame.getPlayers());
        boardGame.setType(updatedGame.getType());
        boardGame.setPublisher(updatedGame.getPublisher());
        boardGame.setReleaseYear(updatedGame.getReleaseYear());
        boardGame.setRating(updatedGame.getRating());
        boardGame.setTags(updatedGame.getTags());

        // Save updated game
        mapRepo.saveBoardGame(redisKey, boardGameId, boardGame);
        updateGameSuccessCount(valueRedisKey);
        return true;

    }

    public void updateGameSuccessCount(String valueRedisKey) {
        // use opsforvalue??
        valueRepo.increaseUpdateCounter(valueRedisKey);

    }

    public int getUpdateCounter(String valueRedisKey) {
        return valueRepo.getUpdateCounter(valueRedisKey);
    }

}
