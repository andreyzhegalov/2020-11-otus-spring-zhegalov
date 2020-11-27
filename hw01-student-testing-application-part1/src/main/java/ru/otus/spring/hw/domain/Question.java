package ru.otus.spring.hw.domain;

public class Question {
    private final int number;
    private final String text;
    private final Answer answer;

    public Question(int number, String text, Answer answer) {
        this.number = number;
        this.text = text;
        this.answer = answer;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public Answer getAnswer() {
        return answer;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (int)number;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question object = (Question) o;

        if (number != object.number) return false;
        if (text != null ? !text.equals(object.text) : object.text != null) return false;
        return !(answer != null ? !answer.equals(object.answer) : object.answer != null);
    }

    @Override
    public String toString() {
        return "Question{" +
            "number = " + getNumber() +
            ", text = " + getText() +
            ", answer = " + getAnswer() +
            "}";
    }

}
