import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Logic {
    private static final String styleDelimeter = ")";
    private static String helpText = "Привет, я бот, вот что я умею:\n" +
            "1. Задавать вопросики, на которые тебе нужно ответить. В вопросе может быть несколько верных отетов, которые пишутся через запятую, например, \"2,3\"\n" +
            "Чтобы начать, напиши /start\n" +
            "Для вызова помощи напиши /help\n" +
            "Чтобы выйти во время викторины напиши /exit\n";

    private static final BotType botMode = BotType.Console;
    private static IO botIO;

    private static boolean quitQuestFlag = false;

    private static Random rnd = new Random(System.currentTimeMillis());
    private static int getRandom(int min, int max)
    {
        return min + rnd.nextInt(max - min + 1);
    }

    public static void start() throws IOException {
        selectIOClass();    //выбирается в зависимости от botMode

        botIO.println(helpText);
        resolveCommand(botIO.readUserQuery().substring(1));
    }

    private static void selectIOClass()
    {
        switch (botMode)
        {
            case Console:
                botIO = new ConsoleIO();
                break;
            case Telegram:
                throw new UnsupportedOperationException("Telegram mode not implemented yet");
                //var botIO = new TelegramIO();
                //break;
            default:
                throw new IllegalStateException("Unexpected value: " + botMode);
        }
    }

    private static void runQuiz() throws IOException {
        var questions = Question.parseQuestions(Main.QuestPath);
        var totalQuestionsAvailable = questions.size();
        var questionsAskedQuantity = 0;
        var score = 0;

        var totalQuestionsToAsk = getTotalQuestionsToAsk(totalQuestionsAvailable);

        while (!quitQuestFlag)
        {
            if (questionsAskedQuantity == totalQuestionsAvailable || questionsAskedQuantity == totalQuestionsToAsk)
                break;                                              //если все вопросы были то конец

            var currentQuestionNum = getNextQuestionNum(questions, totalQuestionsAvailable);
            var currentQuestion = questions.get(currentQuestionNum);

            botIO.println((questionsAskedQuantity+1) + styleDelimeter + currentQuestion.getQuestion());         //печатаем вопрос
            for (var i = 0; i < currentQuestion.getAnswers().size(); i++)                                       //печатаем ответы
                botIO.println(currentQuestion.getAnswers().get(i));

            var intInput = handleUserQuizInput();

            if (isAnswersRight(intInput, currentQuestion.getRightAnswers()))
                score++;
            currentQuestion.setHasBeen();
            questions.set(currentQuestionNum, currentQuestion);
            questionsAskedQuantity++;
        }
        botIO.println("Твой счет: " + score + " из " + questionsAskedQuantity);
    }

    private static int[] handleUserQuizInput() throws IOException {
        int[] intInput = new int[0];

        var answerIsGiven = false;
        while (!answerIsGiven)
        {
            var input = botIO.readUserQuery();
            if (input.substring(0, 1).equals("/")) {
                resolveCommand(input.substring(1));
                continue;
            }
            try {
                intInput = getIntInputArray(input);
                answerIsGiven = true;
            } catch (Exception e) {
                botIO.println("Введите числа, соответствующие ответам, через запятую");
            }
        }
        return intInput;
    }

    private static int getNextQuestionNum(List<Question> questions, int totalQuestionsAvailable)
    {
        var nextQuestionNum = getRandom(1, totalQuestionsAvailable);
        while (questions.get(nextQuestionNum-1).getHasBeen())                       //если такой вопрос был
        {
            nextQuestionNum = ((nextQuestionNum)%totalQuestionsAvailable)+1;        //двигаемся вперед пока не найдем
        }
        return nextQuestionNum-1;
    }

    private static int getTotalQuestionsToAsk(int totalQuestionsAvailable)
    {
        botIO.println("Сколько вопросов? Общее число вопросов:" + totalQuestionsAvailable);
        int totalQuestionsToAsk=0;
        var inputIsNumber = false;
        var inputIsGood = false;
        while (!inputIsNumber & !inputIsGood) {
            try {
                totalQuestionsToAsk = Integer.parseInt(botIO.readUserQuery());
                if (totalQuestionsToAsk <= totalQuestionsAvailable)
                {
                    inputIsGood = true;
                    inputIsNumber = true;
                }
                else
                    botIO.println("Число вопросов не может быть больше " + totalQuestionsAvailable);
            } catch (Exception e) {
                botIO.println("Введите число");
            }
        }
        return totalQuestionsToAsk;
    }

    private static boolean isAnswersRight(int[] userInput, List<Integer> rightInput)
    {
        var rightCounter = 0;
        if (rightInput.size() != userInput.length)
            return false;
        for(var i = 0; i < userInput.length; i++)
        {
            if (rightInput.contains(userInput[i]))
                rightCounter++;
        }
        return rightCounter == userInput.length;
    }

    private static int[] getIntInputArray(String input)
    {
        var numinput = input.replaceAll(" ", "").split(",");
        var intInput = new int[numinput.length];
        for (var j = 0; j < numinput.length; j++)
            intInput[j] = Integer.parseInt(numinput[j]);
        return intInput;
    }

    private static void resolveCommand(String command) throws IOException {
        switch (command)
        {
            case "start":
                runQuiz();
                break;
            case "help":
                System.out.print(helpText);
                break;
            case "exit":
                quitQuestFlag = true;
                break;
            default:
                botIO.println("Я не знаю такой команды.");
        }
    }
}
