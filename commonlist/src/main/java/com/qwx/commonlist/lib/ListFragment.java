package com.qwx.commonlist.lib;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.qwx.basemvp.BaseMvpFragment;
import com.qwx.basemvp.BasePresenter;
import com.qwx.basemvp.UsePresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by qqin on 2018/12/20
 * <p>
 * email qqin@finbtc.net
 */
public class ListFragment<P extends BaseListPresenter> extends BaseMvpFragment implements BaseListView {
    protected RecyclerView mRv;
    protected SmartRefreshLayout refreshLayout;
    protected View mHeaderView;
    protected ListFragmentConfig mConfig;
    protected P mPresenter;

    protected FrameLayout flContent;
    protected View mEmptyView;

    protected CommonRecyclerViewAdapter commonRecyclerViewAdapter;

    public static ListFragment newInstance(ListFragmentConfig config) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", config);
        ListFragment fragment = new ListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected BasePresenter initPresenter() {
        mConfig = (ListFragmentConfig) getArguments().getSerializable("config");
        Class<P> clazz = (Class<P>) mConfig.getaClass();
        if (clazz != null) {
            try {
                Constructor constructor = clazz.getConstructor(BaseListView.class);
                mPresenter = (P) constructor.newInstance(this);
                //除了view 还有其它参数（例如网络请求的参数）
                if (mConfig.getExtraParams() != null) {
                    mPresenter.setExtraParams(mConfig.getExtraParams());
                }
            } catch (NoSuchMethodException e) {
            } catch (IllegalAccessException e) {
            } catch (java.lang.InstantiationException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
            return;
        }
        initPresenter();

        commonRecyclerViewAdapter = new CommonRecyclerViewAdapter(mConfig.getViewBeanRes(), getContext());
        if (getContext() instanceof CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner) {
            commonRecyclerViewAdapter.setOnRecyclerItemClickListner((CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner) getContext());
            ((CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner) getContext()).attachPresenter(mPresenter);
        } else if (getParentFragment() instanceof CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner) {
            commonRecyclerViewAdapter.setOnRecyclerItemClickListner((CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner) getParentFragment());
            ((CommonRecyclerViewAdapter.OnRecyclerViewItemClickListner) getParentFragment()).attachPresenter(mPresenter);
        } else if (getParentFragment() instanceof UsePresenter) {
            ((UsePresenter) getParentFragment()).attachPresenter(mPresenter);
        } else if (getContext() instanceof UsePresenter) {
            ((UsePresenter)getContext()).attachPresenter(mPresenter);
        }

        if (mPresenter != null) {
            getLifecycle().addObserver(mPresenter);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_common_list, container, false);
        flContent = view.findViewById(R.id.fl_content);
        mRv = view.findViewById(R.id.rv_list);
        mRv.setAdapter(commonRecyclerViewAdapter);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        if (mConfig.getListEmptyRes() > 0) {
            mEmptyView = inflater.inflate(mConfig.getListEmptyRes(), flContent, false);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            flContent.addView(mEmptyView, layoutParams);
            mEmptyView.setVisibility(View.GONE);
        }

        if (mRv.getLayoutManager() == null) {
            mRv.setLayoutManager(new LinearLayoutManager(getContext(), mConfig.getOrientation(), false));
        }

        initHeaderView(inflater);


        refreshLayout.setEnableLoadMore(mConfig.isSupportLoadMore());
        refreshLayout.setEnableRefresh(mConfig.isSupportRefresh());
        if (mConfig.isSupportLoadMore()) {
            ClassicsFooter footer = new ClassicsFooter(getContext());
            footer.setSpinnerStyle(SpinnerStyle.Translate);
            footer.setPrimaryColorId(R.color.white);
            footer.setAccentColorId(R.color.common_color_666666);
            refreshLayout.setRefreshFooter(footer);
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    mPresenter.loadMore();
                }
            });
        }
        if (mConfig.isSupportRefresh()) {
            refreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    mPresenter.refreshRequest();
                }
            });
        }

        if (mConfig.getDecorationConfig() != null) {
            mRv.addItemDecoration(mConfig.getDecorationConfig().createLinearItemDecoration(getContext()));
        }

        if (mConfig.isAutoSubscribe()) {
            mPresenter.subscribe();
        }

        return view;
    }


    /**
     * todo header特性不支持
     * @param inflater
     */
    private void initHeaderView(@NonNull LayoutInflater inflater) {
        if (mConfig.getListHeaderRes() > 0) {
            mHeaderView = inflater.inflate(mConfig.getListHeaderRes(), mRv, false);
            if (mHeaderView instanceof UsePresenter && mPresenter != null) {
                ((UsePresenter) mHeaderView).attachPresenter(mPresenter);
            }
//            mRv.addHeaderView(mHeaderView);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();

        if (commonRecyclerViewAdapter != null) {
            commonRecyclerViewAdapter.setOnRecyclerItemClickListner(null);
        }
    }

    @Override
    public void renderListData(List<? extends ViewBean> viewBeans, boolean isRefresh, boolean hasMore) {
        if (mConfig.isSupportRefresh() && isRefresh) {
            refreshLayout.finishRefresh();
        }
        if (mConfig.isSupportLoadMore() && !isRefresh) {
            refreshLayout.finishLoadMore();
        }
        if ((viewBeans == null || viewBeans.isEmpty()) && isRefresh) {
            showEmptyView();
            return;
        }


        refreshLayout.setNoMoreData(!hasMore);
        refreshLayout.setEnableLoadMore(hasMore);

        if (isRefresh) {
            commonRecyclerViewAdapter.updateList(viewBeans);
        } else {
            commonRecyclerViewAdapter.appendList(viewBeans);
        }
    }

    @Override
    public void renderHeaderView(ViewBean viewBean) {
        if (mHeaderView instanceof ItemView) {
            ((ItemView) mHeaderView).bindData(viewBean);
        }
    }

    @Override
    public void showEmptyView() {
        if (!mConfig.isSupportEmpty() || mEmptyView == null) {
            return;
        }
        mEmptyView.setVisibility(View.VISIBLE);
        mRv.setVisibility(View.GONE);
    }

    @Override
    public void clearList() {
        CommonRecyclerViewAdapter adapter = (CommonRecyclerViewAdapter) mRv.getAdapter();
        if (adapter != null) {
            adapter.clear();
        }
    }

}
