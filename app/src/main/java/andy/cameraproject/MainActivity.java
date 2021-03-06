package andy.cameraproject;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    // Variables for the WebView
    private String webURL;
    private String username;
    private String password;
    private WebViewClient client;
    private WebView webCamera;

    // Variables for handler (auto-refresh)
    private Handler timer;
    private int delay;
    private static boolean finishedLoading;
    private static boolean btnStartPressed;
    private Runnable imageTick;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views as finals (constants)
        final Button btnStart = findViewById(R.id.btnStart);
        final Button btnStop = findViewById(R.id.btnStop);
        webCamera = findViewById(R.id.webCamera);
        final EditText editUsername = findViewById(R.id.editUsername);
        final EditText editPassword = findViewById(R.id.editPassword);
        final EditText editStreamURL = findViewById(R.id.editStreamURL);

        // Prepare image streamer
        timer = new Handler();
        delay = 100;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int scale = (int) Math.round(height * 0.18);

        webCamera.setInitialScale(scale);
        finishedLoading = false;
        btnStartPressed = false;

        imageTick = new Runnable() {
            @Override
            public void run() {
                webCamera.loadUrl(webURL);
                if (btnStartPressed && !finishedLoading) delay += 20;
                timer.postDelayed(this, delay);
            }
        };

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartPressed = true;
                startTimer();

                // Get current input
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                webURL = editStreamURL.getText().toString().concat(getString(R.string.stream_URL_ending));

                client = new MyWebViewClient(username, password);
                webCamera.setWebViewClient(client);
                webCamera.loadUrl(webURL);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartPressed = false;
                stopTimer();
            }
        });
    }

    private void startTimer() {
        timer.postDelayed(imageTick, delay);
    }

    private void stopTimer() {
        // Currently stops the images from loading onto device
        timer.removeCallbacks(imageTick);
    }

    public static void setFinishedLoading(boolean finished) {
        finishedLoading = finished;
    }
}


class MyWebViewClient extends WebViewClient {
    private String handlerUsername;
    private String handlerPassword;

    MyWebViewClient(String username, String password) {
        handlerUsername = username;
        handlerPassword = password;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        handler.proceed(handlerUsername, handlerPassword);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        MainActivity.setFinishedLoading(false);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        MainActivity.setFinishedLoading(true);
    }
}