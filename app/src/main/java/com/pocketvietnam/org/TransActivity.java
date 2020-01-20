package com.pocketvietnam.org;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class TransActivity extends AppCompatActivity
{
    //WebView
    private WebView webView;
    private WebView childView;
    private WebSettings webSettings;
    private String myURL = "";
    private String childURL = "";
    int count = 1;
    private ValueCallback mFilePathCallBack;
    private String realURL = "https://translate.google.co.kr/?hl=ko#view=home&op=translate&sl=ko&tl=vi";

    //Admob
    private AdView mBannerAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("번역하기 Phiên dịch");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);

        MainActivity.progressTrans.dismiss();

        initAdmob();
        initWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView()
    {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        webView = findViewById(R.id.webView);

        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setTextZoom(100);

        webView.setWebChromeClient(new CWSWebChromeClient());
        webView.setWebViewClient(new CWSWebViewClient());

        webView.loadUrl(realURL);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        webView.resumeTimers();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        webView.pauseTimers();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 30 && resultCode == Activity.RESULT_OK)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                mFilePathCallBack.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            else
                mFilePathCallBack.onReceiveValue(new Uri[]{data.getData()});
            mFilePathCallBack = null;
        }
    }

    private class CWSWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.startsWith("http://"))
                return false;
            else
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                if (url.startsWith("sms:"))
                {
                    Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                else if (url.startsWith("tel:"))
                {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                else if (url.startsWith("mailto:"))
                {
                    Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                else if (url.startsWith("intent:"))
                {
                    try
                    {
                        Intent i = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        Intent existPackage = getPackageManager().getLaunchIntentForPackage(i.getPackage());
                        if (existPackage != null)
                        {
                            startActivity(i);
                        }
                        else
                        {
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                            marketIntent.setData(Uri.parse("market://details?id=" + i.getPackage()));
                            startActivity(marketIntent);
                        }
                        return true;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            super.onPageStarted(view, url, favicon);


            myURL = url;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);

            myURL = url;

            if (!childURL.equals("") || !childURL.equals(null) || !childURL.isEmpty())
            {
                childURL = "";
                webView.removeView(childView);
            }

            Log.e("urllog", webView.getUrl() + "");
        }

        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl)
        {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Toast.makeText(TransActivity.this, "Loading Err! Please check your network state." + description, Toast.LENGTH_SHORT).show();
        }
    }

    private class CWSWebChromeClient extends WebChromeClient
    {
        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg)
        {
            count = 1;
            webView.removeAllViews();

            childView = new WebView(TransActivity.this);
            childView.getSettings().setJavaScriptEnabled(true);
            childView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            childView.setWebChromeClient(this);

            childView.setWebViewClient(new WebViewClient()
            {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {
                    super.onPageStarted(view, url, favicon);

                    childURL = url;

                    if (count == 1) count = 0;
                }

                @Override
                public void onPageFinished(WebView view, String url)
                {
                    super.onPageFinished(view, url);
                    count = 1;
                    webView.scrollTo(0, 0);
                }

            });

            childView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            webView.addView(childView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(childView);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onCloseWindow(WebView window)
        {
            super.onCloseWindow(window);
            webView.removeView(window);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]>
                filePathCallback, FileChooserParams fileChooserParams)
        {
            mFilePathCallBack = filePathCallback;

            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(i.CATEGORY_OPENABLE);
            i.setType("image/*");

            startActivityForResult(i, 30);
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result)
        {
            final JsResult finalRes = result;
            myURL = url;
            //AlertDialog 생성
            new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setMessage(message)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finalRes.confirm();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)
        {
            final JsResult finalRes = result;
            myURL = url;
            //AlertDialog 생성
            new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setMessage(message)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finalRes.confirm();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
        {
            final JsResult finalRes = result;
            myURL = url;
            //AlertDialog 생성
            new AlertDialog.Builder(view.getContext(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setMessage(message)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finalRes.confirm();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finalRes.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }
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

        if (myURL.equals(realURL) && (childURL.equals("") || childURL.equals(null) || childURL.isEmpty()))
        {
            finish();
        }
        else if (webView.canGoBack() && (childURL.equals("") || childURL.equals(null) || childURL.isEmpty()))
        {
            webView.goBack();
        }
        else if (!childURL.equals("") || !childURL.equals(null) || !childURL.isEmpty())
        {
            webView.removeView(childView);
            childURL = "";
            webView.reload();
        }
    }
}