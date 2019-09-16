import java.io.IOException;
import java.util.Random;
import java.util.Scanner;


public class Main
{
    private static String QuestPath = "C:\\Users\\EgorK\\Desktop\\test.txt";
    private static Random rnd = new Random(System.currentTimeMillis());
    public static void main(String[] args) throws IOException, InterruptedException {
        runProgram();
    }
    private static int getRandom(int min, int max)
    {
        return min + rnd.nextInt(max - min + 1);
    }
    private static void runProgram() throws IOException {
        var questions = Question.parseQuestions(QuestPath);
        Scanner in = new Scanner(System.in);
        var questNum = questions.size();
        var quitQuest = false;
        var givenQuestNum = 0;
        var score = 0;
        System.out.println("Сколько вопросов?");
        var maxQuestNum = Integer.parseInt(in.next());
        while (!quitQuest)
        {
            if (givenQuestNum == questNum || givenQuestNum == maxQuestNum)                              //если все вопросы были то конец
                break;
            var currentQuestionNum = getRandom(1, questNum);
            while (questions.get(currentQuestionNum-1).HasBeen)            //если такой вопрос был
            {
                currentQuestionNum = ((currentQuestionNum)%questNum)+1;         //двигаемся вперед пока не найдем
            }
            var currentQuestion = questions.get(currentQuestionNum-1);
            currentQuestion.HasBeen = true;
            questions.set(currentQuestionNum-1, currentQuestion);
            System.out.print(currentQuestion.Question + "\n"); //печатаем вопрос
            for (var i = 0; i < currentQuestion.Answers.size(); i++)  //печатаем ответы
                System.out.print(currentQuestion.Answers.get(i) + "\n");
            var input = in.next().replaceAll(" ", "").split(",");
            var intInput = new int[input.length];
            for (var j = 0; j < input.length; j++)
                intInput[j] = Integer.parseInt(input[j]);
            var counter = 0;
            var rightCounter = 0;
            var rightAnswers = currentQuestion.RightAnswers;
            var breakFlag = false;
            for (;counter < intInput.length; counter++)
            {
                if (intInput.length != rightAnswers.size())
                {
                    breakFlag = true;
                    break;
                }
                if (rightAnswers.contains(intInput[counter]))
                    rightCounter++;
            }
            if (counter == rightCounter && !breakFlag)
                score++;
            givenQuestNum++;
        }
        System.out.println("Твой счет " + score + " из " + givenQuestNum);
    }
}
