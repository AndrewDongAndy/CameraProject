package andy.cameraproject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    WebView webCamera;
    EditText editUsername;
    EditText editPassword;
    EditText editStreamURL;

    Handler timer;
    int delay;

    String webURL;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        btnStart = findViewById(R.id.btnStart);
        webCamera = findViewById(R.id.webCamera);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editStreamURL = findViewById(R.id.editStreamURL);

        // Prepare image streamer
        timer = new Handler();
        delay = 200;
        webCamera.setInitialScale(210);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                webURL = editStreamURL.getText().toString();

                webCamera.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                        super.onReceivedHttpAuthRequest(view, handler, host, realm);
                        handler.proceed(username, password);
                    }
                });

                timer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webCamera.loadUrl(webURL);
                        timer.postDelayed(this, delay);
                    }
                }, delay);
            }
        });
    }
}