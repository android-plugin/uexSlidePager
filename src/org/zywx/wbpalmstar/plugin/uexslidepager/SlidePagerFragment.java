package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexslidepager.EUExSlidePager.OnChangeColorListener;
import org.zywx.wbpalmstar.plugin.uexslidepager.EUExSlidePager.OnStateChangeListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.zywx.wbpalmstar.base.view.BaseFragment;
import org.zywx.wbpalmstar.plugin.uexslidepager.vo.OpenOptionVO;

public class SlidePagerFragment extends BaseFragment
        implements OnPageChangeListener{
    private static final String TAG = "PluginSliderActivity";
    public static int ITEMS_DISPLAY_NUM = 7;
    private List<AppModel> mList = new ArrayList<AppModel>();
    private ViewPagerAdapter mViewPagerAdapter;
    private ScrollViewAdapter mScrollViewAdapter;
    private ScrollViewLayout mScView;
    private ViewPager mViewPager;
    private RelativeLayout mMainLinearlayout;
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
    private OnStateChangeListener mStateListener;
    private boolean isShowIcon = true;
    private boolean isShowContent = true;

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

        mMainLinearlayout = (RelativeLayout)view.findViewById(EUExUtil.getResIdID("plugin_slidepager_main"));
        initData();

        mScrollViewAdapter = new ScrollViewAdapter(this.getActivity().getApplicationContext(),
                mList, mScViewHeight);
        mScView = (ScrollViewLayout) view.findViewById(EUExUtil.getResIdID("plugin_slidepager_scrollview"));
        if (isShowIcon){
            mScView.setVisibility(View.VISIBLE);
            mScView.getLayoutParams().height = mScViewHeight;
            mScView.setScViewItemWidth(mScViewItemWidth, mScViewHeight);
            mScView.setOnScViewSelectedListener(new OnScViewSelectedListener() {
                @Override
                public void onScViewSelected(int index) {
                    if (mStateListener != null){
                        mStateListener.onIconClicked(index);
                    }
                    mViewPager.setCurrentItem(index);
                    if (!isShowContent){
                        setMainBackgroundColor(index);
                    }
                }
            });
            mScView.setScrollScStartDelayTime(VIEW_PAGER_CHANGE_DURATION,
                    SCROLL_VIEW_SLIDE_DURATION, SCROLL_VIEW_ITEM_HEIGHT_DURATION);
            mScView.setAdapter(mScrollViewAdapter);
        }else{
            mScView.setVisibility(View.GONE);
        }
        mViewPager = (ViewPager) view.findViewById(EUExUtil
                .getResIdID("plugin_slidepager_viewpager"));
        if (isShowContent){
            mViewPager.setOffscreenPageLimit(mList.size());
            mViewPagerAdapter = new ViewPagerAdapter(mFragmentActivity.getSupportFragmentManager(),
                    mList, isEncrypt);
            if (mStateListener != null){
                mViewPagerAdapter.setListener(mStateListener);
            }
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.setOnPageChangeListener(this);
            mViewPager.setVisibility(View.VISIBLE);
        }else{
            mViewPager.setVisibility(View.GONE);
        }
        onAppPagerChange(0);
        return view;
    }

    private void initData() {
        if(mData == null){
            return;
        }
        if (mData.length > 4){
            String json = mData[4];
            if (!TextUtils.isEmpty(json)){
                OpenOptionVO optionVO = DataHelper.gson.fromJson(json, OpenOptionVO.class);
                if (optionVO != null){
                    isShowIcon = optionVO.isShowIcon();
                    isShowContent = optionVO.isShowContent();
                }
            }
        }
        String content = mData[1];
        String icon = mData[2];
        String color = mData[3];
        String[] contentArray = content.split(",");
        String[] iconArray = icon.split(",");
        String[] colorArray = color.split(",");
        int count = 0;
        if (isShowContent && isShowIcon){
            count = Math.min(contentArray.length, iconArray.length);
        }else if (isShowIcon){
            count = iconArray.length;
        }else if(isShowContent){
            count = contentArray.length;
        }
        if (count == 0){return;}
        for(int i = 0; i < count; i++){
            AppModel item = new AppModel();
            if (isShowIcon){
                item.setIconUrl(iconArray[i]);
            }
            if(isShowContent){
                item.setIntroduction(Util.getRealUrlPath(mEBrw, contentArray[i]));
            }
            String colorItem;
            if (colorArray.length - 1 < i){
                colorItem = colorArray[colorArray.length -1];
            }else{
                colorItem = colorArray[i];
            }
            item.setBgColor(colorItem);
            item.setId(i);

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
        if (isShowIcon) mScView.setCurrentScrollViewItem(paramInt);
        setMainBackgroundColor(paramInt);
    }
    
    void setCurrentPage(int paramInt)
    {
        if(paramInt < 0 || paramInt >= mList.size()){
            return;
        }
        mViewPager.setCurrentItem(paramInt);
        if (isShowIcon) mScView.setCurrentScrollViewItem(paramInt);
        //setMainBackgroundColor(paramInt);
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
        this.mStateListener = listener;
    }

    public static void setOnChangeColor(
            OnChangeColorListener listener) {
        mListener = listener;
    }

    public static void setObfuscation(boolean b) {
        isEncrypt = b;
    }
}
