import org.junit.*;
import java.io.IOException;

import static org.junit.Assert.fail;

public class Tests {
    @Test
    public void questionToggleTest()
    {
        var question = new Question();
        Assert.assertFalse(question.getAsked());
        question.toggleAsked();
        Assert.assertTrue(question.getAsked());
    }

    /*
    @Test
    public void botIO_selectionTest()
    {
        BotIO.selectIOClass();
        if ( ! (Main.botMode == BotIOType.Console || Main.botMode == BotIOType.Telegram) )
            fail("Illegal botMode detected");
    }
    */

    @Test
    public void deserializationTest() throws IOException, ClassNotFoundException {
        Serialization.deserialize(Main.UsersPath);                  //failure if throws exception
    }

    @Test
    public void serializationTest() throws IOException {
        Serialization.serialize(User.userTable, Main.UsersPath);    //failure if throws exception
    }

    @Test
    public void threeSecondsTest() throws IOException, InterruptedException, ClassNotFoundException {
        var timerStart = System.currentTimeMillis();
        long timerEnd = timerStart + 3000;
        Main.main(new String[0]);
        while (System.currentTimeMillis() < timerEnd) {}            //failure if throws exception
    }

    @Test
    public void questionGenerationTest() {
        var testQuestion = Question.generateQuestion("What does the WINE acronym mean?\n" +
                "}\n" +
                "1. Wine Is Not an Emulator$\n" +
                "2. Wine Is Not Unix $\n" +
                "3. We and I made a NEw acronym$\n" +
                "}" +
                "1");

        //i begrudgingly made the isAnswersRight() method public so i can test it, but i don't feel very good about it
        Assert.assertTrue(QuizLogic.isAnswersRight(new int[] {1}, testQuestion.getRightAnswers()));
        Assert.assertFalse(QuizLogic.isAnswersRight(new int[] {2}, testQuestion.getRightAnswers()));
        Assert.assertFalse(QuizLogic.isAnswersRight(new int[] {3}, testQuestion.getRightAnswers()));
    }


}
