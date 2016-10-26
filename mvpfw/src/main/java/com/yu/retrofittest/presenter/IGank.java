package com.yu.retrofittest.presenter;

import com.yu.retrofittest.entity.GankEntity;
import com.yu.retrofittest.base.BaseView;

import java.util.List;

/**
 * @author yu
 *         Create on 16/10/26.
 */
public interface IGank {
    interface GankView extends BaseView {
        void loadSuccess(List<GankEntity> data);
    }

    interface GankPresenter {
        void loadData(String pageNum, String pageSize);
    }
}
