import org.junit.*;

import java.util.ArrayList;

public class Tests {
    @Test
    public void question_setHasBeen_Test()
    {
        var question = new Question();
        Assert.assertFalse(question.HasBeen);
        question.setHasBeen();
        Assert.assertTrue(question.HasBeen);
    }

    @Test
    public void question_generator_Test()
    {
        var input = "1)First test question}1.First answer$2.Second answer$3.Third answer}2,3]2)Second test question}1.First answer$2.Second answer}1]".split("]");
        var questions = Question.generateQuestions(input);
        Assert.assertEquals(2, questions.size());
        var firstQuest = questions.get(0);
        Assert.assertEquals("1)First test question", firstQuest.Question);
        var fQAnswers = firstQuest.Answers;
        Assert.assertEquals("1.First answer$2.Second answer$3.Third answer", fQAnswers.get(0)+"$"+fQAnswers.get(1)+"$"+fQAnswers.get(2));
        Assert.assertEquals(2,(int) firstQuest.RightAnswers.get(0));
        Assert.assertEquals(3,(int) firstQuest.RightAnswers.get(1));

        var secondQuest = questions.get(1);
        Assert.assertEquals("2)Second test question", secondQuest.Question);
        var sQAnswers = secondQuest.Answers;
        Assert.assertEquals("1.First answer$2.Second answer", sQAnswers.get(0)+"$"+sQAnswers.get(1));
        Assert.assertEquals(1,(int) secondQuest.RightAnswers.get(0));
    }

    @Test
    public void some_Test(){} //idk что еще тут тестировать...
}
