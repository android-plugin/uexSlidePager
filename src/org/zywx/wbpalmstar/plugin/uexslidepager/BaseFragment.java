package org.zywx.wbpalmstar.plugin.uexslidepager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    protected Handler mHandler = new Handler();

    protected DisplayMetrics getScreenDisplayMetrics()
    {
      DisplayMetrics localDisplayMetrics = new DisplayMetrics();
      getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
      return localDisplayMetrics;
    }

    protected abstract void initActions(View paramView);

    protected abstract void initData();

    protected abstract View initViews(LayoutInflater paramLayoutInflater);

    public void onCreate(Bundle paramBundle)
    {
      super.onCreate(paramBundle);
      initData();
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
    {
      super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
      View localView = initViews(paramLayoutInflater);
      initActions(localView);
      return localView;
    }
}
