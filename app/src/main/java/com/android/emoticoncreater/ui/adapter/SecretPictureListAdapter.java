package com.android.emoticoncreater.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.emoticoncreater.R;
import com.android.emoticoncreater.model.PictureInfo;
import com.android.emoticoncreater.utils.ImageDataHelper;
import com.android.emoticoncreater.widget.imageloader.ImageLoaderFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 秘密表情列表
 */

public class SecretPictureListAdapter extends RecyclerView.Adapter {

    private IOnListClickListener mListClick;
    private Context mContext;
    private List<PictureInfo> mDatas;
    private LayoutInflater mInflater;

    public SecretPictureListAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
        mInflater = LayoutInflater.from(context);

        mDatas.clear();
        for (int i = 0; i < ImageDataHelper.SECRET_LIST.length; i++) {
            final PictureInfo secret = new PictureInfo();
            secret.setResourceId(ImageDataHelper.SECRET_LIST[i]);
            secret.setTitle(ImageDataHelper.SECRET_TITLES[i]);
            mDatas.add(secret);
        }
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_secret_picture_list, parent, false));
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
        final String title = model.getTitle();

        ImageLoaderFactory.getLoader().loadImageFitCenter(mContext, holder.ivPicture, resourceId, 0, 0);
        holder.tvTitle.setText(title);

        holder.itemView.setTag(holder.getAdapterPosition());
        holder.itemView.setOnClickListener(mClick);
    }

    public void setListClick(IOnListClickListener listClick) {
        mListClick = listClick;
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView ivPicture;
        private AppCompatTextView tvTitle;

        private BaseViewHolder(View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            tvTitle = itemView.findViewById(R.id.tv_title);
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
