package com.suusoft.restaurantuser.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.model.ProductVersion;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoRegular;

import java.util.ArrayList;


/**
 * Created by Suusoft on 22/03/2017.
 */

public class PropertyAdapter extends BaseAdapter {
    private static final int ITEM = 1;
    private static final int DROP_DOWN = 2;
    private ArrayList<ProductVersion> listProductVersions;
    private Context context;
    private IItemChecked itemChecked;

    public interface IItemChecked {
        void onSelected(int position);
    }

    public PropertyAdapter(ArrayList<ProductVersion> listProductVersions) {
        this.listProductVersions = listProductVersions;
    }

    @Override
    public int getCount() {
        return listProductVersions.size();
    }

    @Override
    public Object getItem(int i) {
        return listProductVersions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductVersion version = listProductVersions.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
            viewHolder = new ViewHolder(convertView, DROP_DOWN);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ckbProperty.setChecked(version.isSelected());
        if (version.getPrice().equals("0")) {
            viewHolder.tvPrice.setText("");
        } else {
            viewHolder.tvPrice.setText(String.format(context.getString(R.string.format_currency), AppUtil.formatCurrency(Double.parseDouble(version.getPrice()))));
        }

        viewHolder.tvName.setText(version.getName());
        viewHolder.ckbProperty.setTag(position);
        return convertView;
    }

    private class ViewHolder implements View.OnClickListener {
        private CheckBox ckbProperty;
        private TextView tvPrice;
        private TextView tvName;

        public ViewHolder(View view, int type) {
            initUI(view, type);
        }

        private void initUI(View item, int type) {
            if (type == DROP_DOWN) {
                ckbProperty = (CheckBox) item.findViewById(R.id.ckb_property);
                tvPrice = (TextView) item.findViewById(R.id.tv_price);
                tvName = (TextView) item.findViewById(R.id.tv_name);
                tvName.setSelected(true);
                tvPrice.setSelected(true);
                item.setOnClickListener(this);
                ckbProperty.setOnClickListener(this);
            } else if (type == ITEM) {
                tvName = (TextViewLatoRegular) item.findViewById(R.id.tv_name);
                tvName.setSelected(true);
            }

        }

        @Override
        public void onClick(View view) {
            refreshStateProperty();
            int index = (Integer) ckbProperty.getTag();
            listProductVersions.get(index).setSelected(true);
            itemChecked.onSelected(index);
            if (view != ckbProperty)
                ckbProperty.setChecked(!ckbProperty.isChecked());
        }
    }

    private void refreshStateProperty() {
        for (ProductVersion version : listProductVersions) {
            version.setSelected(false);
        }
    }

    public void setOnItemChecked(IItemChecked onItemChecked) {
        this.itemChecked = onItemChecked;
    }
}
