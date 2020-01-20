package com.pocketvietnam.org;

import android.graphics.drawable.Drawable;

public class LifeItem
{
    String tvKor;
    String tvVi;
    Drawable imgLife;

    public void setTvKor(String item)
    {
        this.tvKor = item;
    }

    public void setTvVi(String item)
    {
        this.tvVi = item;
    }

    public void setImgLife(Drawable item)
    {
        this.imgLife = item;
    }

    public String getTvKor()
    {
        return tvKor;
    }

    public String getTvVi()
    {
        return tvVi;
    }

    public Drawable getImgLife()
    {
        return imgLife;
    }
}
