package com.example.mywebview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText urlInput;
    ImageView clearUrl;
    WebView webView;
    ProgressBar progressBar;
    ImageView web_back, web_forward,web_share,web_refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        urlInput=findViewById(R.id.url_input);
       progressBar= findViewById(R.id.progressbar);
        clearUrl=findViewById(R.id.clear_icon);
        webView=findViewById(R.id.web_view);
        web_back=findViewById(R.id.web_back);
        web_refresh=findViewById(R.id.web_refresh);
        web_forward=findViewById(R.id.web_forward);
        web_share=findViewById(R.id.web_share);

        WebSettings webSettings= webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webView.setWebViewClient(new MyWebViewClient());


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });


        loadMyUrl("google.com");
        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i== EditorInfo.IME_ACTION_GO || i == EditorInfo.IME_ACTION_DONE){
                InputMethodManager inm= (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inm.hideSoftInputFromWindow(urlInput.getWindowToken(),0);
                loadMyUrl(urlInput.getText().toString());
                return true;
            }
                return false;
            }
        });

        clearUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlInput.setText("");
            }
        });


      web_back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (webView.canGoBack()){
            webView.goBack();
        }
     }
    });
      web_forward.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (webView.canGoForward()){
                  webView.goForward();
              }
          }
      });

      web_refresh.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
             webView.reload();
          }
      });

      web_share.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent= new Intent(Intent.ACTION_VIEW);
              intent.setAction(Intent.ACTION_SEND);
              intent.putExtra(Intent.EXTRA_TEXT,webView.getUrl());
              intent.setType("text/plain");
              startActivity(intent);
          }
      });



    }


    void  loadMyUrl (String url){
        boolean matchUrl= Patterns.WEB_URL.matcher(url).matches();
        if (matchUrl){
            webView.loadUrl(url);


        }else {
            webView.loadUrl("google.com/search?q="+ url);
        }
    }


    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    class MyWebViewClient extends WebViewClient {
         @Override
         public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
             return false;
         }

         @Override
         public void onPageStarted(WebView view, String url, Bitmap favicon) {

             super.onPageStarted(view, url, favicon);

             urlInput.setText(webView.getUrl());
             progressBar.setVisibility(View.VISIBLE);
         }

         @Override
         public void onPageFinished(WebView view, String url) {
             super.onPageFinished(view, url);
             progressBar.setVisibility(View.INVISIBLE);
         }
     }
}