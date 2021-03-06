package com.fit.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.fit.dao.ConnectionDAO;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText name;
	EditText pass;
	private PrintWriter request = null;
	private BufferedReader response = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        name=(EditText) findViewById(R.id.userName);
      
        pass=(EditText) findViewById(R.id.userPass);
        
        Button btn=(Button) findViewById(R.id.login);
        Button reset=(Button) findViewById(R.id.reset);
        btn.setOnClickListener(new OnClickListener() {
			
        	
			@Override
			public void onClick(View v) {
			
		        String userName=name.getText().toString();
		        String userPass=pass.getText().toString();
		        String ur="http://192.168.100.114:8080/proj_myweb/dologin";
		        HttpURLConnection connection=ConnectionDAO.getConnection(ur);
					try {
						request=new PrintWriter(connection.getOutputStream());
						request.print("userName="+userName+"&userPass="+userPass);
						request.close();
						
						//接受服务端信息 
						response=new BufferedReader(new InputStreamReader(connection.getInputStream()));
						List<String> list=new ArrayList<String>();
						String info=response.readLine();
						
						//判断是否登陆成功
						if(info.equals("ERROR")){
							Toast.makeText(MainActivity.this,"用户名或密码不正确！！",Toast.LENGTH_LONG).show();
						}else if(info.equals("Nothing")){
							Toast.makeText(MainActivity.this,"登陆成功，该用户暂无任务！！",Toast.LENGTH_LONG).show();
						}
						else{
							Toast.makeText(MainActivity.this,"登陆成功！！",Toast.LENGTH_LONG).show();
							list.add(info);
							while((info=response.readLine())!=null){
								list.add(info);
						}
							Bundle extras=new Bundle();
							extras.putStringArrayList("list",(ArrayList<String>) list);
							Intent intent=new Intent(MainActivity.this,Activity1.class);
							intent.putExtras(extras);
							startActivity(intent);
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
        
        reset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				name.setText("");
				pass.setText("");
			}
		});
    }
}