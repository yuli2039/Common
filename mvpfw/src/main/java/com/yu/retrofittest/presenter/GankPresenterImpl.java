package com.yu.retrofittest.presenter;

import com.yu.retrofittest.base.BasePresenter;
import com.yu.retrofittest.entity.GankEntity;
import com.yu.retrofittest.http.ApiService;
import com.yu.retrofittest.http.RetrofitManager;
import com.yu.retrofittest.rx.ApiSubscriber;
import com.yu.retrofittest.rx.DefaultTransformer;

import java.util.List;

import rx.Subscription;
import rx.functions.Func1;

/**
 * @author yu
 *         Create on 16/10/26.
 */
public class GankPresenterImpl extends BasePresenter<IGank.GankView> implements IGank.GankPresenter {

    private ApiService service;

    public GankPresenterImpl(IGank.GankView view) {
        super(view);
        service = RetrofitManager.getInstance().createService(ApiService.class);
    }

    @Override
    public void loadData(String pageNum, String pageSize) {
        Subscription subscribe = service.gank(pageNum, pageSize)
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
