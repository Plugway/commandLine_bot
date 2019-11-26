import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizLogic {
    public static final String styleDelimiter = ")";
    public static List<Question> questionsList;

    private static Random rnd = new Random(System.nanoTime());
    private static int getRandom(int min, int max) {
        return min + rnd.nextInt(max - min + 1);
    }

    public static void parseQuestions(String questPath) throws IOException {
        questionsList = Question.parseQuestions(questPath);
    }
    public void runQuiz(User user, IO botIO) throws IOException, InterruptedException, SerializationException {
        var questions = new ArrayList<>(questionsList);
        Collections.shuffle(questions, rnd);

        var currentQuestionNumber = 0;
        var totalQuestionsAvailable = questions.size();
        var questionsAskedQuantity = 0;
        var score = 0;

        botIO.println("Сколько вопросов?", user.getChatId());
        var totalQuestionsToAsk = getTotalQuestionsToAsk(user, totalQuestionsAvailable, botIO);

        try {
            for (currentQuestionNumber = 0; currentQuestionNumber < totalQuestionsToAsk; ++currentQuestionNumber) {
                var currentQuestion = questions.get(currentQuestionNumber);

                botIO.println((currentQuestionNumber + 1) + styleDelimiter + currentQuestion.getQuestionText(), user.getChatId());         //печатаем вопрос
                for (var i = 0; i < currentQuestion.getAnswers().size(); i++)                                       //печатаем ответы
                    botIO.println(currentQuestion.getAnswers().get(i), user.getChatId());

                var intInput = handleUserQuizInput(user, botIO);
                if (Question.isAnswersRight(intInput, currentQuestion.getRightAnswers())) {
                    botIO.println("Верно!", user.getChatId());
                    score++;
                } else
                    botIO.println("Неверно :(", user.getChatId());

                questionsAskedQuantity++;

                if (currentQuestionNumber == totalQuestionsToAsk - 1)
                    throw new QuizShouldFinishException();
            }
        } catch (QuizShouldFinishException e) {
            botIO.println("Викторина завершена.", user.getChatId());
            botIO.println("Твой счет: " + score + " из " + questionsAskedQuantity, user.getChatId());
            //Highscore.checkScore(user, botIO, score);
            if (user.getHighscore() < score) {
                botIO.println("Новый рекорд\uD83C\uDF89", user.getChatId());
                user.setHighscore(score);
                UserTableSerialization.serialize(UserTable.get(), Main.UsersPath);
                Hash.writeHashOfFileToFile(Main.UsersPath, Main.UsersHashPath);
            }
        }
    }

    private int[] handleUserQuizInput(User user, IO botIO) throws InterruptedException, IOException, QuizShouldFinishException {
        int[] intInput;
        while (true) {
            var input = botIO.readUserQuery(user);
            if (input.substring(0, 1).equals("/")) {
                UserCommandHandler.quizResolveCommand(input, user);
                continue;
            }
            try {
                intInput = getIntInputArray(input);
                return intInput;
            } catch (Exception e) {
                botIO.println("Введите числа, соответствующие ответам.\nЕсли вы считаете, что ответов несколько, то введите их через запятую.", user.getChatId());
            }
        }
    }

    public static int getTotalQuestionsToAsk(User user, int totalQuestionsAvailable, IO botIO) {
        botIO.println("Общее число вопросов:" + totalQuestionsAvailable, user.getChatId());
        int totalQuestionsToAsk = 0;
        var inputIsGood = false;
        while (!inputIsGood) {
            try {
                totalQuestionsToAsk = Integer.parseInt(botIO.readUserQuery(user));
                var failNum = new String[]{
                        "Возможно, тебе не стоит учавствовать в этом, если у тебя этот вопрос вызывает затруднения...",
                        "Число вопросов должно быть быть меньше " + totalQuestionsAvailable + " и больше 0."
                };
                if (totalQuestionsToAsk <= totalQuestionsAvailable && totalQuestionsToAsk > 0) {
                    inputIsGood = true;
                } else
                    botIO.println(failNum[getRandom(0, failNum.length - 1)], user.getChatId());
            } catch (Exception e) {
                botIO.println("Введите натуральное число больше 0 и меньше либо равно " + totalQuestionsAvailable + ".", user.getChatId());
            }
        }
        return totalQuestionsToAsk;
    }

    public static int[] getIntInputArray(String input) throws Exception {
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