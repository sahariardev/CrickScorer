package com.sahariar.star.crickscorer.Others;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.sahariar.star.crickscorer.R;

/**
 * Created by STAR on 4/16/2018.
 */

public class SwipeMenuImpl implements SwipeMenuCreator {


    Context context;

    public SwipeMenuImpl(Context context)
    {
        this.context=context;
    }
    @Override
    public void create(SwipeMenu menu) {
        // create "delete" item
        SwipeMenuItem deleteItem = new SwipeMenuItem(
                context);
        // set item background
        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                0x3F, 0x25)));
        // set item width
        deleteItem.setWidth(170);
        // set a icon
        deleteItem.setIcon(R.drawable.ic_delete);
        // add to menu
        menu.addMenuItem(deleteItem);
    }
}
