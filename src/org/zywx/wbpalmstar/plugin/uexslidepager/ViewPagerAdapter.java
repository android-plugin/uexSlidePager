package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zywx.wbpalmstar.plugin.uexslidepager.EUExSlidePager.OnStateChangeListener;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    
    private List<Fragment> mFragments;
    
    public ViewPagerAdapter(FragmentManager paramFragmentManager, List<AppModel> paramList, boolean isEncrypt) {
        super(paramFragmentManager);
        Iterator localIterator = paramList.iterator();
        if (this.mFragments == null)
            this.mFragments = new ArrayList<Fragment>();
        while (localIterator.hasNext())
        {
          AppModel localAppModel = (AppModel)localIterator.next();
          this.mFragments.add(ViewPagerFragment.getInstance(localAppModel, isEncrypt));
        }
    }
    
    @Override
    public Fragment getItem(int paramInt) {
        return (Fragment)this.mFragments.get(paramInt);
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup arg0, int arg1) {
        return super.instantiateItem(arg0, arg1);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return super.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        super.restoreState(arg0, arg1);
    }

    @Override
    public Parcelable saveState() {
        return super.saveState();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    public void setListener(OnStateChangeListener listener) {
        for (int i = 0; i < mFragments.size(); i++) {
            ((ViewPagerFragment)mFragments.get(i)).setListener(listener);  
        }
    }
}
