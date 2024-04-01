package com.bc.testip;

import java.net.Inet4Address;
import java.net.Inet6Address;
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
	private TextView textView2;
	private TextView textView3;
	private OtherRun otherRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        textView=(TextView) findViewById(R.id.textView1);
        textView2=(TextView) findViewById(R.id.textView2);
        textView3=(TextView) findViewById(R.id.textView3);
        
        otherRun=new OtherRun();
        Thread thread=new Thread(otherRun,"�����߳�");
        thread.start();
        try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			textView.setText(otherRun.getIpAddress());
			textView2.setText(otherRun.getIpAddressLocalIpv6());
			textView3.setText(otherRun.getIpAddressIpv6());
        
        ipButton=(Button) findViewById(R.id.ipButton);
        ipButton.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View arg0) {
				otherRun=new OtherRun();
				Thread thread=new Thread(otherRun, "�����߳�");
				thread.start();
				
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					textView.setText(otherRun.getIpAddress());
					textView2.setText(otherRun.getIpAddressLocalIpv6());
					textView3.setText(otherRun.getIpAddressIpv6());
			}
		});
    }

    private String getIp() throws Exception {
		// TODO Auto-generated method stub
    	try {
            // ��ȡ��������ӿ�
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // �ų��ػ��ӿں�����ӿ�
                if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }
                // �����ӿ��ϵ����е�ַ
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // �ų� IPv6 ��ַ�ͱ�����·��ַ
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress() || inetAddress.isAnyLocalAddress() || inetAddress.isMulticastAddress()) {
                        continue;
                    }
                    // ���� IPv4 ��ַ
                    if (inetAddress instanceof Inet4Address) {
                    	String inetAddressIpv4=inetAddress.getHostAddress();
                        return inetAddressIpv4;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "No IPv4 Address found";
    }
    
    private String getLocalIpForIpv6() throws Exception {
		// TODO Auto-generated method stub
    	// TODO Auto-generated method stub
    	InetAddress inetAddress=InetAddress.getLocalHost();
    	System.out.println(inetAddress);
    	Inet4Address inet4Address=(Inet4Address) Inet4Address.getLocalHost();
    	String inetAddressStr=inetAddress.toString();
    	System.out.println(inet4Address);
    	
    	List<NetworkInterface> networkInterfaces=Collections.list(NetworkInterface.getNetworkInterfaces());
    	for (NetworkInterface networkInterface : networkInterfaces) {
			Collection<InetAddress> inetAddressList=Collections.list(networkInterface.getInetAddresses());
			for (InetAddress inetAddress2 : inetAddressList) {
				System.out.println(inetAddress2.getHostAddress());
				return inetAddress2.getHostAddress();
			}
		}
    	return "No Local IPv6 Address found";
	}
    
   private String getIpv6(){
       try {
    	   Enumeration<NetworkInterface>  networkInterfaces = NetworkInterface.getNetworkInterfaces();
           while (networkInterfaces.hasMoreElements()) {
               NetworkInterface networkInterface = networkInterfaces.nextElement();
               if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                   continue;
               }
               Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
               while (inetAddresses.hasMoreElements()) {
                   InetAddress inetAddress = inetAddresses.nextElement();
                   if (inetAddress instanceof Inet6Address) {
                       Inet6Address inet6Address = (Inet6Address) inetAddress;
                       // �ų�������·��ַ
                       if (!inet6Address.isLinkLocalAddress()) {
                           return inetAddress.getHostAddress();
                       }
                   }
               }
           }
       } catch (SocketException e) {
           e.printStackTrace();
       }
       return "No IPv6 Address found";
	   
   }
    
    
   class OtherRun implements Runnable{
	   
	  private String ipAddress;
	  private String ipAddressLocalIpv6;
	  private String ipAddressIpv6;
	  
	  public String getIpAddress() {
		return ipAddress;
	}
	  public String getIpAddressLocalIpv6() {
		return ipAddressLocalIpv6;
	}
	  public String getIpAddressIpv6() {
		return ipAddressIpv6;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ipAddress=getIp();
			ipAddressLocalIpv6=getLocalIpForIpv6();
			ipAddressIpv6=getIpv6();
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
