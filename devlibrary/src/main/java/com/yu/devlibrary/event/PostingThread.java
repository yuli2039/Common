package com.yu.devlibrary.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu
 *         Create on 2016/9/27 0027.
 */
class PostingThread {
    List<Object> mEventQueue = new ArrayList<>();
    boolean isMainThread;
    boolean isPosting;
}