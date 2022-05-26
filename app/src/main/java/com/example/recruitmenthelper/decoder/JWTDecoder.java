package com.example.recruitmenthelper.decoder;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.recruitmenthelper.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JWTDecoder {

    private static final String TOKEN_REGEX = "\\.";
    private static final String DECODING_ERROR_MESSAGE = "Decoding of token failed!";

    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static User getUserFromToken(String token) throws JSONException {
        String[] tokenParts = token.split(TOKEN_REGEX);
        String body = new String(decoder.decode(tokenParts[1]));
        JSONObject jsonObject = new JSONObject(body);
        try {
            return new User(jsonObject.getInt("userId"), jsonObject.getString("username"), jsonObject.getString("email"), jsonObject.getString("role"));
        } catch (IllegalArgumentException | JSONException exception) {
            throw new RuntimeException(DECODING_ERROR_MESSAGE);
        }
    }
}
