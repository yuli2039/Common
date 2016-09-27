package com.yu.devlibrary.event;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 简化事件订阅，用法参照EventBus
 *
 * @author yu
 */
public class EventBus {
    private static EventBus sInstance;
    private Handler mHandler;

    private static ThreadLocal<PostingThread> sPostingThread = new ThreadLocal<PostingThread>() {
        @Override
        public PostingThread get() {
            return new PostingThread();
        }
    };

    private static Map<Class, CopyOnWriteArrayList<SubscribeMethod>> mSubscribeMethodsByEventType = new HashMap<>();

    private EventBus() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static EventBus getInstance() {
        if (sInstance == null) {
            synchronized (EventBus.class) {
                if (sInstance == null) {
                    sInstance = new EventBus();
                }
            }
        }
        return sInstance;
    }

    public void register(Object subscriber) {
        Class clazz = subscriber.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        CopyOnWriteArrayList<SubscribeMethod> subscribeMethods;
        for (Method method : methods) {
            SubscribeMethod subscribeMethod;
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                ThreadMode mode = subscribe.threadMode();
                Class<?>[] paramterTypes = method.getParameterTypes();
                if (paramterTypes.length == 1) {
                    Class<?> eventType = paramterTypes[0];
                    synchronized (this) {
                        if (mSubscribeMethodsByEventType.containsKey(eventType)) {
                            subscribeMethods = mSubscribeMethodsByEventType.get(eventType);
                        } else {
                            subscribeMethods = new CopyOnWriteArrayList<>();
                            mSubscribeMethodsByEventType.put(eventType, subscribeMethods);
                        }
                    }

                    subscribeMethod = new SubscribeMethod(method, mode, subscriber);
                    subscribeMethods.add(subscribeMethod);
                }
            }
        }
    }

    public void unregister(Object subscriber) {
        Class clazz = subscriber.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if (subscribe != null) {
                Class<?>[] parametersTypes = method.getParameterTypes();
                if (parametersTypes.length == 1) {
                    synchronized (this) {
                        mSubscribeMethodsByEventType.remove(parametersTypes);
                    }
                }
            }
        }
    }

    public void post(Object eventObject) {
        PostingThread postingThread = sPostingThread.get();
        postingThread.isMainThread = Looper.getMainLooper() == Looper.myLooper();

        List<Object> eventQueue = postingThread.mEventQueue;
        eventQueue.add(eventObject);
        if (postingThread.isPosting) {
            return;
        }

        postingThread.isPosting = true;
        while (!eventQueue.isEmpty()) {
            Object eventType = eventQueue.remove(0);
            postEvent(eventType, postingThread);
        }

        postingThread.isPosting = false;
    }

    private void postEvent(final Object eventType, PostingThread postingThread) {
        CopyOnWriteArrayList<SubscribeMethod> subscribeMethods;
        synchronized (this) {
            subscribeMethods = mSubscribeMethodsByEventType.get(eventType.getClass());
        }

        for (final SubscribeMethod method : subscribeMethods) {
            if (method.threadMode == ThreadMode.UI) {
                if (postingThread.isMainThread) {
                    invokeMethod(eventType, method);
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(eventType, method);
                        }
                    });
                }
            } else {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        invokeMethod(eventType, method);
                    }
                }.start();
            }
        }
    }


    private void invokeMethod(Object eventType, SubscribeMethod method) {
        try {
            method.method.invoke(method.subscriber, eventType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}