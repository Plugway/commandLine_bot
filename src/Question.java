import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Question {
    private String mQuestionText;

    public String getQuestionText() {
        return mQuestionText;
    }

    private List<String> mAnswers;

    public List<String> getAnswers() {
        return mAnswers;
    }

    private List<Integer> mRightAnswers = new ArrayList<>();

    public List<Integer> getRightAnswers() {
        return mRightAnswers;
    }

    private boolean mAsked = false;

    public boolean getAsked() {
        return mAsked;
    }

    public void toggleAsked() {
        this.mAsked = !mAsked;
    }

    public static List<Question> questionsList;

    public static void parseQuestions(String path) throws IOException
    {
        var questions = Files.readString(Paths.get(path), StandardCharsets.UTF_8).
                replaceAll("\n", "").replaceAll("\r", "").split("]");
        var list = Stream.of(questions)
                .map(Question::generateQuestion)
                .collect(Collectors.toList());
        questionsList = list;
    }

    public static Question generateQuestion(String question) {
        var currentQuestion = new Question();
        var splitted = question.split("}");
        currentQuestion.mQuestionText = splitted[0];
        currentQuestion.mAnswers = Arrays.asList(splitted[1].split("\\$"));
        var answers = splitted[2].split(",");
        for (String answer : answers) currentQuestion.mRightAnswers.add(Integer.parseInt(answer));
        return currentQuestion;
    }

    public static boolean isAnswersRight(int[] userInput, List<Integer> rightInput) {
        var rightCounter = 0;
        if (rightInput.size() != userInput.length)
            return false;
        for (int value : userInput) {
            if (rightInput.contains(value))
                rightCounter++;
        }
        return rightCounter == userInput.length;
    }
}