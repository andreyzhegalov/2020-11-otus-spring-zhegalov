package ru.otus.spring.hw.domain;

public class Answer {
    private final String text;

    public Answer(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Answer object = (Answer) o;

        return !(text != null ? !text.equals(object.text) : object.text != null);
    }

    @Override
    public String toString() {
        return "Answer{" +
            "text = " + getText() +
            "}";
    }

}
