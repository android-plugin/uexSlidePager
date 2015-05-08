package org.zywx.wbpalmstar.plugin.uexslidepager;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class EUExSlidePager extends EUExBase {

    private static final String TAG = "EUExSlidePager";
    public static final String ON_FUNCTION_PAGE_CLICK = "uexSlidePager.onPageClick";
    public static final String ON_FUNCTION_CHANGE_COLOR = "uexSlidePager.onChangeColor";
    public static final String ON_FUNCTION_CLOSE_VIEW = "uexSlidePager.onClose";
    private static final String TAG_ACTIVITY = "SlidePagerActivity";
    private static boolean isOpen = false;
    public static LocalActivityManager mgr;
    private View myView;

    public EUExSlidePager(Context context, EBrowserView view) {
        super(context, view);
        mgr = ((ActivityGroup) mContext)
                .getLocalActivityManager();
    }
    
    public void openSlidePager(final String[] params){
        if(params == null || params.length < 2){
            Log.e(TAG, "openSlidePager needs 3 params!!!");
            return;
        }
        if(isOpen){
            close();
        }
        int topMargin = 0;
        try {
            if(Integer.parseInt(params[0]) > 0){
                topMargin = Integer.parseInt(params[0]);
            };
            final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            lp.topMargin = topMargin;
            ((ActivityGroup) mContext).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(mContext, SlidePagerActivity.class);
                    boolean obf = mBrwView.getCurrentWidget().m_obfuscation == 1 ? true : false;
                    SlidePagerActivity.setObfuscation(obf);
                    SlidePagerActivity.setEBrwView(mBrwView);
                    SlidePagerActivity.setOnChangeColor(new OnChangeColorListener() {

                        @Override
                        public void onChangeColor(String color) {
                            String js = SCRIPT_HEADER + "if(" + ON_FUNCTION_CHANGE_COLOR + "){"
                                    + ON_FUNCTION_CHANGE_COLOR + "('" + color + "');}";
                            onCallback(js);
                        }
                    });
                    intent.putExtra("data", params);
                    Window window = mgr.startActivity(TAG_ACTIVITY, intent);
                    View decorView = window.getDecorView();
                    myView = decorView;
                    addViewToCurrentWindow(decorView, lp);
                    SlidePagerActivity chatWindowActivity = ((SlidePagerActivity) window.getContext());
                    isOpen = true;
                    chatWindowActivity.setListener(new OnStateChangeListener(){

                        @Override
                        public void onPageClicked(long index) {
                            String js = SCRIPT_HEADER + "if(" + ON_FUNCTION_PAGE_CLICK + "){"
                                    + ON_FUNCTION_PAGE_CLICK + "('" + index + "');}";
                            onCallback(js);
                        }
                    });
                }
            });
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void setCurrentPage(String[] params){
        if(params == null || params.length < 1){
            Log.e(TAG, "openSlidePager needs 1 params!!!");
            return;
        }
        final int index = Integer.parseInt(params[0]);
        try {
            final ActivityGroup activityGroup = (ActivityGroup) mContext;
            activityGroup.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Activity activity = mgr.getActivity(TAG_ACTIVITY);
                    if (activity != null && activity instanceof SlidePagerActivity) {
                        SlidePagerActivity chatWindowActivity = ((SlidePagerActivity) activity);
                        chatWindowActivity.setCurrentPage(index);
                    }
                }
            });
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean clean() {
        return false;
    }

    public void closeSlidePager(String[] params){
        close();
    }

    private void close() {
        Message msg = mHandler.obtainMessage();
        msg.obj = this;
        mHandler.sendMessage(msg);
    }

    public interface OnStateChangeListener{
        public void onPageClicked(long index);
    }

    public interface OnChangeColorListener{
        public void onChangeColor(String color);
    }

    @Override
    public void onHandleMessage(Message msg) {
        if(isOpen){
            Window w = mgr.destroyActivity(TAG_ACTIVITY, true);
            if(w != null){
                View view = myView;
                removeViewFromCurrentWindow(view);
            }
            isOpen = false;
        }
    }
}
