import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Logic {
    private static Random rnd = new Random(System.currentTimeMillis());

    private static String helpText = "Привет, я бот, вот что я умею:\n" +
            "1. Задавать вопросики, на которые тебе нужно ответить. В вопросе может быть несколько верных отетов, которые пишутся через запятую, например, \"2,3\"\n" +
            "Чтобы начать, напиши \\start\n" +
            "Для вызова помощи напиши \\help\n" +
            "Чтобы выйти во время викторины напиши \\exit\n";

    private static boolean quitQuestFlag = false;

    private static int getRandom(int min, int max)
    {
        return min + rnd.nextInt(max - min + 1);
    }

    public static void start() throws IOException {
        System.out.print(helpText);
        Scanner in = new Scanner(System.in);
        resolveCommand(in.next().substring(1));
    }

    private static void runProgram() throws IOException {
        var questions = Question.parseQuestions(Main.QuestPath);
        Scanner in = new Scanner(System.in);
        var totalQuestionsAvailable = questions.size();
        var givenQuestNum = 0;
        var score = 0;

        System.out.println("Сколько вопросов?");
        var maxQuestNum = Integer.parseInt(in.next());

        while (!quitQuestFlag)
        {
            if (givenQuestNum == totalQuestionsAvailable || givenQuestNum == maxQuestNum)                              //если все вопросы были то конец
                break;

            var currentQuestionNum = getRandom(1, totalQuestionsAvailable);
            while (questions.get(currentQuestionNum-1).HasBeen)            //если такой вопрос был
            {
                currentQuestionNum = ((currentQuestionNum)%totalQuestionsAvailable)+1;         //двигаемся вперед пока не найдем
            }
            var currentQuestion = questions.get(currentQuestionNum-1);
            System.out.print(currentQuestion.Question + "\n"); //печатаем вопрос
            for (var i = 0; i < currentQuestion.Answers.size(); i++)  //печатаем ответы
                System.out.print(currentQuestion.Answers.get(i) + "\n");
            var input = in.next();
            if (input.substring(0, 1).equals("\\"))
            {
                resolveCommand(input.substring(1));
                continue;
            }
            var numinput = input.replaceAll(" ", "").split(",");
            var intInput = new int[numinput.length];
            for (var j = 0; j < numinput.length; j++)
                intInput[j] = Integer.parseInt(numinput[j]);
            var counter = 0;
            var rightCounter = 0;
            var rightAnswers = currentQuestion.RightAnswers;
            var breakFlag = true;
            for (;counter < intInput.length; counter++)
            {
                if (intInput.length != rightAnswers.size())
                {
                    breakFlag = false;
                    break;
                }
                if (rightAnswers.contains(intInput[counter]))
                    rightCounter++;
            }
            if (counter == rightCounter && breakFlag)
                score++;
            currentQuestion.setHasBeen();
            questions.set(currentQuestionNum-1, currentQuestion);
            givenQuestNum++;
        }
        System.out.println("Твой счет: " + score + " из " + givenQuestNum);
    }

    private static void resolveCommand(String command) throws IOException {
        switch (command)
        {
            case "start":
                runProgram();
                break;
            case "help":
                System.out.print(helpText);
                break;
            case "exit":
                quitQuestFlag = true;
                break;
            default:
                System.out.print("Я не знаю такой команды.");
        }
    }
}
