package com.example.firsttranslate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest;

import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText inputText;
    EditText outputText;
    EditText sourceLanguage;
    EditText targetLanguage;
    Button translateButton;
    Translate translate;
    Map<String, String> languageCodes;

    private final int AUDIO_REQUEST_CODE = 1;
    private final int SPEECH_REQUEST_CODE = 0;

    private TextToSpeech textToSpeechEngine;

    private BroadcastReceiver translateReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        outputText = findViewById(R.id.outputText);
        sourceLanguage = findViewById(R.id.sourceLanguage);
        targetLanguage = findViewById(R.id.targetLanguage);
        translateButton = findViewById(R.id.translateButton);

        sourceLanguage.setText("English");  // set default source language
        targetLanguage.setText("Spanish");  // set default target language

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
            InputStream is = getAssets().open("apijson");
            GoogleCredentials credentials = GoogleCredentials.fromStream(is);
            translate = TranslateOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        translateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                translateText(inputText.getText().toString(),
//                        languageCodes.get(sourceLanguage.getText().toString()),
//                        languageCodes.get(targetLanguage.getText().toString()));
//            }
//        });
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });

        //click translateButton when I press play button on my headphones
        translateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                translateButton.performClick();
            }
        };
        registerReceiver(translateReceiver, new IntentFilter("com.your.package.firsttranslate.ACTION_TRANSLATE"));

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    AUDIO_REQUEST_CODE);
        }

        textToSpeechEngine = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeechEngine.setLanguage(Locale.US); // set your desired language
                }
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


                Map<String, Locale> locales = new HashMap<>();
                locales.put("en", Locale.ENGLISH);
                locales.put("es", new Locale("es", "ES"));
                locales.put("fr", Locale.FRENCH);
                locales.put("de", Locale.GERMAN);
                locales.put("it", Locale.ITALIAN);
                // Add more languages as needed

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        outputText.setText(translatedText);
//                        // speak the translated text
//                        textToSpeechEngine.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
                        Locale targetLocale = locales.get(targetLang);
                        if (textToSpeechEngine.isLanguageAvailable(targetLocale) != TextToSpeech.LANG_MISSING_DATA && textToSpeechEngine.isLanguageAvailable(targetLocale) != TextToSpeech.LANG_NOT_SUPPORTED) {
                            textToSpeechEngine.setLanguage(targetLocale);
                            textToSpeechEngine.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null, null);
                        } else {
                            Toast.makeText(MainActivity.this, "This language is not supported for speech", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Your device does not support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = result.get(0);
            // Perform your operation here with spokenText, this is your speech converted to text.
            // You can pass this text to your Translation function and proceed with the translation operation.
            translateText(spokenText,
                        languageCodes.get(sourceLanguage.getText().toString()),
                        languageCodes.get(targetLanguage.getText().toString()));
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeechEngine != null) {
            textToSpeechEngine.stop();
            textToSpeechEngine.shutdown();
        }
        super.onDestroy();
    }

}

