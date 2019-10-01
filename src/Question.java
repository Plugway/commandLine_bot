import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Question {
    private String Question;
    public String getQuestion() {return Question;}

    private List<String> Answers;
    public List<String> getAnswers(){return Answers;}

    private List<Integer> RightAnswers = new ArrayList<>();
    public List<Integer> getRightAnswers(){return RightAnswers;}

    private boolean HasBeen = false;
    public boolean getHasBeen(){return HasBeen;}
    public void setHasBeen() {this.HasBeen = !HasBeen;}

    public static List<Question> parseQuestions(String path) throws IOException {
        var questions = Files.readString(Paths.get(path), StandardCharsets.UTF_8).
                replaceAll("\r\n", "").split("]");
        return generateQuestions(questions);
    }
    public static List<Question> generateQuestions(String[] questions) {
        var res = new ArrayList<Question>();
        for(var i = 0; i < questions.length; i++)
        {
            var currentQuestion = new Question();
            var a = questions[i].split("}");
            currentQuestion.Question = a[0];
            currentQuestion.Answers = new ArrayList<>(Arrays.asList(a[1].split("\\$")));
            a = a[2].split(",");
            for (var j = 0; j < a.length; j++)
                currentQuestion.RightAnswers.add(Integer.parseInt(a[j]));
            res.add(currentQuestion);
        }
        res.trimToSize();
        return res;
    }
}
