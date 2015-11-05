package org.zywx.wbpalmstar.plugin.uexslidepager;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RelativeLayout;

public class EUExSlidePager extends EUExBase {

    private static final String TAG = "EUExSlidePager";
    public static final String ON_FUNCTION_PAGE_CLICK = "uexSlidePager.onPageClick";
    public static final String ON_FUNCTION_CHANGE_COLOR = "uexSlidePager.onChangeColor";
    private static final String TAG_ACTIVITY = "SlidePagerFragment";
    private static boolean isOpen = false;
    private static final String BUNDLE_DATA = "data";
    private static final int MSG_OPEN_SLIDE_PAGER = 1;
    private static final int MSG_SET_CURRENT_PAGE = 2;
    private static final int MSG_CLOSE_SLIDE_PAGER = 3;
    private SlidePagerFragment slidePagerFragment;

    public EUExSlidePager(Context context, EBrowserView view) {
        super(context, view);
    }

    public void openSlidePager(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_OPEN_SLIDE_PAGER;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void openSlidePagerMsg(String[] params) {
        if(params == null || params.length < 2){
            Log.e(TAG, "openSlidePager needs 3 params!!!");
            return;
        }
        if(isOpen){
            closeSlidePager(null);
        }
        int topMargin = 0;
        try {
            if(Integer.parseInt(params[0]) > 0){
                topMargin = Integer.parseInt(params[0]);
            }
            final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-1, -1);
            lp.topMargin = topMargin;
            slidePagerFragment = new SlidePagerFragment();
            slidePagerFragment.setBaseData(params);
            slidePagerFragment.setFragmentActivity((FragmentActivity) mContext);
            boolean obf = mBrwView.getCurrentWidget().m_obfuscation == 1;
            SlidePagerFragment.setObfuscation(obf);
            SlidePagerFragment.setEBrwView(mBrwView);
            SlidePagerFragment.setOnChangeColor(new OnChangeColorListener() {

                @Override
                public void onChangeColor(String color) {
                    String js = SCRIPT_HEADER + "if(" + ON_FUNCTION_CHANGE_COLOR + "){"
                            + ON_FUNCTION_CHANGE_COLOR + "('" + color + "');}";
                    onCallback(js);
                }
            });
            addFragmentToCurrentWindow(slidePagerFragment, lp, TAG_ACTIVITY);
            isOpen = true;
            slidePagerFragment.setListener(new OnStateChangeListener(){

                @Override
                public void onPageClicked(long index) {
                    String js = SCRIPT_HEADER + "if(" + ON_FUNCTION_PAGE_CLICK + "){"
                            + ON_FUNCTION_PAGE_CLICK + "('" + index + "');}";
                    onCallback(js);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPage(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_SET_CURRENT_PAGE;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void setCurrentPageMsg(String[] params) {
        if(params == null || params.length < 1){
            Log.e(TAG, "openSlidePager needs 1 params!!!");
            return;
        }
        if (slidePagerFragment == null){
            return;
        }
        int index = Integer.parseInt(params[0]);
        slidePagerFragment.setCurrentPage(index);
    }

    public void closeSlidePager(String[] params) {
        Message msg = new Message();
        msg.obj = this;
        msg.what = MSG_CLOSE_SLIDE_PAGER;
        Bundle bd = new Bundle();
        bd.putStringArray(BUNDLE_DATA, params);
        msg.setData(bd);
        mHandler.sendMessage(msg);
    }

    private void closeSlidePagerMsg() {
        if(isOpen && slidePagerFragment != null){
            removeFragmentFromWindow(slidePagerFragment);
            slidePagerFragment = null;
            isOpen = false;
        }
    }

    @Override
    protected boolean clean() {
        return false;
    }

    public interface OnStateChangeListener{
        public void onPageClicked(long index);
    }

    public interface OnChangeColorListener{
        public void onChangeColor(String color);
    }

    @Override
    public void onHandleMessage(Message message) {
        if(message == null){
            return;
        }
        Bundle bundle=message.getData();
        switch (message.what) {

            case MSG_OPEN_SLIDE_PAGER:
                openSlidePagerMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_SET_CURRENT_PAGE:
                setCurrentPageMsg(bundle.getStringArray(BUNDLE_DATA));
                break;
            case MSG_CLOSE_SLIDE_PAGER:
                closeSlidePagerMsg();
                break;
            default:
                super.onHandleMessage(message);
        }
    }
}
