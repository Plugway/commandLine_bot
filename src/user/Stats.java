import java.io.Serializable;

public class Stats implements Serializable {
    private int questionsCount;
    private int rightQuestionsCount;
    private int highscore;
    private int ignoredMessagesCount;
    private int duelCount;
    private int duelWinsCount;
    private int duelLostCount;
    private int quizHighscoreHitCount;

    public int getQuizHighscoreHitCount(){return quizHighscoreHitCount;}
    public int getQuestionsCount() { return questionsCount; }
    public int getRightQuestionsCount() { return rightQuestionsCount; }
    public int getHighscore(){return highscore;}
    public int getIgnoredMessagesCount(){return ignoredMessagesCount;}
    public int getDuelCount(){return duelCount;}
    public int getDuelWinsCount(){return duelWinsCount;}
    public int getDuelLostCount(){return duelLostCount;}

    public String toString()
    {
        return "Количество отвеченных вопросов - " + questionsCount +
                "\nКоличество правильных ответов в викторине - "+rightQuestionsCount+
                "\nВаш рекорд - "+highscore+
                "\nКоличество сыграных дуэлей - "+duelCount+
                "\nКоличество побед в дуэлях - "+duelWinsCount+
                "\nКоличество проигрышей в дуэлях - "+duelLostCount;
    }

    public void addQuizHighscoreHitCount(User user, IO botIO)
    {
        quizHighscoreHitCount++;
        Achievement a = null;
        switch (quizHighscoreHitCount)
        {
            case 1:
                a = new Achievement("Первый результат.", "Поставить первый рекорд.", user);
                break;
            case 2:
                a = new Achievement("Я могу лучше!","Побить свой рекорд в первый раз.", user);
                break;
            case 6:
                a = new Achievement("Как по лестнице.","Побить свой рекорд в пятый раз.", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }
    public void addQuestionsCount(User user, IO botIO)
    {
        questionsCount++;
        Achievement a = null;
        switch (questionsCount)
        {
            case 10:
                a = new Achievement("Первые шаги.","Ответить на 10 вопросов викторины.", user);
                break;
            case 100:
                a = new Achievement("Вторые шаги.","Ответить на 100 вопросов викторины.", user);
                break;
            case 1000:
                a = new Achievement("Очень упертый.","Ответить на 1000 вопросов викторины.", user);
                break;
            case 10000:
                a = new Achievement("Очень упоротый(как ты вообще здесь оказался?).","Ответить на 10000 вопросов викторины.", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }

    public void addRightQuestionsCount(User user, IO botIO)
    {
        rightQuestionsCount++;
        Achievement a = null;
        switch (rightQuestionsCount)
        {
            case 10:
                a = new Achievement("Начальные знания.","Ответить на 10 вопросов викторины правильно.", user);
                break;
            case 100:
                a = new Achievement("Достаточно умно.","Ответить на 100 вопросов викторины правильно.", user);
                break;
            case 1000:
                a = new Achievement("Гуру.","Ответить на 1000 вопросов викторины правильно.", user);
                break;
            case 10000:
                a = new Achievement("Читер?","Ответить на 10000 вопросов викторины правильно.", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }

    public void setHighscore(int highscore, User user, IO botIO)
    {
        this.highscore = highscore;
        Achievement a = null;
        if (highscore >= 10)
        {
            a = new Achievement("Начальные знания.","Поставить рекорд 10.", user);
            if (highscore >= 50)
            {
                a = new Achievement("Продвинутый мозг.","Поставить рекорд 50.", user);
                if (highscore >= 1000)
                    a = new Achievement("Есть ли предел?.","Поставить рекорд 1000.", user);
            }
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }

    public void addIgnoredMessagesCount(User user, IO botIO)
    {
        ignoredMessagesCount++;
        Achievement a = null;
        switch (ignoredMessagesCount)
        {
            case 5:
                a = new Achievement("Просто ошибка.","Озадачить бота, отправив ему сообщение без текста.", user);
                break;
            case 30:
                a = new Achievement("Чего ты добиваешься?","Продолжить отправлять неподдерживаемые сообщения.", user);
                break;
            case 500:
                a = new Achievement("Как об стену горох.","Бот проигнорил 500 сообщений от тебя. Готов ли ты дальше это делать? Есть ли ачивки дальше?", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }

    public void addDuelCount(User user, IO botIO)
    {
        duelCount++;
        Achievement a = null;
        switch (duelCount)
        {
            case 5:
                a = new Achievement("Начинающий дуэлянт.","Принять участие в дуэли 5 раз.", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }

    public void addDuelWinsCount(User user, IO botIO)
    {
        duelWinsCount++;
        Achievement a = null;
        switch (duelWinsCount)
        {
            case 5:
                a = new Achievement("Везунчик.","Одержать победу в дуэли 5 раз.", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }

    public void addDuelLostCount(User user, IO botIO)
    {
        duelLostCount++;
        Achievement a = null;
        switch (duelLostCount)
        {
            case 5:
                a = new Achievement("Просто не повезло.","Проиграть в дуэли 5 раз.", user);
                break;
            default:
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }
}
