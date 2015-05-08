package org.zywx.wbpalmstar.plugin.uexslidepager;

import java.util.List;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ScrollViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<AppModel> mList;
    private LayoutInflater inflater;
    protected int mIconHeight;
    
    public ScrollViewAdapter(Context context, List<AppModel> list, int width){
        inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mList = list;
        this.mIconHeight = (int) (1.15 * width);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public AppModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(EUExUtil.getResLayoutID("plugin_slidepager_scrollview_item"), parent);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) convertView.findViewById(EUExUtil.getResIdID("plugin_slidepager_scrollview_item_icon"));
            viewHolder.icon.getLayoutParams().height = mIconHeight;
            convertView.setTag(viewHolder);
        }
        AppModel item = getItem(position);
        updateViewWithData(convertView, item, parent);
        return convertView;
    }

    private void updateViewWithData(View convertView, AppModel item,
            ViewGroup parent) {
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.icon.setImageBitmap(Util.getImage(mContext, item.getIconUrl()));
    }

    public class ViewHolder {
        private ImageView icon;
    }
}
