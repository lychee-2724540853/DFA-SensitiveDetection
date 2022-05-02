import com.lychee.resourceLoader.wordsLoader;

import java.util.Set;

public class myThread extends Thread {
    private int num;

    public myThread(int num) {
        this.num = num;
    }

    public void run()
    {
        System.out.println("thread: "+ num +" is running...");
        wordsLoader loader = wordsLoader.getWordsLoader();
        Set<String> dictWords = loader.getDictWords();
        System.out.println(dictWords.size());
    }
}
