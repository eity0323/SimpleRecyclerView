package com.xdandroid.sample;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.sample.adapter.SimpleAdapter;
import com.xdandroid.sample.bean.*;
import com.xdandroid.simplerecyclerview.*;

import java.util.*;

public class SimpleFragment extends Fragment {

    private SimpleSwipeRefreshLayout mSwipeContainer;
    private SimpleRecyclerView mRecyclerView;
    private SimpleAdapter mAdapter;
    private List<SampleBean> mSampleList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSwipeContainer = (SimpleSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        setupSwipeContainer(view);
        setupRecyclerView();
        initData();
    }

    private void setupSwipeContainer(View fragmentView) {
        mSwipeContainer.setColorSchemeResources(R.color.colorAccent);
        mSwipeContainer.setOnRefreshListener(this::initData);
        //启动SwipeRefreshLayout样式下拉刷新转圈。
        //mSwipeContainer.setRefreshing(true);
        //启动自定义LoadingView布局。
        mRecyclerView.setLoadingView(fragmentView.findViewById(R.id.loading_view));
    }

    private void setupRecyclerView() {
        //自定义Divider Drawable
        //recyclerView.addItemDecoration(new SimpleRecyclerView.Divider(this, R.drawable.rv_divider, false, 0, 0, 0, 0));
        //默认Item Divider
        mRecyclerView.addItemDecoration(new Divider(getActivity(), null, false, 0, 0, 0, 0));
        mAdapter = new SimpleAdapter() {

            protected void onLoadMore(Void v) {
                new Handler().postDelayed(() -> {
                    List<SampleBean> moreSampleList = new ArrayList<>();
                    for (int i = 0; i < 26; i++) {
                        char c = (char) (65 + i);
                        moreSampleList.add(new SampleBean("Title " + String.valueOf(c), "Content " + String.valueOf(c)));
                    }
                    addAll(moreSampleList);
                }, 1777);
            }

            protected boolean hasMoreElements(Void v) {
                return list.size() <= 666;
            }
        };
        //设置加载更多的Threshold, 即离最后一个还有多少项时就开始提前加载
        mAdapter.setThreshold(7);
        //设置点击事件的监听器
        mAdapter.setOnItemClickLitener((holder, view, position, viewType) -> Toast.makeText(getActivity(), "Clicked " + position, Toast.LENGTH_SHORT)
                                                                                  .show());
        /**
         * true为使用 SwipeRefreshLayout 样式的加载更多转圈，以及设置转圈的颜色。false为使用 ProgressBar样式的加载更多转圈。
         * SwipeRefreshLayout 样式与系统版本无关。
         * ProgressBar的外观因系统版本而异，仅在 API 21 以上的 Android 系统中具有 Material Design 风格。
         */
        mAdapter.setUseMaterialProgress(true, new int[]{getResources().getColor(R.color.colorAccent)});
        //也可单独调用API设置颜色
        //adapter.setColorSchemeColors(new int[]{getResources().getColor(R.color.colorAccent)});
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        /**
         * 设置EmptyView和ErrorView.
         */
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setEmptyView(findViewById(R.id.empty_view));
                swipeRefreshLayout.setRefreshing(false);
            }
        },1777);*/
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.showErrorView(findViewById(R.id.error_view));
                //recyclerView.hideErrorView();
                swipeRefreshLayout.setRefreshing(false);
            }
        },1777);*/
    }

    private void initData() {
        new Handler().postDelayed(() -> {
            mSampleList = new ArrayList<>();
            for (int i = 0; i < 26; i++) {
                char c = (char) (65 + i);
                mSampleList.add(new SampleBean("Title " + String.valueOf(c), "Content " + String.valueOf(c)));
            }
            mAdapter.setList(mSampleList);
            mSwipeContainer.setRefreshing(false);
        }, 1777);
    }
}
