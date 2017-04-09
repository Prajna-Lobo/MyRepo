package example.com.vokal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.com.vokal.R;

public class CustomAdapter extends BaseAdapter {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private Map<Integer,String> sectionHeader = new HashMap<>();
    private List<Map.Entry<String,Integer>> list = new ArrayList<>();
    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addItem(Map.Entry<String,Integer> item) {
        list.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(Integer i,String header,Map.Entry<String,Integer> item) {

        list.add(item);
        sectionHeader.put(list.indexOf(item),header);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.containsKey(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.listview_data, null);
                    holder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
                    holder.tv_occurrence = (TextView) convertView.findViewById(R.id.tv_occurrence);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.listview_header, null);
                    holder.tv_header= (TextView) convertView.findViewById(R.id.tv_header);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(rowType==TYPE_SEPARATOR){
            if(sectionHeader.get(position)!= null) {
                holder.tv_header.setText(sectionHeader.get(position) + "");
            }
        }else {
            holder.tv_word.setText(list.get(position).getKey() + "");
            holder.tv_occurrence.setText(list.get(position).getValue() + "");
        }
        return convertView;
    }
    public static class ViewHolder {
        public TextView tv_word;
        public TextView tv_occurrence;
        public TextView tv_header;
    }
}
