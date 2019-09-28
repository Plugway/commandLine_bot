import org.junit.*;
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
    public void some_Test(){} //idk что еще тут тестировать...
}
