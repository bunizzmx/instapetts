package com.bunizz.instapetts.web.parameters;

public class ParameterSearching {

    String word;
    String uuid;

    public ParameterSearching() {
    }

    public ParameterSearching(String word, String uuid) {
        this.word = word;
        this.uuid = uuid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
