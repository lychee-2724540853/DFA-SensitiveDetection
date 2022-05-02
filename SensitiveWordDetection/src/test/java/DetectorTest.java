import com.lychee.Detecotor.Detector;
import com.lychee.Detecotor.impl.simpleWordsDetector;
import com.lychee.Detecotor.impl.strictWordsDetector;
import com.lychee.Filters.SensitiveWordsFilter;
import org.junit.Test;

public class DetectorTest {
    @Test
    public void simpleModeTest()
    {
        Detector filter1 = simpleWordsDetector.getWordsDector();
        Detector filter2 = strictWordsDetector.getWordsDector();
        String text1 = "我曹，这是个什么东西？";
        String text2 = "我是代购";
        String text3 = "卧槽，吓死我了";
        String text4 = "wo是dai购";
        String text5 = "想买东西找我，我是大购";
        System.out.println("=====================普通监测模式====================");
        filter1.isSensitive(text1);
        filter1.isSensitive(text2);
        filter1.isSensitive(text3);
        filter1.isSensitive(text4);
        filter1.isSensitive(text5);
        System.out.println("=====================严格监测模式====================");
        filter2.isSensitive(text1);
        filter2.isSensitive(text2);
        filter2.isSensitive(text3);
        filter2.isSensitive(text4);
        filter2.isSensitive(text5);
    }

    @Test
    public void multiThread() throws InterruptedException {
        for (int i=0; i<10; i++)
        {
            Runnable runnable = new TestRunnable(i);
            Thread thread = new Thread(runnable);
            thread.start();
            System.out.println("thread: "+i+" starting...");
            thread.join();
        }
        Thread.sleep(10000000);
    }
    private static class TestRunnable implements Runnable
    {
        private int num;
        public TestRunnable(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            try {
                simpleModeTest();
            }
            catch (Exception e)
            {

                System.out.println("thread: "+num+" Suspending..: "+e);
            }
            finally {
                System.out.println("thread: "+num+" Stop..");
            }
        }

        public void simpleModeTest() throws Exception {
            Detector filter1 = new SensitiveWordsFilter(0);
            Detector filter2 =  new SensitiveWordsFilter(1);
            String text1 = "我曹，这是个什么东西？";
            String text2 = "我是代购";
            String text3 = "卧槽，吓死我了";
            String text4 = "wo是dai购";
            String text5 = "想买东西找我，我是大购";
            System.out.println("=====================普通监测模式====================");
            filter1.isSensitive(text1);
            filter1.isSensitive(text2);
            filter1.isSensitive(text3);
            filter1.isSensitive(text4);
            filter1.isSensitive(text5);
            System.out.println("=====================严格监测模式====================");
            filter2.isSensitive(text1);
            filter2.isSensitive(text2);
            filter2.isSensitive(text3);
            filter2.isSensitive(text4);
            filter2.isSensitive(text5);
            Thread.sleep(3000);
        }
    }
}
