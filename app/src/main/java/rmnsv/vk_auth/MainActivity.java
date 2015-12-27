package rmnsv.vk_auth;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiOwner;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.util.VKUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText text_to_post;
    Button publish_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String[] need = new String[]{VKScope.WALL, VKScope.FRIENDS};

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKSdk.login(this, need);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), "Авторизация прошла успешно!", Toast.LENGTH_LONG).show();
                text_to_post = (EditText) findViewById(R.id.text_to_post);
                publish_confirm = (Button) findViewById(R.id.publish_confirm);
                text_to_post.setText("А я только что сумел сделать вот что:" + /* task_name + */ "! \n Ай да я!"); // (головка от руля)
                // Тут нужно передать название таски (желательно, чтобы она была в инфинитиве).
                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        String post_it = text_to_post.getText().toString();
                        if (!Objects.equals(post_it, "")) {
                            VKRequest request = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, "", VKApiConst.MESSAGE, post_it, VKApiConst.ATTACHMENTS, "photo157847440_381149447"));
                            request.executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    super.onComplete(response);
                                    Toast.makeText(getApplicationContext(), "Запись опубликована.", Toast.LENGTH_LONG).show();
                                    text_to_post.setText("");
                                }
                            });
                            request.attempts = 10;
                        }

                    }
                };

                publish_confirm.setOnClickListener(onClickListener);
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "(Ярик, я нихуя не залогинился!)", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
