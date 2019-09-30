import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Question {
    public String Question;
    public ArrayList<String> Answers;
    public ArrayList<Integer> RightAnswers = new ArrayList<>();
    public boolean HasBeen = false;

    public void setHasBeen()
    {
        this.HasBeen = !HasBeen;
    }

    public static ArrayList<Question> parseQuestions(String path) throws IOException {
        var questions = Files.readString(Paths.get(path), StandardCharsets.UTF_8).
                replaceAll("\r\n", "").split("]");
        return generateQuestions(questions);
    }
    public static ArrayList<Question> generateQuestions(String[] questions) {
        var res = new ArrayList<Question>();
        for(var i = 0; i < questions.length; i++)
        {
            var currentQuestion = new Question();
            var a = questions[i].split("}");
            currentQuestion.Question = a[0];
            currentQuestion.Answers = new ArrayList<>(Arrays.asList(a[1].split("\\$")));
            currentQuestion.Answers.trimToSize();
            a = a[2].split(",");
            for (var j = 0; j < a.length; j++)
                currentQuestion.RightAnswers.add(Integer.parseInt(a[j]));
            currentQuestion.RightAnswers.trimToSize();
            res.add(currentQuestion);
        }
        res.trimToSize();
        return res;
    }
}
