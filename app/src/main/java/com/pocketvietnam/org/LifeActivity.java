package com.pocketvietnam.org;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

public class LifeActivity extends AppCompatActivity
{
    private GridView lvLifeMain;

    //Admob
    private AdView mBannerAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("생활 베트남어 Cuộc sống tiếng việt");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            actionBar.setElevation(0);
        }

        initAdmob();

        lvLifeMain = findViewById(R.id.lv_life_main);

        final LifeAdapter adapter = new LifeAdapter();

        adapter.addItem("식당", "Nhà hàng", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_restaurant));
        adapter.addItem("옷가게", "cửa hàng quần áo", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_clothingstore));
        adapter.addItem("식사", "Các bữa ăn", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_repast));
        adapter.addItem("회사", "Công ty", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_company));
        adapter.addItem("숙박", "Chỗ ở", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_lodgment));
        adapter.addItem("택시", "Taxi", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_taxi));
        adapter.addItem("긴급상황", "Tình huống\nkhẩn cấp", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_help));
        adapter.addItem("공항", "Sân bay", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_airport));
        adapter.addItem("이성만남", "Khi bạn gặp\nngười khác giới", ContextCompat.getDrawable(getApplicationContext(), R.drawable.icon_dating));

        lvLifeMain.setAdapter(adapter);

        lvLifeMain.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Intent i = new Intent(LifeActivity.this, LifeValueActivity.class);
                switch (adapter.getItem(position).getTvKor())
                {
                    case "식당":
                        i.putExtra("title", "식당 Nhà hàng");
                        i.putExtra("key", 1);
                        break;
                    case "옷가게":
                        i.putExtra("title", "옷가게 cửa hàng quần áo");
                        i.putExtra("key", 2);
                        break;
                    case "식사":
                        i.putExtra("title", "식사 Các bữa ăn");
                        i.putExtra("key", 3);
                        break;
                    case "회사":
                        i.putExtra("title", "회사 Công ty");
                        i.putExtra("key", 4);
                        break;
                    case "숙박":
                        i.putExtra("title", "숙박 Chỗ ở");
                        i.putExtra("key", 5);
                        break;
                    case "택시":
                        i.putExtra("title", "택시 Taxi");
                        i.putExtra("key", 6);
                        break;
                    case "긴급상황":
                        i.putExtra("title", "긴급상황 Tình huống khẩn cấp");
                        i.putExtra("key", 7);
                        break;
                    case "공항":
                        i.putExtra("title", "공항 Sân bay");
                        i.putExtra("key", 8);
                        break;
                    case "이성만남":
                        i.putExtra("title", "이성만남 Khi bạn gặp người khác giới");
                        i.putExtra("key", 9);
                        break;
                }

                startActivity(i);
            }
        });
    }

    public void initAdmob()
    {
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        mBannerAd = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3286891742591316/2671282606");
        //Using : ca-app-pub-3286891742591316/2671282606
        //Test : ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mBannerAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
                Log.d("@@@", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d("@@@", "onAdFailedToLoad " + errorCode);
            }

            @Override
            public void onAdOpened()
            {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked()
            {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication()
            {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed()
            {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                // Code to be executed when an ad finishes loading.
                // 광고가 문제 없이 로드시 출력됩니다.
                Log.d("@@@", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                // Code to be executed when an ad request fails.
                // 광고 로드에 문제가 있을시 출력됩니다.
                Log.d("@@@", "onAdFailedToLoad " + errorCode);
            }

            @Override
            public void onAdOpened()
            {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked()
            {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication()
            {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed()
            {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        MainActivity.BackKeyCnt++;

        if (MainActivity.BackKeyCnt == 5)
        {
            MainActivity.BackKeyCnt = 0;

            if (mInterstitialAd.isLoaded())
            {
                mInterstitialAd.show();
            }
            else
            {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
        }

        else
            finish();
    }
}