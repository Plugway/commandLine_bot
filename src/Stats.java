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
    private int achievementsCount;

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
            case 2:
                var b = new Achievement("Я могу лучше!","Побить свой рекорд в первый раз.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
                var b = new Achievement("Первые шаги.","Ответить на 10 вопросов викторины.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
                var b = new Achievement("Начальные знания.","Ответить на 10 вопросов викторины правильно.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
        Achievement b;
        if (highscore >= 10)
        {
            b = new Achievement("Начальные знания.","Поставить рекорд больше 10 или 10.");
            if (!user.getAchievements().contains(b))
                user.addAchievement(a = b);
            if (highscore >= 25)
            {
                b = new Achievement("Продвинутый мозг.","Поставить рекорд больше 25 или 25.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
                var b = new Achievement("Просто ошибка.","Озадачить бота, отправив ему сообщение без текста.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
                var b = new Achievement("Начинающий дуэлянт.","Принять участие в дуэли 5 раз.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
                var b = new Achievement("Везунчик.","Одержать победу в дуэли 5 раз.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
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
                var b = new Achievement("Просто не повезло.","Проиграть в дуэли 5 раз.");
                if (!user.getAchievements().contains(b))
                    user.addAchievement(a = b);
                break;
        }
        if (a != null)
            botIO.println(a.newToString(), user.getChatId());
    }
}
