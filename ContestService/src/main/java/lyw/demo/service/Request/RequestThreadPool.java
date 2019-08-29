package lyw.demo.service.Request;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RequestThreadPool {

    private static final int RequestQueueSize = 128;
    private static final int ThreadPoolSize = 20;

    private static RequestThreadPool requestThreadPool = null;

    private ExecutorService threadPool = Executors.newFixedThreadPool(ThreadPoolSize);

    private RequestThreadPool(){
        Requestqueue requestqueue = Requestqueue.getRequestqueue();

        for(int i = 0; i < ThreadPoolSize; ++i){
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<>(RequestQueueSize);
            requestqueue.addQueue(queue);
            threadPool.submit(new ThreadTask(queue));
        }
    }

    private static RequestThreadPool getRequestThreadPool(){
        if(requestThreadPool==null){
            synchronized (RequestThreadPool.class){
                if(requestThreadPool==null){
                    requestThreadPool = new RequestThreadPool();
                }
            }
        }
        return requestThreadPool;
    }

    public static void init(){
        getRequestThreadPool();
    }
}
