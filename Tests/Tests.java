import org.junit.*;
import com.pengrad.telegrambot.TelegramBot;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

public class Tests {
    @Test
    public void deserializationTest() throws DeserializationException, WrongHashException {
        UserTableSerialization.deserialize("Tests/testUsers.txt");                  //failure if throws exception
    }

    @Test
    public void hashVerificationTest() {
        Hash.verifyHashFileAgainst("Tests/testUsersHash.txt", "Tests/testUsers.txt");      //failure if throws exception
    }

    @Test
    public void correctAnswerTest() {
        int[] userInput = {1, 2};
        NumQuestion testQuestion = new NumQuestion("Is this a question?", "1. Yes$ 2. Yes", "1,2");
        Assert.assertTrue(testQuestion.assertAnswers(userInput));
    }

    @Test
    public void wrongAnswerTest() {
        int[] userInput = {1, 55};
        NumQuestion testQuestion = new NumQuestion("Is this a question?", "1. Yes$ 2. Yes$ 3. No", "1,2");
        Assert.assertFalse(testQuestion.assertAnswers(userInput));
    }

    @Test
    public void incompleteAnswerTest() {
        int[] userInput = {1};
        NumQuestion testQuestion = new NumQuestion("Is this a question?", "1. Yes$ 2. Yes", "1,2");
        Assert.assertFalse(testQuestion.assertAnswers(userInput));
    }

    @Test
    public void questionToggleTest() {
        var question = new Question();
        Assert.assertFalse(question.getAsked());
        question.toggleAsked();
        Assert.assertTrue(question.getAsked());
    }
/*
    @Test
    public void questionGenerationTest() {
        var testQuestion = Question.generateQuestion("What does the WINE acronym mean?\n" +
                "}\n" +
                "1. Wine Is Not an Emulator$\n" +
                "2. Wine Is Not Unix $\n" +
                "3. We and I made a NEw acronym$\n" +
                "}" +
                "1");

        Assert.assertTrue(Question.isAnswersRight(new int[] {1}, testQuestion.getRightAnswers()));
        Assert.assertFalse(Question.isAnswersRight(new int[] {2}, testQuestion.getRightAnswers()));
        Assert.assertFalse(Question.isAnswersRight(new int[] {3}, testQuestion.getRightAnswers()));
    }
*/
    @Test
    public void highscoreTableTest() throws IOException, ClassNotFoundException, DeserializationException, WrongHashException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        user.getStats().setHighscore(100, user, Main.botIO);
        UserTable.initializeUserTable("Tests/testUsers.txt");
        String table = Highscore.generateTable(user);
        Assert.assertEquals("Таблица рекордов:", table.substring(0, 17));
        Assert.assertTrue(table.contains("100"));
    }

    @Test
    public void preQuizCommandResolverNonFailureTest() throws IOException, ClassNotFoundException, SerializationException, InterruptedException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserCommandHandler.preQuizResolveCommand("/help", user);
    }

    @Test
    public void quizCommandResolverNonFailureTest() throws IOException, ClassNotFoundException, QuizShouldFinishException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserCommandHandler.quizResolveCommand("/help", user);
    }

    @Test
    public void duelCommandResolverNonFailureTest() throws IOException, ClassNotFoundException{
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserCommandHandler.preDuelResolveCommand("/help", user);
    }

    @Test
    public void gettingUserByTypeTest() throws IOException, ClassNotFoundException, DeserializationException, WrongHashException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserTable.initializeUserTable("Tests/testUsers.txt");
        user.getStats().setHighscore(1, user, Main.botIO);

        List<User> resultHighscore = UserTable.getUsersByType(FindTypes.highscore, "1");
        Assert.assertTrue(resultHighscore.contains(user));

        List<User> resultFirstName = UserTable.getUsersByType(FindTypes.firstname, "QWERTYQWERTYQ");
        Assert.assertTrue(resultFirstName.contains(user));

        List<User> resultUserName = UserTable.getUsersByType(FindTypes.username, "QWERTYQWERTYQ");
        Assert.assertTrue(resultUserName.contains(user));
    }
}
