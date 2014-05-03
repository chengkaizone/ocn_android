package com.hongguaninfo.ocnandroid.main;

import com.hongguaninfo.ocnandroid.utils.OcnUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.widget.RemoteViews;
/**
 * 消息推送---有新的工单发出
 * @author Administrator
 *
 */
public class NotifyService extends Service{
	private long TIME_STEP=60000;//间隔60秒请求一次
	//通知
	private Notification notify;
	//通知管理器
	private NotificationManager notifyManager;
	//目标界面
	private Intent targetIntent;
	//意图界面
	private PendingIntent pIntent;
	//消息id---不变始终显示一条消息---因为只需要进入预约界面即可
	private int smsID=0x666;
	//通知栏的自定义视图---如果只是简单通知可以不需要布局视图
	private RemoteViews remoteView;
	//振动器
	private Vibrator vibrator;
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			int temp=msg.arg1;
			//发送通知
			sendNotify(temp);
		}
	};
	
	@Override//不需要获取后台运行的状态---不需要绑定器
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override//开启消息推送
	public int onStartCommand(Intent intent, int flags, int startId) {
		//System.out.println("开启消息服务！！！");
		//创建振动器
		vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		notify=new Notification();
		//通知栏图标
		notify.defaults=Notification.DEFAULT_SOUND;
		notify.icon=R.drawable.monitor_icon;
		//通知栏消息
		notify.tickerText="有新工单进入";
		//创建通知栏视图-----通知视图
		remoteView=new RemoteViews(getPackageName(),R.layout.notify_view);
		remoteView.setImageViewResource(R.id.image,R.drawable.monitor_icon);
		//设置通知栏视图
		notify.contentView=remoteView;
		//点击消息后消失
		notify.flags|=Notification.FLAG_AUTO_CANCEL;
		notifyManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		//点击消息后跳到预约列表-----服务器能发过来表示已经签到
		targetIntent=new Intent(this,OrderPage.class);
		//此处保证Activity只有一个实例
		targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//启动socket连接线程
		new MessageThread().start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * 服务器端下发的消息过来---WebService方案
	 * @return
	 */
	public int getNewMessage(){
		//调用webservice MainPage.service.backOrderType();
		String str=MainPage.service.selectProcessCount();
		if(str!=null){
			int i=Integer.parseInt(str);
			if(i>=OcnUtil.getProcessCount()){
				return i;
			}
		}
		return 0;
	}
	@Override//结束消息推送
	public void onDestroy() {
		//System.out.println("关闭消息服务！！！");
		System.exit(0);//正常退出---该方法对Service清理更干净
		super.onDestroy();
	}
	private class MessageThread extends Thread{
		public void run(){
			try {
				sleep(TIME_STEP);
				int temp=getNewMessage();
				if(getNewMessage()>OcnUtil.getProcessCount()){
					Message mesg=new Message();
					mesg.arg1=temp;
					handler.sendMessage(mesg);
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private void sendNotify(int count){
		//设置消息栏内容
		remoteView.setTextViewText(R.id.text,"当前连接数："+count);
		//创建延迟意图
		pIntent = PendingIntent.getActivity(
				NotifyService.this, 0, targetIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		// 将延迟意图装入消息
		//notify.contentIntent =pIntent;//只发送通知；不进入页面
		//该方法会覆盖remoteView视图
		notify.setLatestEventInfo(NotifyService.this,
				"当前连接数："+count, "        点击查看", pIntent);
		//发送通知
		notifyManager.notify(smsID,notify);
		vibrator.vibrate(100);
		//System.out.println("发送通知！！");
	}
}
