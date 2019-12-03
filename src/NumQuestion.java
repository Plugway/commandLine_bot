import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumQuestion extends Question {
    public NumQuestion(String questText, String answers, String rightAnswers)
    {
        setQuestionText(questText);
        mAnswers = Arrays.asList(answers.split("\\$"));
        for (String answer : rightAnswers.split(",")) mRightAnswers.add(Integer.parseInt(answer));
    }
    private List<String> mAnswers;

    public List<String> getAnswers() {
        return mAnswers;
    }

    private List<Integer> mRightAnswers = new ArrayList<>();

    public boolean assertAnswers(int[] userInput) {
        var rightCounter = 0;
        if (mRightAnswers.size() != userInput.length)
            return false;
        for (int value : userInput) {
            if (mRightAnswers.contains(value))
                rightCounter++;
        }
        return rightCounter == userInput.length;
    }
}
