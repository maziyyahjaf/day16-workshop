package sg.edu.nus.iss.day16_workshop.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import sg.edu.nus.iss.day16_workshop.model.BoardGame;
import sg.edu.nus.iss.day16_workshop.model.ErrorResponse;
import sg.edu.nus.iss.day16_workshop.service.BoardGameService;
import sg.edu.nus.iss.day16_workshop.util.Utils;


import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/boardgame")
public class BoardGameController {

    private final BoardGameService boardGameService;

    public BoardGameController(BoardGameService boardGameService) {
        this.boardGameService = boardGameService;
    }

    @PostMapping("")
    public ResponseEntity<String> insertBoardGame(@RequestBody BoardGame boardGame) {
        boardGameService.addBoardGame(Utils.keyBoardgame, boardGame);

        JsonObjectBuilder responseBuilder = Json.createObjectBuilder();
        responseBuilder.add("insert_count", boardGameService.numberOfGamesAdded(Utils.keyBoardgame))
                .add("id", Utils.keyBoardgame);
        JsonObject response = responseBuilder.build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response.toString());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardGameById(@PathVariable("id") String id) {

        Optional<BoardGame> boardGame = boardGameService.getBoardGameById(Utils.keyBoardgame, id);

        if (boardGame.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Board game not found",
                    "The board game with the given ID does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }

        return ResponseEntity.ok(boardGame.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoardGame(@PathVariable("id") String id, 
     @RequestBody BoardGame updatedGame,
     @RequestParam(required = false, defaultValue = "false") Boolean upsert) {

        Boolean updateSuccess = boardGameService.updateBoardGame(Utils.keyBoardgame, id, updatedGame, Utils.keyUpdateCount, upsert);

        if (!updateSuccess) {
            ErrorResponse errorResponse = new ErrorResponse("Board game not found",
                    "The board game with the given ID does not exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse);
        }
    
        // need to parse the uri for upsert??
        
        JsonObjectBuilder responseBuiler = Json.createObjectBuilder();
        responseBuiler.add("update_count", boardGameService.getUpdateCounter(Utils.keyUpdateCount))
                        .add("id", Utils.keyBoardgame);
        JsonObject response = responseBuiler.build();

        return ResponseEntity.ok().body(response.toString());

      

    }

}
