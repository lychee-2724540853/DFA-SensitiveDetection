import com.lychee.Filters.SensitiveWordsFilter;
import com.lychee.resourceLoader.pinyinLoader;
import org.junit.Test;

import java.util.logging.Logger;

public class myTest {

    @Test
    public void SensitiveFilterTest() throws Exception {
            SensitiveWordsFilter sensitiveWordsFilter = new SensitiveWordsFilter(3);
    }
    @Test
    public void wordsLoaderTest() throws InterruptedException {
        int num = 10;
        for (int i=0;i<num;i++)
        {
            Runnable runnable = new myRunnable(i);
            Thread thread = new Thread(runnable);
            thread.start();
            System.out.println("thread: "+i+" starting...");
        }
        Thread.sleep(3000);
    }
    @Test
    public void pinyinLoaderTest() throws InterruptedException {
        int num = 5;
        for (int i=0;i<num;i++)
        {
            Runnable runnable = new pinyinloaderRunnable(i);
            Thread thread = new Thread(runnable);
            thread.start();
            System.out.println("thread: "+i+" starting...");
        }
        Thread.sleep(3000);
    }
    public static class pinyinloaderRunnable implements Runnable
    {
        private int num;
        public pinyinloaderRunnable(int i) {
            num = i;
        }

        @Override
        public void run() {
            try {
                System.out.println("thread: " + num + " is running...");
                pinyinLoader loader = pinyinLoader.getPinyinLoader();
                System.out.println(loader.getPinyinMap().size());
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

}
