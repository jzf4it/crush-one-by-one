package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.OnResultCallBack;
import com.jzf.net.observer.BaseObserver;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.controller.MainController;
import com.zjrb.sjzsw.entity.GirlList;
import com.zjrb.sjzsw.utils.ActivityUtil;
import com.zjrb.sjzsw.widget.recyclerview.CommonAdapter;
import com.zjrb.sjzsw.widget.recyclerview.DividerGridItemDecoration;
import com.zjrb.sjzsw.widget.recyclerview.MultiItemTypeAdapter;
import com.zjrb.sjzsw.widget.recyclerview.base.MyViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;

    private MainController mainController;
    private List<GirlList.NewslistBean> beanList = new ArrayList<>();
    private CommonAdapter commonAdapter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        registerController(mainController = new MainController(this));
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        recyclerView = findViewById(R.id.recycle_view);
        smartRefreshLayout = findViewById(R.id.refreshLayout);

        //初始化RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setAdapter(commonAdapter = new CommonAdapter<GirlList.NewslistBean>(this, R.layout.item_main_list, beanList) {
            @Override
            protected void convert(MyViewHolder holder, GirlList.NewslistBean newslistBean, int position) {
                TextView tv = holder.getView(R.id.item_title);
                ImageView itemImg = holder.getView(R.id.item_img);

                tv.setText(newslistBean.getTitle());
                Glide.with(context).load(newslistBean.getPicUrl()).centerCrop().placeholder(R.mipmap.img_defult).into(itemImg);
            }
        });
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ActivityUtil.next(MainActivity.this, TestActivity.class);
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //初始化SmartRefreshLayout
        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.autoLoadmore();
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getGirls();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getGirls();
            }
        });
    }

    /**
     * 获取美女列表
     */
    private void getGirls() {
        mainController.getGrils("9ea08bbe593c23393780a4d5a7fa35cd", 50,
                mainController.registerObserver(new BaseObserver(new OnResultCallBack<GirlList>() {
                    @Override
                    public void onSuccess(GirlList tb) {
                        beanList.addAll(tb.getNewslist());
                        commonAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onComplete() {
                        smartRefreshLayout.finishRefresh();
                        smartRefreshLayout.finishLoadmore();
                    }

                    @Override
                    public void onError(ApiException.ResponeThrowable e) {
                        Log.e("onError", "" + e.getMessage());
                    }
                }))
        );
    }
}