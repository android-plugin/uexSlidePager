package org.zywx.wbpalmstar.plugin.uexslidepager;

import org.zywx.wbpalmstar.acedes.ACEDes;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexslidepager.EUExSlidePager.OnStateChangeListener;

import android.R.color;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class ViewPagerFragment extends BaseFragment 
        implements OnClickListener{
    
    private int mTouchSlop;
    protected AppModel appModel;
    private static final String APP_MODEL = "AppModel";
    private WebView mConWV;
    private OnStateChangeListener mListener;
    private boolean isVisible = false;
    private static boolean mIsEncrypt;
    
    public static ViewPagerFragment getInstance(AppModel paramAppModel, boolean isEncrypt)
    {
      ViewPagerFragment localCardFragment = new ViewPagerFragment();
      Bundle localBundle = new Bundle();
      localBundle.putSerializable("AppModel", paramAppModel);
      localCardFragment.setArguments(localBundle);
      mIsEncrypt = isEncrypt;
      return localCardFragment;
    }

    @Override
    protected void initActions(View paramView) {
        
    }

    @Override
    protected void initData() {
        this.appModel = ((AppModel)getArguments().getSerializable(APP_MODEL));
    }

    @Override
    protected View initViews(LayoutInflater paramLayoutInflater) {
        View localView = paramLayoutInflater.inflate(EUExUtil.getResLayoutID("plugin_slidepager_viewpager_item"), null);
        mConWV = (WebView) localView.findViewById(EUExUtil.getResIdID("plugin_slidepager_viewpager_item_content"));
        Button btn = (Button) localView.findViewById(EUExUtil.getResIdID("plugin_slidepager_viewpager_item_btn"));
        btn.setOnClickListener(this);
        String url = appModel.getIntroduction();
        WebSettings ws = mConWV.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setAppCachePath(getActivity().getDir("cache", 0).getPath());
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setLoadWithOverviewMode(false);
        ws.setDatabasePath(getActivity().getDir("database", 0).getPath());
        ws.setDefaultTextEncodingName("UTF-8");
        if(mIsEncrypt){
            try {
                String data = ACEDes.decrypt(url, this.getActivity(), false, null);
                mConWV.loadDataWithBaseURL(url, data, "text/html", "utf-8", url);
            } catch (Exception e) {
                e.printStackTrace();
                mConWV.loadUrl(url);
            }
        }else{
            mConWV.loadUrl(url);
        }
        mConWV.setWebChromeClient(new MyChromeClient());
        mConWV.setBackgroundColor(color.transparent);
        return localView;
    }
    
    private boolean isScrolling(Point paramPoint1, Point paramPoint2)
    {
      if ((Math.abs(paramPoint1.x - paramPoint2.x) > this.mTouchSlop) || (Math.abs(paramPoint1.y - paramPoint2.y) > this.mTouchSlop));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    @Override
    public void onClick(View v) {
            mListener.onPageClicked(appModel.getId());
    }

    public void setListener(OnStateChangeListener listener) {
        this.mListener = listener;
    }

    public class MyChromeClient extends WebChromeClient {
        public boolean onJsAlert(WebView view, String url, String message,
                final JsResult result) {
            if (!isVisible){
                result.cancel();
                return true;
            }
            AlertDialog.Builder dia = new AlertDialog.Builder(view.getContext());
            dia.setTitle("提示消息");
            dia.setMessage(message);
            dia.setCancelable(false);
            dia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            dia.create();
            dia.show();
            return true;
        }
    }

    @Override
    public void onResume() {
        isVisible = true;
        super.onResume();
    }

    @Override
    public void onStop() {
        isVisible = false;
        super.onStop();
    }

}