/**
 * 2015-9-30
 * @author:zhangbin
 */
package com.zhang.zhangsafe.engine;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import com.zhang.zhangsafe.util.ToastUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

public class SmsOpertor {
	private SmsOpertor(Context mContext) {
		super();
		this.mContext = mContext;
	}

	private Context mContext;
	private static Cursor cursor;
	private static FileOutputStream fos;
	private static int index = 0;
	/**
	 * 短信备份
	 * 
	 * @param mContext
	 *            上下文环境
	 * @param path
	 *            保存的路径
	 * @param pd
	 */
	public static void backUp(Context mContext, String path, CallBack callBack) {
		try {// 指向文件夹
			File file = new File(path);
			cursor = mContext.getContentResolver().query(
					Uri.parse("content://sms/"),
					new String[] { "address", "date", "type", "body" }, null,
					null, null);
			// 输入流
			fos = new FileOutputStream(file);
			//xml序列化
			XmlSerializer newSerializer = Xml.newSerializer();
			//给次xml做相应设置
			newSerializer.setOutput(fos, "utf-8");
			//DTD(xml规范)
			newSerializer.startDocument("utf-8", true);
			
			newSerializer.startTag(null, "smss");
			//pd.setMax(cursor.getCount());
			if (callBack!=null) {
				
				callBack.setMax(cursor.getCount());
			}
			while(cursor.moveToNext()){
				newSerializer.startTag(null, "sms");
				
				newSerializer.startTag(null, "address");
				newSerializer.text(cursor.getString(0));
				newSerializer.endTag(null, "address");
				
				newSerializer.startTag(null, "date");
				newSerializer.text(cursor.getString(1));
				newSerializer.endTag(null, "date");
				
				newSerializer.startTag(null, "type");
				newSerializer.text(cursor.getString(2));
				newSerializer.endTag(null, "type");
				
				newSerializer.startTag(null, "body");
				newSerializer.text(cursor.getString(3));
				newSerializer.endTag(null, "body");
				
				newSerializer.endTag(null, "sms");
				
				//8,每循环一次就需要去让进度条叠加
				
				index++;
				Thread.sleep(1000);
				//ProgressDialog可以在子线程中更新相应的进度条的改变
				
				//A 如果传递进来的是对话框,指定对话框进度条的当前百分比
				//B	如果传递进来的是进度条,指定进度条的当前百分比
			//	pd.setProgress(index);
			if (callBack!=null) {
				
				callBack.setProgress(index);
				}
				  
			
			}
			newSerializer.endTag(null, "smss");
			newSerializer.endDocument(); 
			
		}catch (Exception e) {
			e.printStackTrace();
			} finally {
	             try {
	            	 cursor.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
	            
			}

	}
	//回调
		//1.定义一个接口
		//2,定义接口中未实现的业务逻辑方法(短信总数设置,备份过程中短信百分比更新)
		//3.传递一个实现了此接口的类的对象(至备份短信的工具类中),接口的实现类,一定实现了上诉两个为实现方法(就决定了使用对话框,还是进度条)
		//4.获取传递进来的对象,在合适的地方(设置总数,设置百分比的地方)做方法的调用
		public interface CallBack{
			//短信总数设置为实现方法(由自己决定是用	对话框.setMax(max) 还是用	进度条.setMax(max))
			public void setMax(int max);
			//备份过程中短信百分比更新(由自己决定是用	对话框.setProgress(max) 还是用	进度条.setProgress(max))
			public void setProgress(int index);
		}
}
