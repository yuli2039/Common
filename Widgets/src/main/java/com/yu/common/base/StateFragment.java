package com.yu.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 处理了各种Fragment被销毁或者旋转屏幕造成的数据恢复的bug；<p>
 * 在onSaveState()保存数据，在onRestoreState()中取出数据
 */
public class StateFragment extends Fragment {

    private Bundle savedState;
    private String BundleKey = "StateFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }

    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    /**
     * onDestroyView和onSaveInstanceState时，均调用此方法保存状态到Arguments中
     */
    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            b.putBundle(BundleKey, savedState);
        }
    }

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        savedState = b.getBundle(BundleKey);
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    /**
     * Restore Instance State Here
     */
    private void restoreState() {
        if (savedState != null) {
            onRestoreState(savedState);
        }
    }

    /**
     * Save Instance State Here
     */
    private Bundle saveState() {
        Bundle state = new Bundle();
        onSaveState(state);
        return state;
    }

    /**
     * 子类恢复保存的数据,复写此方法
     */
    protected void onRestoreState(Bundle inState) {

    }

    /**
     * 子类保存状态时复写此方法
     */
    protected void onSaveState(Bundle outState) {

    }
}
