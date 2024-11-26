package sg.edu.nus.iss.day16_workshop.model;

import java.util.Arrays;

public class BoardGame {

    private String id;
    private String name;
    private Integer players;
    private String type;
    private String publisher;
    private Integer releaseYear;
    private Double rating;
    private String[] tags;



    public BoardGame(String id, String name, Integer players, String type, String publisher, Integer releaseYear,
            Double rating, String[] tags) {
        this.id = id;
        this.name = name;
        this.players = players;
        this.type = type;
        this.publisher = publisher;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.tags = tags;
    }



    public String getId() {
        return id;
    }



    public void setId(String id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public Integer getPlayers() {
        return players;
    }



    public void setPlayers(Integer players) {
        this.players = players;
    }



    public String getType() {
        return type;
    }



    public void setType(String type) {
        this.type = type;
    }



    public String getPublisher() {
        return publisher;
    }



    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }



    public Integer getReleaseYear() {
        return releaseYear;
    }



    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }



    public Double getRating() {
        return rating;
    }



    public void setRating(Double rating) {
        this.rating = rating;
    }



    public String[] getTags() {
        return tags;
    }



    public void setTags(String[] tags) {
        this.tags = tags;
    }



    @Override
    public String toString() {
        return  id + "," + name + "," + players + "," + type + ","
                + publisher + "," + releaseYear + "," + rating + "," + Arrays.toString(tags);
    }

    
    

    
}
