public class WordQuestion extends Question {
    public WordQuestion(String questText, String rightAnswer)
    {
        setQuestionText(questText);
        this.rightAnswer = rightAnswer;
    }
    private String rightAnswer;
    public boolean assertAnswer(String answer) {
        return rightAnswer.toLowerCase().equals(answer.toLowerCase());
    }
}