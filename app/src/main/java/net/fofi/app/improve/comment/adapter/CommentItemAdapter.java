package net.fofi.app.improve.comment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.fofi.app.R;
import net.fofi.app.improve.base.adapter.BaseRecyclerAdapter;

/**
 * Created by ZYY on 2018/2/26.
 */

public class CommentItemAdapter extends BaseRecyclerAdapter<CommentItemAdapter.CommentItem> {
    public CommentItemAdapter(Context context) {
        super(context, NEITHER);
        addItem(new CommentItem("复制", R.mipmap.ic_copy));
        addItem(new CommentItem("评论", R.mipmap.ic_comment_40_pressed));
        addItem(new CommentItem("分享", R.mipmap.ic_share_black_pressed));
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new CommentViewHolder(mInflater.inflate(R.layout.item_list_comment_item, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, CommentItemAdapter.CommentItem item, int position) {
        CommentViewHolder h = (CommentViewHolder) holder;
        h.mTextItem.setText(item.getItem());
        h.mImageItem.setImageResource(item.getIcon());
    }

    private static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageItem;
        private TextView mTextItem;

        CommentViewHolder(View itemView) {
            super(itemView);
            mTextItem = (TextView) itemView.findViewById(R.id.tv_comment);
            mImageItem = (ImageView) itemView.findViewById(R.id.iv_comment);
        }
    }

    static class CommentItem {
        private String item;
        private int icon;

        CommentItem(String item, int icon) {
            this.item = item;
            this.icon = icon;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }
}
