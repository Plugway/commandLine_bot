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

    private static int getRandom(int min, int max)
    {
        return min + rnd.nextInt(max - min + 1);
    }

    public void runQuiz(IO botIO) throws IOException, InterruptedException
    {
        var questions = Question.parseQuestions(Main.QuestPath);
        var totalQuestionsAvailable = questions.size();
        var questionsAskedQuantity = 0;
        var score = 0;

        var totalQuestionsToAsk = getTotalQuestionsToAsk(totalQuestionsAvailable, botIO);

        try
        {
            while (true)
            {
                if (questionsAskedQuantity == totalQuestionsToAsk)
                    throw new QuizShouldFinishException();

                var currentQuestionNum = getNextQuestionNum(questions, totalQuestionsAvailable);
                var currentQuestion = questions.get(currentQuestionNum);

                botIO.println((questionsAskedQuantity + 1) + styleDelimeter + currentQuestion.getQuestionText(), chatId);         //печатаем вопрос
                for (var i = 0; i < currentQuestion.getAnswers().size(); i++)                                       //печатаем ответы
                    botIO.println(currentQuestion.getAnswers().get(i), chatId);

                var intInput = handleUserQuizInput(botIO);
                if (isAnswersRight(intInput, currentQuestion.getRightAnswers())) {
                    botIO.println("Верно!", chatId);
                    score++;
                } else
                    botIO.println("Неверно :(", chatId);
                currentQuestion.toggleAsked();
                questions.set(currentQuestionNum, currentQuestion);
                questionsAskedQuantity++;
            }
        } catch (QuizShouldFinishException e) {
            botIO.println("Викторина завершена.", chatId);
            botIO.println("Твой счет: " + score + " из " + questionsAskedQuantity, chatId);
        }
    }

    private int[] handleUserQuizInput(IO botIO) throws InterruptedException, IOException, QuizShouldFinishException {
        int[] intInput;
        while (true) {
            var input = botIO.readUserQuery(user);
            if (input.substring(0, 1).equals("/"))
            {
                UserCommandHandler.resolveCommand(input, user, true, botIO);
                continue;
            }
            //otherwise we are probably dealing with an answer

            try {
                intInput = getIntInputArray(input);
                return intInput;
            } catch (Exception e) {
                botIO.println("Введите числа, соответствующие ответам.\nЕсли вы считаете, что ответов несколько, то введите их через запятую.", chatId);
            }
        }
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

    private int getTotalQuestionsToAsk(int totalQuestionsAvailable, IO botIO)
    {
        botIO.println("Сколько вопросов? Общее число вопросов:" + totalQuestionsAvailable, chatId);
        int totalQuestionsToAsk=0;
        var inputIsGood = false;
        while (!inputIsGood) {
            try {
                totalQuestionsToAsk = Integer.parseInt(botIO.readUserQuery(user));
                var failNum = new String[]{
                        "Возможно, тебе не стоит учавствовать в викторине, если у тебя этот вопрос вызывает затруднения...",
                        "Число вопросов должно быть быть меньше " + totalQuestionsAvailable+" и больше 0."
                };
                if (totalQuestionsToAsk <= totalQuestionsAvailable && totalQuestionsToAsk > 0)
                {
                    inputIsGood = true;
                }
                else
                    botIO.println(failNum[getRandom(0, failNum.length-1)], chatId);
            } catch (Exception e) {
                botIO.println("Введите натуральное число больше 0 и меньше либо равно 143.", chatId);
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
