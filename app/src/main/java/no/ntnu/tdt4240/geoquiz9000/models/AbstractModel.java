package no.ntnu.tdt4240.geoquiz9000.models;


import android.app.Activity;

import java.util.HashMap;

import io.objectbox.Box;
import no.ntnu.tdt4240.geoquiz9000.database.DatabaseLayer;

//Class containing variables to use in the game, plus getters and setters to the variables.

public abstract class AbstractModel {

    public String question;
    public int score; //or do we want a double?
    public float longitude;
    public float latitude;
    public float distance; //Difference between player answer and the correct answer. Do we use manhattan or euler distance?
    public String correctAnswer;//A String that is returned after the player has guessed, which says where the picture is from.
    public HashMap<String,String> answerToQuestion;
    // Hashmap where question is the key and the correctAnswer is the value. I have used Strings, this needs to be changed, because
    // the key should be a picture or something?
    // Need also a way to relate the question to the correct position (longitude and latitude).

    public void save(Activity activity) {
        DatabaseLayer layer = DatabaseLayer.getInstance(activity);
        Box box = layer.getBoxFor(this.getClass());
        box.put(this);
    }


    public float getDistance() {
        return distance;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }



    public String getQuestion(){
        return question;
    };
    public void setQuestion(String Question){
        this.question = Question;
    }

    public int getScore() {
        return score;
    }


    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

}
