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

    Handler timer;
    int delay;
    int fps;

    private String webURL;
    private String username;
    private String password;

    private WebViewClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views as finals
        final Button btnStart = findViewById(R.id.btnStart);
        final WebView webCamera = findViewById(R.id.webCamera);
        final EditText editUsername = findViewById(R.id.editUsername);
        final EditText editPassword = findViewById(R.id.editPassword);
        final EditText editStreamURL = findViewById(R.id.editStreamURL);

        // Prepare image streamer
        timer = new Handler();
        fps = 10;
        delay = 1000 / fps;
        webCamera.setInitialScale(210);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current states
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                webURL = editStreamURL.getText().toString();

                client = new MyWebViewClient(username, password);
                webCamera.setWebViewClient(client);

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

class MyWebViewClient extends WebViewClient {
    private String handlerUsername;
    private String handlerPassword;


    public MyWebViewClient(String username, String password) {
        handlerUsername = username;
        handlerPassword = password;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        handler.proceed(handlerUsername, handlerPassword);
    }
}