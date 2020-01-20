package com.pocketvietnam.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
{
    private LinearLayout LayBtnTrans, LayBtnLife;
    static ProgressDialog progressTrans = null;

    //Admob
    private AdView mBannerAd;
    private InterstitialAd mInterstitialAd;

    static int BackKeyCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        initAdmob();

        LayBtnTrans = findViewById(R.id.lay_btn_trans);
        LayBtnLife = findViewById(R.id.lay_btn_life);

        LayBtnTrans.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressTrans = new ProgressDialog(MainActivity.this);
                progressTrans.setIndeterminate(true);
                progressTrans.setMessage("잠시만 기다려 주세요");
                progressTrans.show();

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        startActivity(new Intent(MainActivity.this, TransActivity.class));
                    }
                }, 100);
            }
        });

        LayBtnLife.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, LifeActivity.class));
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
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("종료하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {

                            finish();
                        }
                    })
                    .setNegativeButton("취소", null)
                    .setCancelable(false)
                    .show();
        }
    }
}
