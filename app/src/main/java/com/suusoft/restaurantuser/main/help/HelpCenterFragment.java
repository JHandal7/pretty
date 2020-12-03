package com.suusoft.restaurantuser.main.help;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseFragment;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.widgets.listview.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

public class HelpCenterFragment extends BaseFragment {

    private static final String TAG = HelpCenterFragment.class.getSimpleName();

    private AnimatedExpandableListView mAnmExpLsv;
    private ExpHelpAdapter mAnmExpAdapter;


    public static HelpCenterFragment newInstance() {

        return new HelpCenterFragment();
    }

    @Override
    protected int getLayoutInflate() {
        return R.layout.fragment_help;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initView(View view) {
        mAnmExpLsv = (AnimatedExpandableListView) view.findViewById(R.id.anmExp);

        initData();
        initControl();

    }

    @Override
    protected void getData() {

    }


    private void initData() {
        // Move indicator to right
        int width = AppUtil.getScreenWidth(getActivity());
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mAnmExpLsv.setIndicatorBounds(width - GetPixelFromDips(48), width - GetPixelFromDips(8));
        } else {
            mAnmExpLsv.setIndicatorBoundsRelative(width - GetPixelFromDips(48), width - GetPixelFromDips(8));
        }

        List<GroupItem> items = new ArrayList<>();

        // Populate our list with groups and it's children
        for (int i = 1; i <= 5; i++) {
            GroupItem item = new GroupItem();

            item.title = "How can we help " + i + "?";
            ChildItem child = new ChildItem();
            child.content = "Once way to anoucement promote a " +
                    "certain new product or special event. Once way to anoucement promote a certain new product or special event " ;
            item.items.add(child);

            items.add(item);
        }

        mAnmExpAdapter = new ExpHelpAdapter(getActivity());
        mAnmExpAdapter.setData(items);

        mAnmExpLsv.setAdapter(mAnmExpAdapter);
    }

    private int GetPixelFromDips(float pixels) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pixels * scale + 0.5f);
    }

    private void initControl() {
        mAnmExpLsv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mAnmExpLsv.isGroupExpanded(groupPosition)) {
                    mAnmExpLsv.collapseGroupWithAnimation(groupPosition);
                } else {
                    mAnmExpLsv.expandGroupWithAnimation(groupPosition);
                }

                return true;
            }

        });
    }

    /*********
     * Start of expandable
     ********/
    private class ExpHelpAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExpHelpAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_exp_help_child, parent, false);
                holder = new ChildHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.bind(item.content);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_exp_help_group, parent, false);
                holder = new GroupHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.bind(item.title);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }
    }

    // -------------- object class ----------------
    private class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<>();
    }

    private class ChildItem {
        String content;
    }

    //--------------- View holder -----------------
    private class ChildHolder {
        TextView title;

        public ChildHolder(View view){
            title = (TextView) view.findViewById(R.id.content);
        }

        public void bind(String text){
            title.setText(text);
        }
    }

    private static class GroupHolder {
        TextView title;
        public GroupHolder(View view){
            title = (TextView) view.findViewById(R.id.textTitle);
        }

        public void bind(String text){
            title.setText(text);
        }
    }
    /********* End of expandable ********/
}
