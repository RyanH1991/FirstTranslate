package com.example.firsttranslate;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//public class MainActivity extends AppCompatActivity {
//
//    EditText inputText;
//    EditText outputText;
//    Button translateButton;
//    Translate translate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        inputText = findViewById(R.id.inputText);
//        outputText = findViewById(R.id.outputText);
//        translateButton = findViewById(R.id.translateButton);
//
//        try {
//            InputStream is = getAssets().open("test-project-06252022-8f28cee94883.json");
//            GoogleCredentials credentials = GoogleCredentials.fromStream(is);
//            translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        translateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                translateText(inputText.getText().toString());
//            }
//        });
//    }
//
//    private void translateText(String input) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
//
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                Translation translation = translate.translate(
//                        input,
//                        Translate.TranslateOption.sourceLanguage("en"),
//                        Translate.TranslateOption.targetLanguage("es")
//                );
//                String translatedText = translation.getTranslatedText();
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        outputText.setText(translatedText);
//                    }
//                });
//            }
//        });
//    }
//}

//public class MainActivity extends AppCompatActivity {
//
//    EditText inputText;
//    EditText outputText;
//    EditText sourceLanguage;
//    EditText targetLanguage;
//    Button translateButton;
//    Translate translate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        inputText = findViewById(R.id.inputText);
//        outputText = findViewById(R.id.outputText);
//        sourceLanguage = findViewById(R.id.sourceLanguage);
//        targetLanguage = findViewById(R.id.targetLanguage);
//        translateButton = findViewById(R.id.translateButton);
//
//        try {
//            InputStream is = getAssets().open("test-project-06252022-8f28cee94883.json");
//            GoogleCredentials credentials = GoogleCredentials.fromStream(is);
//            translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        translateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                translateText(inputText.getText().toString(),
//                        sourceLanguage.getText().toString(),
//                        targetLanguage.getText().toString());
//            }
//        });
//    }
//
//    private void translateText(String input, String sourceLang, String targetLang) {
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Handler handler = new Handler(Looper.getMainLooper());
//
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                Translation translation = translate.translate(
//                        input,
//                        Translate.TranslateOption.sourceLanguage(sourceLang),
//                        Translate.TranslateOption.targetLanguage(targetLang)
//                );
//                String translatedText = translation.getTranslatedText();
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        outputText.setText(translatedText);
//                    }
//                });
//            }
//        });
//    }
//}

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    EditText inputText;
    EditText outputText;
    EditText sourceLanguage;
    EditText targetLanguage;
    Button translateButton;
    Translate translate;
    Map<String, String> languageCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        sourceLanguage = findViewById(R.id.sourceLanguage);
        targetLanguage = findViewById(R.id.targetLanguage);
        translateButton = findViewById(R.id.translateButton);

        // Create a mapping from language names to language codes
        languageCodes = new HashMap<>();
        languageCodes.put("English", "en");
        languageCodes.put("Spanish", "es");
        languageCodes.put("French", "fr");
        languageCodes.put("German", "de");
        languageCodes.put("Italian", "it");
        languageCodes.put("Hindi", "hi");
        // Add more languages to the map as needed

        try {
            InputStream is = getAssets().open("test-project-06252022-8f28cee94883.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(is);
            translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateText(inputText.getText().toString(),
                        languageCodes.get(sourceLanguage.getText().toString()),
                        languageCodes.get(targetLanguage.getText().toString()));
            }
        });
    }

    private void translateText(String input, String sourceLang, String targetLang) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Translation translation = translate.translate(
                        input,
                        Translate.TranslateOption.sourceLanguage(sourceLang),
                        Translate.TranslateOption.targetLanguage(targetLang)
                );
                String translatedText = translation.getTranslatedText();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        outputText.setText(translatedText);
                    }
                });
            }
        });
    }
}

