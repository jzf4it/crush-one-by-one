package com.zjrb.sjzsw.ui.fragment;import android.animation.ValueAnimator;import android.os.Bundle;import android.support.annotation.Nullable;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.LinearLayout;import android.widget.TextView;import com.zjrb.sjzsw.R;import com.zjrb.sjzsw.widget.CustomView;import butterknife.BindView;import butterknife.ButterKnife;import butterknife.OnClick;import butterknife.Unbinder;/** * 类描述： * * @author jinzifu * @Email jinzifu123@163.com * @date 2018/2/26 2045 */public class CustomViewFragment extends BaseFragment {    private static final String TAG = "CustomViewFragment";    @BindView(R.id.titleText)    TextView titleText;    @BindView(R.id.ok)    TextView ok;    Unbinder unbinder;    @BindView(R.id.customview)    CustomView customview;    @BindView(R.id.root_layout)    LinearLayout rootLayout;    @Override    protected int getLayoutId() {        return R.layout.fr_customview;    }    @Override    protected void init(@Nullable Bundle savedInstanceState) {        titleText.setText("自定义view");    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {        View rootView = super.onCreateView(inflater, container, savedInstanceState);        unbinder = ButterKnife.bind(this, rootView);        return rootView;    }    @Override    public void onDestroyView() {        super.onDestroyView();        unbinder.unbind();    }    @OnClick({R.id.leftImage, R.id.ok, R.id.customview})    public void onViewClicked(View view) {        switch (view.getId()) {            case R.id.leftImage:                getActivity().finish();                break;            case R.id.ok:                break;            case R.id.customview:                slowAnim();                break;            default:                break;        }    }    /**     * 针对view内容的渐进滑动     */    private void slowAnim() {        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {            @Override            public void onAnimationUpdate(ValueAnimator animation) {                // 根据动画执行的进度百分比，控制view内容偏移进度。                float fraction = animation.getAnimatedFraction();                int offsetX = (-1) * (int) ((float) ok.getRight() * fraction);                int offsetY = (-1) * (int) ((float) ok.getBottom() * fraction);                ok.scrollTo(offsetX, offsetY);            }        });        animator.start();    }}