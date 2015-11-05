package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.util.ArrayList;
import java.util.List;

import org.zywx.wbpalmstar.plugin.uexslidepager.SlidePagerFragment.OnScViewSelectedListener;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class ScrollViewLayout extends HorizontalScrollView {

    private int scrollHorizontalDelayTime = 0;
    private int scrollVerticalDelayTime = 0;
    private int tmpHeightDelayTime = 0;
    private int currentIndex = -1;
    private LinearLayout mLinearLayout;
    public static int ITEMS_DISPLAY_NUM = 7;
    private int mScViewItemWidth;
    private OnScViewSelectedListener mSelectListener;
    private int currentMoveItemPosition = -1;
    private int mMarginTop = 20;
    private int mScViewHeight;
    
    
    public void setScViewItemWidth(int mScViewItemWidth, int height) {
        this.mScViewItemWidth = mScViewItemWidth;
        this.mScViewHeight = height;
    }


    private ScrollViewAdapter mScrollViewAdapter;
    private Context mContext;
    private int mScViewItemHeight;
    
    public ScrollViewLayout(Context context) {
        super(context);
        this.mContext = context;
    }
    
    public ScrollViewLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public ScrollViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @SuppressLint("NewApi")
    public void setCurrentScrollViewItem(int paramInt) {
        if(paramInt == currentIndex) return;
        Animator localAnimator1 = null;
        Animator localAnimator2 = null;
        Animator localAnimator3 = null;
        AnimatorSet localAnimatorSet1 = null;
        Animator[] arrayOfAnimator2 = null;
        if ((this.currentIndex < 0) || (getAllViewsCount() <= 7) || (paramInt <= 3))
        {
          localAnimator1 = scrollToPosition(0, this.scrollHorizontalDelayTime, false);
        }else if (getAllViewsCount() - paramInt <= 3){
          localAnimator1 = scrollToPosition(-7 + getAllViewsCount(), this.scrollHorizontalDelayTime, false);
        }else{
          localAnimator1 = scrollToPosition(paramInt - 3, this.scrollHorizontalDelayTime, false);
        }
        
        localAnimator2 = bounceUpScItem(paramInt, false);
        localAnimator3 = shootDownScItem(this.currentIndex, false);
        localAnimatorSet1 = new AnimatorSet();
        if ((localAnimator3 != null) && (localAnimator2 != null)){
            arrayOfAnimator2 = new Animator[2];
            arrayOfAnimator2[0] = localAnimator2;
            arrayOfAnimator2[1] = localAnimator3;
            localAnimatorSet1.playTogether(arrayOfAnimator2);
        }else if(localAnimator2 != null){
            arrayOfAnimator2 = new Animator[1];
            arrayOfAnimator2[0] = localAnimator2;
            localAnimatorSet1.playTogether(arrayOfAnimator2);
        }
        
        AnimatorSet localAnimatorSet2 = new AnimatorSet();
        Animator[] arrayOfAnimator1 = new Animator[2];
        arrayOfAnimator1[0] = localAnimator1;
        arrayOfAnimator1[1] = localAnimatorSet1;
        localAnimatorSet2.playSequentially(arrayOfAnimator1);
        localAnimatorSet2.start();
        this.currentIndex = paramInt;
    }
    
    private List<View> getVisibleScViews()
    {
        List<View> localArrayList = new ArrayList<View>();
        if (this.mLinearLayout == null || this.mLinearLayout.getChildCount() < 7)
          return localArrayList;
        int i = getFirstVisibleItemPosition();
        for (int k = i; k < i+7; k++)
            localArrayList.add(this.mLinearLayout.getChildAt(k));
        return localArrayList;
    }
    
    @SuppressLint("NewApi")
    public int getFirstVisibleItemPosition(){
      int start = 0;
      if (this.mLinearLayout == null) return 0;
      int i = this.mLinearLayout.getChildCount();
      for (int j = 0; j < i; j++)
      {
          View localView = this.mLinearLayout.getChildAt(j);
          if (getScrollX() < localView.getX() + this.mScViewItemWidth / 2.0F){
             start = j;
             break;
          }
      }
      return start;
    }
    
    private void updateScViewItemHeight(int paramFloat) {
        List<View> localList = getVisibleScViews();
        if(paramFloat == currentMoveItemPosition) return;
        if (paramFloat < getAllViewsCount()){
            this.currentMoveItemPosition = paramFloat;
            updateScViewItemHeight(paramFloat, localList);
        }
    }

    private void updateScViewItemHeight(int paramInt, List<View> paramList) {
        if (paramInt >= paramList.size()) return;
        for (int i = 0; i < paramList.size(); i++){
            int height = (6 - Math.abs(paramInt - i)) * getUpDownHeight()/6;
            updateItemHeight((View)paramList.get(i), -height);
        }
    }

    private void updateItemHeight(View paramView, int paramInt) {
        // TODO Auto-generated method stub
        if (paramView != null)
            AnimatorUtils.showUpAndDownBounce(paramView, paramInt, tmpHeightDelayTime, true, true);
    }

    @SuppressLint("NewApi")
    private Animator scrollToPosition(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      return smoothScrollX((int)this.mLinearLayout.getChildAt(paramInt1).getX(), 300, paramInt2, paramBoolean);
    }
    
    @SuppressLint("NewApi")
    public Animator scrollToPosition(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    {
      return smoothScrollX((int)this.mLinearLayout.getChildAt(paramInt1).getX(), paramInt2, paramInt3, paramBoolean);
    }
    
    public Animator bounceUpScItem(int paramInt, boolean paramBoolean)
    {
      if (paramInt < 0) return null;
      Animator localAnimator = bounceUpScItem(this.mLinearLayout.getChildAt(paramInt), paramBoolean);  
      return localAnimator;
    }
    
    public Animator bounceUpScItem(View paramView, boolean paramBoolean)
    {
      if (paramView == null) return null;
      Animator localAnimator = AnimatorUtils.showUpAndDownBounce(paramView, -getUpDownHeight(), scrollVerticalDelayTime, paramBoolean, true);
      return localAnimator;
    }
    
    public Animator shootDownScItem(int paramInt, boolean paramBoolean)
    {
      if ((paramInt >= 0) && (this.mLinearLayout != null) && (this.mLinearLayout.getChildCount() > paramInt)){
          Animator localAnimator = shootDownScItem(this.mLinearLayout.getChildAt(paramInt), paramBoolean);  
          return localAnimator;
      }else{
          return null;
      }
    }

    public Animator shootDownScItem(View paramView, boolean paramBoolean)
    {
      if (paramView == null) return null;
      Animator localAnimator = AnimatorUtils.showUpAndDownBounce(paramView, 0, scrollVerticalDelayTime, paramBoolean, true);
      return localAnimator;
    }
    
    private Animator smoothScrollX(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
    {
      return AnimatorUtils.moveScrollViewToX(this, paramInt1, paramInt2, paramInt3, paramBoolean);
    }
    
    private int getAllViewsCount(){
        return this.mLinearLayout.getChildCount();
    }

    public void setAdapter(ScrollViewAdapter adapter) {
        this.mScrollViewAdapter = adapter;
        initView();
    }

    private void initView() {
        this.mLinearLayout = new LinearLayout(mContext);
        this.mScViewItemHeight = 2 * mScViewHeight;
        addView(mLinearLayout);
        if(mLinearLayout.getChildCount() != 0){
            mLinearLayout.removeAllViews();
        }
        for(int i = 0; i < this.mScrollViewAdapter.getCount(); i++){
            View v = mScrollViewAdapter.getView(i, null, null);
            mLinearLayout.addView(v);
            mLinearLayout.getChildAt(i).getLayoutParams().width = mScViewItemWidth;
            mLinearLayout.getChildAt(i).getLayoutParams().height = mScViewItemHeight;
        }
    }

    public void setOnScViewSelectedListener(
            OnScViewSelectedListener onScViewSelectedListener) {
        this.mSelectListener = onScViewSelectedListener;
    }
    
    private int getUpDownHeight(){
        return mScViewItemHeight - mScrollViewAdapter.mIconHeight - mMarginTop;
    }

    public void setScrollScStartDelayTime(int i, int j, int k) {
        this.scrollHorizontalDelayTime = i;
        this.scrollVerticalDelayTime = j;
        this.tmpHeightDelayTime = k;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int offset = (int)ev.getX()/mScViewItemWidth;
        int index = getFirstVisibleItemPosition() + offset;
        switch (action) {
        case MotionEvent.ACTION_UP:
            if(index < getAllViewsCount()){
                setCurrentScrollViewItem(index);
                if(mSelectListener != null)
                    mSelectListener.onScViewSelected(index);
            }
            for(int i = 0; i < getAllViewsCount(); i++){
                if(i != index){
                    updateItemHeight(mLinearLayout.getChildAt(i), 0);
                }
            }
            this.currentMoveItemPosition = -1;
            break;
        case MotionEvent.ACTION_DOWN:
            updateScViewItemHeight(offset);
            break;
        case MotionEvent.ACTION_MOVE:
            updateScViewItemHeight(offset);
            break;
        default:
            break;
        }
        postInvalidate();
        return true;
    }
    
}
