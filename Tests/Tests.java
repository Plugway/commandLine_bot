import org.junit.*;
import java.io.IOException;
import java.util.List;

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

    @Test
    public void preQuizCommandResolverNonFailureTest() throws IOException, ClassNotFoundException, SerializationException, InterruptedException, QuizCreationException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserCommandHandler.preQuizResolveCommand("/help", user);
    }

    @Test
    public void quizCommandResolverNonFailureTest() throws IOException, ClassNotFoundException, QuizShouldFinishException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserCommandHandler.quizResolveCommand("/help", user);
    }

    @Test
    public void duelCommandResolverNonFailureTest() throws IOException, ClassNotFoundException, ExitingLobbyException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserCommandHandler.preDuelResolveCommand("/help", user);
    }
/*
    @Test
    public void gettingUserByTypeTest() throws IOException, ClassNotFoundException, DeserializationException, WrongHashException {
        User user = (User)ObjectSerialization.deserialize("Tests/testUser.txt");
        UserTable.initializeUserTable("Tests/testUsers.txt");

        List<User> resultHighscore = UserTable.getUsersByType(FindTypes.highscore, "0");
        Assert.assertTrue(resultHighscore.contains(user));

        List<User> resultFirstName = UserTable.getUsersByType(FindTypes.firstname, "QWERTYQWERTYQ");
        Assert.assertTrue(resultFirstName.contains(user));

        List<User> resultUserName = UserTable.getUsersByType(FindTypes.username, "QWERTYQWERTYQ");
        Assert.assertTrue(resultUserName.contains(user));
    }*/
}
