package com.doyatama.university.model;

public class CriteriaValue {

    private String id;

    private LinguisticValue value1;
    private LinguisticValue value2;
    private LinguisticValue value3;
    private LinguisticValue value4;
    private LinguisticValue value5;
    private LinguisticValue value6;
    private LinguisticValue value7;
    private LinguisticValue value8;
    private LinguisticValue value9;

    private Question question;
    private TeamTeaching team_teaching;
    private LinguisticValue linguistic_value;
    private Lecture lecture;
    private String user_id;

    private String avgOfAvgValue9;

    public CriteriaValue() {
    }

    public CriteriaValue(String id, LinguisticValue value1, LinguisticValue value2, LinguisticValue value3,
            LinguisticValue value4, LinguisticValue value5, LinguisticValue value6, LinguisticValue value7,
            LinguisticValue value8, LinguisticValue value9, Question question, TeamTeaching team_teaching,
            LinguisticValue linguisticValue, Lecture lecture, String user_id) {
        this.id = id;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
        this.value6 = value6;
        this.value7 = value7;
        this.value8 = value8;
        this.value9 = value9;
        this.question = question;
        this.team_teaching = team_teaching;
        // this.linguistic_value = linguistic_value;
        this.user_id = user_id;
        this.lecture = lecture;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinguisticValue getValue1() {
        return this.value1;
    }

    public LinguisticValue getValue2() {
        return this.value2;
    }

    public LinguisticValue getValue3() {
        return this.value3;
    }

    public LinguisticValue getValue4() {
        return this.value4;
    }

    public LinguisticValue getValue5() {
        return this.value5;
    }

    public LinguisticValue getValue6() {
        return this.value6;
    }

    public LinguisticValue getValue7() {
        return this.value7;
    }

    public LinguisticValue getValue8() {
        return this.value8;
    }

    public LinguisticValue getValue9() {
        return this.value9;
    }

    public void setAvgOfAvgValue9(String avgOfAvgValue9) {
        this.avgOfAvgValue9 = avgOfAvgValue9;
    }

    public void setValue1(LinguisticValue value1) {
        this.value1 = value1;
    }

    public void setValue2(LinguisticValue value2) {
        this.value2 = value2;
    }

    public void setValue3(LinguisticValue value3) {
        this.value3 = value3;
    }

    public void setValue4(LinguisticValue value4) {
        this.value4 = value4;
    }

    public void setValue5(LinguisticValue value5) {
        this.value5 = value5;
    }

    public void setValue6(LinguisticValue value6) {
        this.value6 = value6;
    }

    public void setValue7(LinguisticValue value7) {
        this.value7 = value7;
    }

    public void setValue8(LinguisticValue value8) {
        this.value8 = value8;
    }

    public void setValue9(LinguisticValue value9) {
        this.value9 = value9;
    }

    public String getAvgOfAvgValue9() {
        if (value9 != null) {
            this.avgOfAvgValue9 = String.valueOf(
                    (value9.getValue1() + value9.getValue2() + value9.getValue3() + value9.getValue4()) / 4.0f);
        }
        return avgOfAvgValue9;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public TeamTeaching getTeamTeaching() {
        return team_teaching;
    }

    public void setTeamTeaching(TeamTeaching team_teaching) {
        this.team_teaching = team_teaching;
    }

    public LinguisticValue getLinguisticValue() {
        return linguistic_value;
    }

    public void setLinguisticValue(LinguisticValue linguistic_value) {
        this.linguistic_value = linguistic_value;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    public String getUser() {
        return user_id;
    }

    public void setUser(String user_id) {
        this.user_id = user_id;
    }

    public boolean isValid() {
        return this.id != null && this.value1 != null && this.value2 != null && this.value3 != null
                && this.value4 != null && this.value5 != null && this.value6 != null && this.value7 != null
                && this.value8 != null && this.value9 != null;
    }

    public void set(String fieldName, String value) {
        switch (fieldName) {
            case "id":
                this.id = value;
                break;

            default:
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
        }
    }

}
