package paint.net.task;

import java.util.concurrent.Callable;

public class RequestTask implements Callable<String> {
    private String key;

    public RequestTask(String key) {
        this.key = key;
    }

    @Override
    public String call() throws Exception {
        return null;
    }

}
