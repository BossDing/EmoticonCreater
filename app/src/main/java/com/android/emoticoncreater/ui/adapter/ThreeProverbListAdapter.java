package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.ThreeProverbInfo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 怼人语录列表
 */

public class ThreeProverbListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private List<ThreeProverbInfo> mDatas;
    private LayoutInflater mInflater;

    public ThreeProverbListAdapter(Context context, List<ThreeProverbInfo> datas) {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_three_proverb_list, parent, false));
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
        final ThreeProverbInfo model = mDatas.get(position);
        final String title = model.getTitle();
        final String first = model.getFirstProverb();
        final String second = model.getSecondProverb();
        final String third = model.getThirdProverb();
        final String times = "使用次数：" + model.getUseTimes();

        holder.tvTitle.setText(title);
        holder.tvFirst.setText(first);
        holder.tvSecond.setText(second);
        holder.tvThird.setText(third);
        holder.tvUsedTimes.setText(times);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mClick);

        holder.tvDelete.setTag(position);
        holder.tvDelete.setOnClickListener(mClick);
    }

    public void setListClick(IOnListClickListener listClick) {
        this.mListClick = listClick;
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvFirst;
        private TextView tvSecond;
        private TextView tvThird;
        private TextView tvUsedTimes;
        private TextView tvDelete;

        private BaseViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvFirst = itemView.findViewById(R.id.tv_first);
            tvSecond = itemView.findViewById(R.id.tv_second);
            tvThird = itemView.findViewById(R.id.tv_third);
            tvUsedTimes = itemView.findViewById(R.id.tv_used_times);
            tvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListClick != null) {
                final int position = (int) v.getTag();
                switch (v.getId()) {
                    case R.id.tv_delete:
                        mListClick.onTagClick(IOnListClickListener.ITEM_TAG0, position);
                        break;
                    default:
                        mListClick.onItemClick(position);
                        break;
                }
            }
        }
    };
}
