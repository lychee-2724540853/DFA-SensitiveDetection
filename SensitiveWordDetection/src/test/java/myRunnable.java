import com.lychee.resourceLoader.wordsLoader;

import java.util.Set;

public class myRunnable implements Runnable{

    private int num;
    public myRunnable(int i) {
        num = i;
    }

    @Override
    public void run() {
        try {
            System.out.println("thread: " + num + " is running...");
            wordsLoader loader = wordsLoader.getWordsLoader();
            Set<String> dictWords = loader.getDictWords();
            System.out.println(dictWords.size());
        }
        catch (Exception e)
        {
            System.out.println("thread: "+num+" Suspending..");
        }
        finally {
            System.out.println("thread: "+num+" Stop..");
        }
    }
}
