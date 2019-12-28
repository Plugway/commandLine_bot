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
    protected String mQuestionText;
    private boolean mAsked = false;
    private static List<Question> questionsList;

    public String getQuestionText()
    {
        return mQuestionText;
    }
    public boolean getAsked() {
        return mAsked;
    }
    public void toggleAsked() {
        this.mAsked = !mAsked;
    }
    public static List<Question> getQuestionsList(){return questionsList;}

    public static void parseQuestions() throws IOException
    {
        var questions = Files.readString(Paths.get(FilePaths.QuestPath), StandardCharsets.UTF_8).
                replaceAll("\n", "").split("]");
        questionsList = Stream.of(questions)
                .map(Question::generateQuestion)
                .collect(Collectors.toList());
    }

    private static Question generateQuestion(String question) {
        var splitted = question.split("}");
        Question currentQuestion;
        if (splitted[0].equals("W"))
        {
            currentQuestion = new WordQuestion(splitted[1], splitted[2]);
        }
        else if (splitted[0].equals("N"))
        {
            currentQuestion = new NumQuestion(splitted[1], splitted[2], splitted[3]);
        }
        else
            throw new Error("Questions parse error: Question: " + splitted[1]);
        return currentQuestion;
    }
}