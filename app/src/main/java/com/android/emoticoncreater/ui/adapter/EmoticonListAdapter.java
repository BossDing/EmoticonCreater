package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.PictureInfo;
import com.android.emoticoncreater.utils.ImageDataHelper;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;
import com.android.emoticoncreater.widget.imageloader.SquareImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 表情列表
 */

public class EmoticonListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private Context mContext;
    private List<PictureInfo> mDatas;
    private LayoutInflater mInflater;

    public EmoticonListAdapter(Context context, String title) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();

        final int[] pictures;
        if ("熊猫人".equals(title)) {
            pictures = ImageDataHelper.XIONG_MAO_REN_LIST;
        } else if ("滑稽".equals(title)) {
            pictures = ImageDataHelper.HUA_JI_LIST;
        } else if ("小坏坏".equals(title)) {
            pictures = ImageDataHelper.XIAO_HUAI_HUAI_LIST;
        } else if ("猥琐萌".equals(title)) {
            pictures = ImageDataHelper.WEI_SUO_MENG_LIST;
        } else if ("小仓鼠".equals(title)) {
            pictures = ImageDataHelper.XIAO_CANG_SHU_LIST;
        } else if ("红脸蛋".equals(title)) {
            pictures = ImageDataHelper.HONG_LIAN_DAN_LIST;
        } else {
            pictures = ImageDataHelper.MO_GU_TOU_LIST;
        }

        mDatas.clear();
        for (int picture : pictures) {
            final PictureInfo secret = new PictureInfo();
            secret.setResourceId(picture);
            mDatas.add(secret);
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_emoticon_list, parent, false));
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
        final PictureInfo model = mDatas.get(position);
        final int resourceId = model.getResourceId();

        ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, holder.ivPicture, resourceId,
                R.drawable.ic_photo, R.drawable.ic_photo);

        holder.itemView.setTag(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(mClick);

    }

    public void setListClick(IOnListClickListener listClick) {
        mListClick = listClick;
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        private SquareImageView ivPicture;

        private BaseViewHolder(View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_picture);
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListClick != null) {
                final int position = (int) v.getTag();
                final PictureInfo secret = mDatas.get(position);
                mListClick.onItemClick(v, secret);
            }
        }
    };
}
