package com.bc.testip;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
	private Button ipButton;
	private TextView textView;
	private OtherRun otherRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView=(TextView) findViewById(R.id.textView1);
        
        otherRun=new OtherRun();
        Thread thread=new Thread(otherRun,"其他线程");
        thread.start();
        try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			textView.setText(otherRun.getIpAddress());
        
        
        ipButton=(Button) findViewById(R.id.ipButton);
        ipButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				otherRun=new OtherRun();
				Thread thread=new Thread(otherRun, "其他线程");
				thread.start();
				
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					textView.setText(otherRun.getIpAddress());
				
			}
		});
    }

    private String getIp() throws Exception {
		// TODO Auto-generated method stub
    	
    	try {
            // 获取所有网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 排除回环接口和虚拟接口
                if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                // 遍历接口上的所有地址
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 排除 IPv6 地址和本地链路地址
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isAnyLocalAddress() || inetAddress.isMulticastAddress()) {
                        continue;
                    }
                    // 返回 IPv4 地址
                    if (inetAddress instanceof Inet4Address) {
                    	String inetAddressIpv4=inetAddress.getHostAddress();
                        return inetAddressIpv4;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "无法找到ip";
    }
    
    
    
    
   class OtherRun implements Runnable{
	   
	  private String ipAddress;
	  
	  public String getIpAddress() {
		return ipAddress;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
    	InetAddress inetAddress;
		try {
			ipAddress=getIp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	   
   }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
