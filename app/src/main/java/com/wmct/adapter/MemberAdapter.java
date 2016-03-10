package com.wmct.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wmct.bean.Member;
import com.wmct.health.R;
import com.wmct.net.Image.ImageLoader;
import com.wmct.util.StringUtil;

import java.util.List;

/**
 * Created by shan on 2016/1/4.
 */
public class MemberAdapter extends BaseAdapter {
    ImageLoader imageLoader;
    private Context mcontext;
    private List<Member> mMembers;
    private LayoutInflater mInflater;
    private static final String TAG = "MemberAdapter";
    private int selectIndex = 0;

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public MemberAdapter(Context context, List<Member> members) {

        this.mcontext = context;
        this.mMembers = members;
        this.mInflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.build(mcontext);
    }

    @Override
    public int getCount() {
        return mMembers == null ? 0 : mMembers.size();
    }

    @Override
    public Member getItem(int position) {
        return mMembers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG,"getView-------------->调用"+position);
        ViewHolder mViewHolder;

        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.template_member, parent, false);
            mViewHolder.imageViewHead = (ImageView) convertView.findViewById(R.id.imageviewHead);
            mViewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        Member member = getItem(position);
        String url = member.getImage();
        if (!StringUtil.isEmpty(url)) {
            ImageView imageView = mViewHolder.imageViewHead;
            imageView.setTag(url);
            imageLoader.bindBitmap(url, imageView,imageView.getWidth(),imageView.getHeight());
        }
        mViewHolder.txtName.setText(member.getMembername());
        //    mViewHolder.imageViewHead.setImageResource(member.getImgres());
       /* if(position==0){
            convertView.setBackgroundColor(Color.parseColor("#e7e4e4"));
        }*/
        if( selectIndex == position ){
            Log.i(TAG,"点击的位置------adapter------->"+position);

            convertView.setBackgroundColor(Color.parseColor("#e7e4e4"));
        }else{
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        return convertView;
    }


    class ViewHolder {

        ImageView imageViewHead;
        TextView txtName;

    }
}
