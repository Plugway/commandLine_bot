import java.io.IOException;
import java.util.List;
import java.util.Random;

public class QuizLogic
{
    public QuizLogic(User user)
    {
        this.user = user;
        chatId = user.getChatId();
    }

    private User user;
    private long chatId;
    private static final String styleDelimeter = ")";
    private static Random rnd = new Random(System.nanoTime());
    private boolean questIsQuit = false;

    private static int getRandom(int min, int max)
    {
        return min + rnd.nextInt(max - min + 1);
    }

    public void runQuiz() throws IOException, InterruptedException
    {
        var questions = Question.parseQuestions(Main.QuestPath);
        var totalQuestionsAvailable = questions.size();
        var questionsAskedQuantity = 0;
        var score = 0;

        var totalQuestionsToAsk = getTotalQuestionsToAsk(totalQuestionsAvailable);

        while (!questIsQuit)
        {
            if (questionsAskedQuantity == totalQuestionsToAsk)
                break;                                              //если все вопросы были то конец

            var currentQuestionNum = getNextQuestionNum(questions, totalQuestionsAvailable);
            var currentQuestion = questions.get(currentQuestionNum);

            BotIO.getBotIO().println((questionsAskedQuantity+1) + styleDelimeter + currentQuestion.getQuestionText(), chatId);         //печатаем вопрос
            for (var i = 0; i < currentQuestion.getAnswers().size(); i++)                                       //печатаем ответы
                BotIO.getBotIO().println(currentQuestion.getAnswers().get(i), chatId);

            var intInput = handleUserQuizInput();
            if (questIsQuit)
                break;
            if (isAnswersRight(intInput, currentQuestion.getRightAnswers()))
            {
                BotIO.getBotIO().println("Верно!", chatId);
                score++;
            }
            else
                BotIO.getBotIO().println("Неверно :(", chatId);
            currentQuestion.toggleAsked();
            questions.set(currentQuestionNum, currentQuestion);
            questionsAskedQuantity++;
        }
        BotIO.getBotIO().println("Твой счет: " + score + " из " + questionsAskedQuantity, chatId);
        questIsQuit = true;
    }

    private int[] handleUserQuizInput() throws IOException, InterruptedException {
        int[] intInput;
        while (!questIsQuit) {
            var input = BotIO.getBotIO().readUserQuery(user);
            if (input.substring(0, 1).equals("/")) {
                if (UserCommandHandler.resolveCommand(input, user, questIsQuit) == 1)       //ugly hack
                    questIsQuit = true;
                continue;
            }
            try {
                intInput = getIntInputArray(input);
                return intInput;
            } catch (Exception e) {
                BotIO.getBotIO().println("Введите числа, соответствующие ответам.\nЕсли вы считаете, что ответов несколько, то введите их через запятую.", chatId);
            }
        }
        return null;
    }

    private static int getNextQuestionNum(List<Question> questions, int totalQuestionsAvailable)
    {
        var nextQuestionNum = getRandom(1, totalQuestionsAvailable);
        while (questions.get(nextQuestionNum-1).getAsked())                       //если такой вопрос был
        {
            nextQuestionNum = ((nextQuestionNum)%totalQuestionsAvailable)+1;        //двигаемся вперед пока не найдем
        }
        return nextQuestionNum-1;
    }

    private int getTotalQuestionsToAsk(int totalQuestionsAvailable)
    {
        BotIO.getBotIO().println("Сколько вопросов? Общее число вопросов:" + totalQuestionsAvailable, chatId);
        int totalQuestionsToAsk=0;
        var inputIsGood = false;
        while (!inputIsGood) {
            try {
                totalQuestionsToAsk = Integer.parseInt(BotIO.getBotIO().readUserQuery(user));
                var failNum = new String[]{
                        "Возможно, тебе не стоит учавствовать в викторине, если у тебя этот вопрос вызывает затруднения...",
                        "Число вопросов должно быть быть меньше " + totalQuestionsAvailable+" и больше 0."
                };
                if (totalQuestionsToAsk <= totalQuestionsAvailable && totalQuestionsToAsk > 0)
                {
                    inputIsGood = true;
                }
                else
                    BotIO.getBotIO().println(failNum[getRandom(0, failNum.length-1)], chatId);
            } catch (Exception e) {
                BotIO.getBotIO().println("Введите число.", chatId);
            }
        }
        return totalQuestionsToAsk;
    }

    public static boolean isAnswersRight(int[] userInput, List<Integer> rightInput)
    {
        var rightCounter = 0;
        if (rightInput.size() != userInput.length)
            return false;
        for (int value : userInput) {
            if (rightInput.contains(value))
                rightCounter++;
        }
        return rightCounter == userInput.length;
    }

    private static int[] getIntInputArray(String input) throws Exception {
        var numinput = input.replaceAll(" ", "").split(",");
        var intInput = new int[numinput.length];
        for (String s : numinput)
            if (Integer.parseInt(s) <= 0)
                throw new Exception();
        for (var j = 0; j < numinput.length; j++)
            intInput[j] = Integer.parseInt(numinput[j]);
        return intInput;
    }

}
