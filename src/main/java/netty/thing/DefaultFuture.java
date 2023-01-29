package netty.thing;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DefaultFuture {
    Channel channel;
    Message request;
    private final Lock lock = new ReentrantLock();
    String id;
    private final Condition done = lock.newCondition();
    private static final Map<String, DefaultFuture> FUTURES = new ConcurrentHashMap<String, DefaultFuture>();

    private volatile Message response;

    private static final Map<String, Channel> CHANNELS = new ConcurrentHashMap<String, Channel>();

    public DefaultFuture(Channel channel, Message request) {
        this.channel = channel;
        this.request = request;
        this.id = request.getId();// put into waiting map.
        FUTURES.put(id, this);
        CHANNELS.put(id, channel);
    }

    private void doReceived(Message res) {
        lock.lock();
        try {
            response = res;
            done.signal();
        } finally {
            lock.unlock();
        }
    }

    public boolean isDone() {
        return response != null;
    }

    public static void received(Channel channel, Message response) {
        try {
            DefaultFuture future = FUTURES.remove(response.getId());
            if (future != null) {
                future.doReceived(response);
            } else {
                System.out.println("null future");
            }
        } finally {
            CHANNELS.remove(response.getId());
        }
    }

    public Object get(int timeout) {
        if (timeout <= 0) {
            timeout = 1000;
        }
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                while (!isDone()) {
                    done.await(timeout, TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            if (!isDone()) {
                System.out.println("Time out !!!");
                return null;
            }
        }
        return response.getRes();
    }
}
