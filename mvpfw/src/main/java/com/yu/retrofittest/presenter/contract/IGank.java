package com.yu.retrofittest.presenter.contract;

import com.yu.retrofittest.entity.GankEntity;
import com.yu.retrofittest.base.IView;

import java.util.List;

/**
 * @author yu
 *         Create on 16/10/26.
 */
public interface IGank {
    interface View extends IView {
        void loadSuccess(List<GankEntity> data);
    }

    interface Presenter {
        void loadData(String pageNum, String pageSize);
    }
}
