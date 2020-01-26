package com.example.se;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.se.fightingthreads.Judge;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final int MAX = 3;
    private int questionCount=1;
    private TextView mTextViewResult;
    private TextView mTextViewAnswers;
    private TextView mPleaseEnter;
    private TextView mFinalText;
    OkHttpClient client = new OkHttpClient();

    String url = "http://10.132.2.180:8080/question/";

    private EditText mConfirmedName;
    private String savedName;
    private Button nameButton;
    private TextView mTextShowName;

    //ABCD buttons
    private Button a;
    private Button b;
    private Button c;
    private Button d;
    //Answers
    private int globalID[] ={1,2,3,4};
    private String globalText[]={"","","",""};
    private boolean globalCorrect[]={false,false,false,false};

    private int globalQID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewResult = findViewById(R.id.text_view_result);
        mConfirmedName = findViewById(R.id.name_edittext);
        nameButton = findViewById(R.id.button5);
        mPleaseEnter=findViewById(R.id.textView);
        mTextShowName=findViewById(R.id.textView2);
        mTextViewAnswers=findViewById(R.id.textView3);
        mFinalText=findViewById(R.id.textView4);
        a=findViewById(R.id.button4);
        b=findViewById(R.id.button3);
        c=findViewById(R.id.button2);
        d=findViewById(R.id.button);





    }

    public void sendAnswer(int ID, String Text,boolean correct,int QID ){

        Judge judge = new Judge();
        if(!judge.start())
            correct = !correct;

        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json="{\n" +
                "        \"id\":" +ID+ ",\n" +
                "        \"text\":"+"\""+Text+"\""+ ",\n" +
                "        \"correct\":" +correct+",\n" +
                "        \"question_id\":" +QID+",\n" +
                "        \"player\":"+"\""+savedName+"\""+ "\n" +
                "}";
        RequestBody body = RequestBody.create(json, JSON);

        Request request2 = new Request.Builder()
                .url("http://10.132.2.180:8080/answers")
                .post(body)
                .build();

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });





    }
    public void nextQuestion()  {

        Request request = new Request.Builder()
                .url(url+questionCount)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {


                    String str=response.body().string();
                    String k=str.substring(6,7);
                    globalQID= Integer.parseInt(k);
                    System.out.println(k);

                    str = str.substring(16,str.length() - 2);

                    final String myResponse=str;


                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextViewResult.setText(myResponse);
                                try {
                                    Thread.sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });




                }
            }
        });
    }







    public void nextAnswers()  {

        Request request = new Request.Builder()
                .url(url+questionCount+"/answers")
                .build();
        questionCount++;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {


                    String str=response.body().string();



                    try {

                        JSONArray Jarray = new JSONArray(str);
                        str="";
                        String lett[]={"A.","B.","C.","D."};
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object     = Jarray.getJSONObject(i);


                            String k = (String) object.get("text");

                            globalID[i]= (int) object.get("id");
                            globalCorrect[i]= (boolean) object.get("correct");
                            globalText[i] = (String) object.get("text");
                            str=str+lett[i]+k+"\n";

                            System.out.println(k+"  "+globalID[i]+"  "+globalCorrect[i]+"  "+globalText[i]+"  ");
                        }

                    }catch (JSONException err){
                        Log.d("Error", err.toString());
                    }


                    final String myResponse=str;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextViewAnswers.setText(myResponse);
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });




                }
            }
        });
    }


    public void clearScreen(){

        mTextViewAnswers.setVisibility(View.GONE);
        mTextShowName.setVisibility(View.GONE);
        mConfirmedName.setVisibility(View.GONE);
        mTextViewResult.setVisibility(View.GONE);
        a.setVisibility(View.GONE);
        b.setVisibility(View.GONE);
        c.setVisibility(View.GONE);
        d.setVisibility(View.GONE);
        mPleaseEnter.setVisibility(View.GONE);

    }

    public void finishGame(){
        Request request = new Request.Builder()
                .url("http://10.132.2.180:8080/player/"+savedName+"/score")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {


                    final String myResponse=response.body().string();


                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFinalText.setText("Congratulations, "+savedName+", your score is "+myResponse+"!");
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        });



    }

    public void sendD(View view) {

        if(questionCount>MAX)
        {
            clearScreen();
            finishGame();
        }else {
            sendAnswer(globalID[3], globalText[3], globalCorrect[3], globalQID);
            nextQuestion();
            nextAnswers();
        }
    }

    public void sendC(View view) {
        if(questionCount>MAX)
        {
            clearScreen();
            finishGame();
        }else {
            sendAnswer(globalID[2], globalText[2], globalCorrect[2], globalQID);
            nextQuestion();
            nextAnswers();
        }
    }

    public void sendB(View view) {
        if(questionCount>MAX)
        {
            clearScreen();
            finishGame();
        }else {
            sendAnswer(globalID[1], globalText[1], globalCorrect[1], globalQID);
            nextQuestion();
            nextAnswers();
        }
    }

    public void sendA(View view) {
        if(questionCount>MAX)
        {
            clearScreen();
            finishGame();
        }else {
            sendAnswer(globalID[0], globalText[0], globalCorrect[0], globalQID);
            nextQuestion();
            nextAnswers();
        }
    }

    public void confirmName(View view) {
        savedName=mConfirmedName.getText().toString();
        nameButton.setVisibility(View.GONE);
        mTextShowName.setText(savedName+"  GO GO GO!");

        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json="";
        RequestBody body = RequestBody.create(json, JSON);

        Request request2 = new Request.Builder()
                .url("http://10.132.2.180:8080/player?name="+savedName)
                .post(body)
                .build();

        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

            }
        });



        nextQuestion();
        nextAnswers();
    }
}