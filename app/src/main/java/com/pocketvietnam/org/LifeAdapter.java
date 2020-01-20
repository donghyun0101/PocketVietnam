package com.pocketvietnam.org;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LifeAdapter extends BaseAdapter
{

    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<LifeItem> mItems = new ArrayList<>();

    @Override
    public int getCount()
    {
        return mItems.size();
    }

    @Override
    public LifeItem getItem(int position)
    {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.card_item_life, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView tv1 = convertView.findViewById(R.id.tv_life_kor);
        TextView tv2 = convertView.findViewById(R.id.tv_life_vi);
        ImageView icon = convertView.findViewById(R.id.img_life);

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        LifeItem itemList = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        tv1.setText(itemList.getTvKor());
        tv2.setText(itemList.getTvVi());
        icon.setImageDrawable(itemList.getImgLife());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String item1, String item2, Drawable icon)
    {

        LifeItem itemlist = new LifeItem();

        /* MyItem에 아이템을 setting한다. */
        itemlist.setTvKor(item1);
        itemlist.setTvVi(item2);
        itemlist.setImgLife(icon);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(itemlist);

    }
}
