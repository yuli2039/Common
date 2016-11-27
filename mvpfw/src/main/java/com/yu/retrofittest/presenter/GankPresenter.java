package com.yu.retrofittest.presenter;

import com.yu.retrofittest.base.BasePresenter;
import com.yu.retrofittest.entity.GankEntity;
import com.yu.retrofittest.http.RetrofitManager;
import com.yu.retrofittest.presenter.contract.IGank;
import com.yu.retrofittest.rx.ApiSubscriber;
import com.yu.retrofittest.rx.DefaultTransformer;

import java.util.List;

import rx.Subscription;
import rx.functions.Func1;

/**
 * @author yu
 *         Create on 16/10/26.
 */
public class GankPresenter extends BasePresenter<IGank.View> implements IGank.Presenter {

    public GankPresenter(IGank.View view) {
        super(view);
    }

    @Override
    public void loadData(String pageNum, String pageSize) {
        Subscription subscribe = RetrofitManager.getInstance().getApiService()
                .gank(pageNum, pageSize)
                .compose(new DefaultTransformer<List<GankEntity>>())
                .filter(new Func1<List<GankEntity>, Boolean>() {
                    @Override
                    public Boolean call(List<GankEntity> gankEntities) {
                        return mView != null;
                    }
                })
                .subscribe(new ApiSubscriber<List<GankEntity>>(mView) {
                    @Override
                    public void onNext(List<GankEntity> data) {
                        mView.loadSuccess(data);
                    }
                });
        addSubscription(subscribe);
    }
}
