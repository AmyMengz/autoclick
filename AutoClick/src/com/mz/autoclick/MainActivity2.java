package com.mz.autoclick;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;    
import android.content.Intent;    
import android.os.Bundle;    
import android.view.Menu;    
import android.view.MenuItem;    
import android.view.View;    
import android.widget.Button;    
import android.widget.Toast;    
  
public class MainActivity2 extends Activity {    
  private Button startBtn;    
  
  @Override    
  protected void onCreate(Bundle savedInstanceState) {    
      super.onCreate(savedInstanceState);    
      setContentView(R.layout.activity_main1);    
  
      startBtn = (Button) findViewById(R.id.start);    
      startBtn.setOnClickListener(new View.OnClickListener() {    
          @Override    
          public void onClick(View v) {    
              try {    
                  //��ϵͳ�����и�������    
                  Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);    
                  startActivity(intent);    
                  Toast.makeText(MainActivity2.this, "�ҵ��������Ȼ�������񼴿�", Toast.LENGTH_LONG).show();    
              } catch (Exception e) {    
                  e.printStackTrace();    
              }    
          }    
      });    
  }    
}
