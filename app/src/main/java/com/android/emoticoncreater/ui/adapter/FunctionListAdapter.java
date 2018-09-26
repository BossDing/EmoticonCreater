package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.FunctionInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FunctionListAdapter extends RecyclerView.Adapter {

    private List<FunctionInfo> mDatas;
    private LayoutInflater mInflater;
    private IOnListClickListener mListClick;

    public FunctionListAdapter(Context context, List<FunctionInfo> datas) {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_function_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindItem((BaseViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void bindItem(BaseViewHolder holder, final int position) {
        final FunctionInfo data = mDatas.get(position);
        final String name = data.getName();

        holder.btnFunction.setText(name);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mClick);

    }

    public void setListClick(IOnListClickListener listClick) {
        this.mListClick = listClick;
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        private Button btnFunction;

        private BaseViewHolder(View itemView) {
            super(itemView);
            btnFunction = itemView.findViewById(R.id.btn_function);
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListClick != null) {
                final int position = (int) v.getTag();
                mListClick.onItemClick(position);
            }
        }
    };

}
