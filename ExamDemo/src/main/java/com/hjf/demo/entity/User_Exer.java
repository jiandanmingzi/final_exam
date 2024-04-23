package com.hjf.demo.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjf.demo.utils.JSON_Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User_Exer {
    private  int id;
    private int userId;
    private int partId;
    private float accuracy;
    private String identifier;
    private Map<Integer, String> rightExercise = new HashMap<>();
    private Map<Integer, String> wrongExercise = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getRightExercise() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this.rightExercise);
    }

    public Map<Integer, String> getRightExerciseMap() {
        return this.rightExercise;
    }

    public void setRightExercise(String rightExercise) throws JsonProcessingException {
        JsonNode rootNode = new ObjectMapper().readTree(rightExercise);
        for (JsonNode node : rootNode) {
            JsonNode exerciseId = node.get("exerciseId");
            JsonNode answer = node.get("answer");
            List<JsonNode> list = new ArrayList<>();
            list.add(answer);
            list.add(exerciseId);
            if (JSON_Utils.checkNode(list)){
                this.rightExercise.put(exerciseId.asInt(), answer.asText());
            }
        }
    }

    public void updateRightExercise(int exerciseId, String answer) {
        this.rightExercise.put(exerciseId, answer);
        this.accuracy = (float) this.rightExercise.size() / (this.rightExercise.size() + this.rightExercise.size());
    }

    public void removeRightExercise(int exerciseId) {
        this.rightExercise.remove(exerciseId);
        this.accuracy = (float) this.rightExercise.size() / (this.rightExercise.size() + this.rightExercise.size());
    }

    public String getWrongExercise() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this.wrongExercise);
    }

    public Map<Integer, String> getWrongExerciseMap() {
        return this.wrongExercise;
    }

    public void setWrongExercise(String wrongExercise) throws JsonProcessingException {
        JsonNode rootNode = new ObjectMapper().readTree(wrongExercise);
        for (JsonNode node : rootNode) {
            JsonNode exerciseId = node.get("exerciseId");
            JsonNode answer = node.get("answer");
            List<JsonNode> list = new ArrayList<>();
            list.add(answer);
            list.add(exerciseId);
            if (JSON_Utils.checkNode(list)){
                this.wrongExercise.put(exerciseId.asInt(), answer.asText());
            }
        }
    }

    public void updateWrongExercise(int exerciseId, String answer) {
        this.wrongExercise.put(exerciseId, answer);
        this.accuracy = (float) this.rightExercise.size() / (this.rightExercise.size() + this.rightExercise.size());
    }

    public void removeWrongExercise(int exerciseId) {
        this.wrongExercise.remove(exerciseId);
        this.accuracy = (float) this.rightExercise.size() / (this.rightExercise.size() + this.rightExercise.size());
    }
}
