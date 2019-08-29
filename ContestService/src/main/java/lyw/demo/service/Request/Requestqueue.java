package lyw.demo.service.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class Requestqueue {

    private static Requestqueue requestqueue = null;

    private List<ArrayBlockingQueue<Request>> queues = new ArrayList<>();

    private Requestqueue(){

    }

    public static Requestqueue getRequestqueue() {
        if(requestqueue==null){
            synchronized (Requestqueue.class){
                if(requestqueue==null) requestqueue = new Requestqueue();
            }
        }
        return requestqueue;
    }

    public void addQueue(ArrayBlockingQueue<Request> queue){
        queues.add(queue);
    }

    public ArrayBlockingQueue<Request> getQueueByIndex(int index){
        return queues.get(index);
    }

    public int getQueueSize(){
        return queues.size();
    }


}
