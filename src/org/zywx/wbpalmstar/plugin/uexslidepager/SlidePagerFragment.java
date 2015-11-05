package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexslidepager.EUExSlidePager.OnChangeColorListener;
import org.zywx.wbpalmstar.plugin.uexslidepager.EUExSlidePager.OnStateChangeListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import org.zywx.wbpalmstar.base.view.BaseFragment;

public class SlidePagerFragment extends BaseFragment
        implements OnPageChangeListener{
    private static final String TAG = "PluginSliderActivity";
    public static int ITEMS_DISPLAY_NUM = 7;
    private List<AppModel> mList = new ArrayList<AppModel>();
    private ViewPagerAdapter mViewPagerAdapter;
    private ScrollViewAdapter mScrollViewAdapter;
    private ScrollViewLayout mScView;
    private ViewPager mViewPager;
    private LinearLayout mMainLinearlayout;
    private String[] mData;
    private static EBrowserView mEBrw;
    private int mScViewItemWidth;
    private int mScViewHeight;
    private int preColor = Color.parseColor("#FFFFFF");
    private static OnChangeColorListener mListener;
    public static final int VIEW_PAGER_CHANGE_DURATION = 400;
    public static final int SCROLL_VIEW_SLIDE_DURATION = 300;
    public static final int SCROLL_VIEW_ITEM_HEIGHT_DURATION = 350;
    private static boolean isEncrypt = false;
    private FragmentActivity mFragmentActivity;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFragmentActivity(FragmentActivity activity){
        mFragmentActivity = activity;
    }

    public void setBaseData(String[] data){
        mData = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(EUExUtil.getResLayoutID("plugin_slidepager_activity_main"),
                container, false);

        mMainLinearlayout = (LinearLayout)view.findViewById(EUExUtil.getResIdID("plugin_slidepager_main"));
        initData();

        mScView = (ScrollViewLayout) view.findViewById(EUExUtil.getResIdID("plugin_slidepager_scrollview"));
        mScView.getLayoutParams().height = mScViewHeight;
        mScView.setScViewItemWidth(mScViewItemWidth, mScViewHeight);
        mScrollViewAdapter = new ScrollViewAdapter(this.getActivity().getApplicationContext(),
                mList, mScViewHeight);
        mScView.setOnScViewSelectedListener(new OnScViewSelectedListener(){

            @Override
            public void onScViewSelected(int index) {
                mViewPager.setCurrentItem(index);
            }
        });
        mScView.setScrollScStartDelayTime(VIEW_PAGER_CHANGE_DURATION,
                SCROLL_VIEW_SLIDE_DURATION, SCROLL_VIEW_ITEM_HEIGHT_DURATION);
        mScView.setAdapter(mScrollViewAdapter);
        mViewPager = (ViewPager) view.findViewById(EUExUtil
                .getResIdID("plugin_slidepager_viewpager"));
        mViewPager.setOffscreenPageLimit(mList.size());
        mViewPagerAdapter = new ViewPagerAdapter(mFragmentActivity.getSupportFragmentManager(),
                mList, isEncrypt);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        onAppPagerChange(0);
        return view;
    }

    private void initData() {
        if(mData == null){
            return;
        }
        String content = mData[1];
        String icon = mData[2];
        String color = mData[3];
        String[] contentArray = content.split(",");
        String[] iconArray = icon.split(",");
        String[] colorArray = color.split(",");
        int count = Math.max(contentArray.length, iconArray.length);
        for(int i = 0; i < count; i++){
            AppModel item = new AppModel();
            item.setBgColor(colorArray[i]);
            item.setIconUrl(iconArray[i]);
            item.setId(i);
            item.setIntroduction(Util.getRealUrlPath(mEBrw, contentArray[i]));
            mList.add(item);
        }
        
        if(mList.size() > 6){
            mScViewItemWidth = getScreenDisplayMetrics().widthPixels/ITEMS_DISPLAY_NUM;
        }else{
            mScViewItemWidth = getScreenDisplayMetrics().widthPixels/mList.size(); 
        }
        mScViewHeight = (int) (1.2*mScViewItemWidth);
    }
    
    private void setMainBackgroundColor(int i) {

//        AnimatorUtils.showBackgroundColorAnimation(mMainLinearlayout, this.preColor, 
//                Color.parseColor(mList.get(i).getBgColor()), VIEW_PAGER_CHANGE_DURATION);
        this.preColor = i;
        mMainLinearlayout.setBackgroundColor(Color.parseColor(mList.get(i).getBgColor()));
        if(mListener != null){
            mListener.onChangeColor(mList.get(i).getBgColor()); 
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int paramInt) {
        onAppPagerChange(paramInt);
    }

    private void onAppPagerChange(int paramInt)
    {
        mScView.setCurrentScrollViewItem(paramInt);
        setMainBackgroundColor(paramInt);
    }
    
    void setCurrentPage(int paramInt)
    {
        if(paramInt < 0 || paramInt >= mList.size()){
            return;
        }
        mViewPager.setCurrentItem(paramInt);
        mScView.setCurrentScrollViewItem(paramInt);
        setMainBackgroundColor(paramInt);
    }
    
    private DisplayMetrics getScreenDisplayMetrics()
    {
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
      return localDisplayMetrics;
    }
    
    public interface OnScViewSelectedListener{
        public void onScViewSelected(int index);
    }
    
    public static void setEBrwView(EBrowserView brw){
        mEBrw = brw;
    }
    
    public void setListener(OnStateChangeListener listener){
        mViewPagerAdapter.setListener(listener);
    }

    public static void setOnChangeColor(
            OnChangeColorListener listener) {
        mListener = listener;
    }

    public static void setObfuscation(boolean b) {
        isEncrypt = b;
    }
}
