package andy.cameraproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnLogin = findViewById(R.id.btnLogin);
        final WebView webViewCamera = findViewById(R.id.webViewCamera);
        final EditText editTextAuthUsername = findViewById(R.id.editTextAuthUsername);
        final EditText editTextAuthPassword = findViewById(R.id.editTextAuthPassword);

        webViewCamera.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                handler.proceed(editTextAuthUsername.getText().toString(), editTextAuthPassword.getText().toString());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webViewCamera.loadUrl("http://192.168.2.15:8081/");
            }
        });
    }
}