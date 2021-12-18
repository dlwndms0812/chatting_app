package com.example.final_exam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //private static Handler mHandler;
    private Button btn;
    private EditText editText;
    private String str; //editText를 받을 것. 서버로 보내기 위함
    private String add="127.0.0.1"; //서버 ip //192.168.200.190
    private String res; //서버에서 응답
    private TextView mytext1, mytext2, mytext3, mytext4, mytext5, servertext1, servertext2, servertext3, servertext4, servertext5;
    private static int cnt=1; //버튼 횟수, 텍스트 몇번에 써야하는지 알기 위해

    //시간 출력
    long now=System.currentTimeMillis();
    Date date=new Date(now);
    SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm:ss");
    String getTime=dateFormat.format(date);
    /*
    버튼을 클릭했을 때
    1. editText의 서버 IP 주소와 전송할 데이터 가져오기
    2. 소켓 통신을 위한 스레드의 매개변수로 넣어주어 스레드 객체 생성
    3. 스레드 시작
    */

    Handler handler=new Handler(); //토스트를 띄우기 위한 메인스레드 핸들러 객체 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        mytext1=findViewById(R.id.textView4);
        mytext2=findViewById(R.id.textView5);
        mytext3=findViewById(R.id.textView6);
        mytext4=findViewById(R.id.textView7);
        mytext5=findViewById(R.id.textView8);
        servertext1=findViewById(R.id.textView9);
        servertext2=findViewById(R.id.textView10);
        servertext3=findViewById(R.id.textView11);
        servertext4=findViewById(R.id.textView12);
        servertext5=findViewById(R.id.textView13);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str= editText.getText().toString();
                if(cnt==1){
                    mytext1.setText("("+getTime+") "+str);
                }
                else if(cnt==2){
                    mytext2.setText("("+getTime+") "+str);
                }
                else if(cnt==3){
                    mytext3.setText("("+getTime+") "+str);
                }
                else if(cnt==4){
                    mytext4.setText("("+getTime+") "+str);
                }
                else
                    mytext5.setText("("+getTime+") "+str);


                SocketThread thread=new SocketThread(add, str);
                thread.start();
                thread.run();
                cnt++;
            }
        });

    }

    class SocketThread extends Thread{
        String host; //서버 IP
        String data; //전송 데이터

        public SocketThread(String host, String data){
            this.host=host;
            this.data=data;
        }

        @Override
        public void run(){
            try{
                Toast.makeText(MainActivity.this, "서버 연결 " , Toast.LENGTH_LONG).show();
                int port = 5555; //포트 번호는 서버측과 똑같이
                Socket socket = new Socket(host, port); // 소켓 열어주기
                ObjectOutputStream outstream = new ObjectOutputStream(socket.getOutputStream()); //소켓의 출력 스트림 참조
                outstream.writeObject(data); // 출력 스트림에 데이터 넣기
                outstream.flush(); // 출력

                ObjectInputStream instream = new ObjectInputStream(socket.getInputStream()); // 소켓의 입력 스트림 참조
                res = (String) instream.readObject(); // 응답 가져오기

                /* 토스트로 서버측 응답 결과 띄워줄 러너블 객체 생성하여 메인스레드 핸들러로 전달 */
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "서버 응답 : " + res, Toast.LENGTH_LONG).show();

                        if(cnt==1){
                            servertext1.setText("("+getTime+") "+data);
                        }
                        else if(cnt==2){
                            servertext2.setText("("+getTime+") "+data);
                        }
                        else if(cnt==3){
                            servertext3.setText("("+getTime+") "+data);
                        }
                        else if(cnt==4){
                            servertext4.setText("("+getTime+") "+data);
                        }
                        else
                            servertext5.setText("("+getTime+") "+data);

                    }
                });

                socket.close(); // 소켓 해제

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


}
