package com.miaotu.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.miaotu.util.Util;
import com.miaotu.view.LoadingDlg;

/**
 * 网络访问异步任务基础类，包括执行前对网络的检测，执行时对异常的捕获，提示框的显示等一系列封装
 * @author zhangying
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */ 
@SuppressLint("NewApi")
public abstract class BaseHttpAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	protected Dialog mLoadingDlg; // 加载对话框
	protected Activity activity = null; // Activity
	private boolean showDlg = true;// 是否显示加载对话框
	private boolean onPostExcuted = false;// onPostExecute方法是否已经执行过(防止递归调用)
	private boolean onPreExcuted = false;// onPostExecute方法是否已经执行过(防止递归调用)
	protected Throwable throwable;// 捕获到的异常
	private boolean showTip = true;//发生异常状况时是否进行提示
	private boolean success;

	/**
	 * 构造方法
	 * @param
	 */
	public BaseHttpAsyncTask(Activity activity) {
		this.activity = activity;
	}

	/**
	 * 构造方法
	 * @param activity
	 * @param showDlg 是否显示提示对话框
	 */
	public BaseHttpAsyncTask(Activity activity, boolean showDlg) {
		this.activity = activity;
		this.showDlg = showDlg;
	}
	
	
	/**
	 * 构造方法
	 * @param activity
	 * @param showDlg 是否显示等待对话框
	 * @param showTip 异常时是否显示toast提示信息
	 */
	public BaseHttpAsyncTask(Activity activity, boolean showDlg,boolean showTip) {
		this.activity = activity;
		this.showDlg = showDlg;
		this.showTip = showTip;
	}


	@Override
	protected final void onPreExecute() {
		if(onPreExcuted){
			return;
		}
		success = false;
		onPreExcuted = true;
		super.onPreExecute();
		if (!Util.isNetworkConnected(activity)) {
			Toast.makeText(activity, "没有网络连接", Toast.LENGTH_SHORT).show();
//			finallyRun();
			this.cancel(true);
			return;
		}
		if (showDlg) {
			mLoadingDlg = LoadingDlg.show(activity);
		}
		preRun();
	}
	
	/**
	 * 任务执行前执行操作
	 */
	protected void preRun(){
	}

	protected final void onPostExecute(Result result) {
		// 如果该方法已经被调用过，则直接返回
		if (onPostExcuted) {
			return;
		} else {
			onPostExcuted = true;
		}
		dismissDlg();
		// 如果throwable不为空，则执行任务时捕获到了异常
		if (throwable != null || result == null) {
			if(showTip){
				Toast.makeText(activity, "服务器响应超时", Toast.LENGTH_SHORT).show();
			}
		}else{
			onCompleteTask(result);
			success = true;
		}
		finallyRun();
	};

	@Override
	protected final Result doInBackground(Params... params) {
		Result result = null;
		try {
			result = run(params);
		} catch (Exception e) {
			e.printStackTrace();
			throwable = e;
			Log.e("networkError", e.getMessage());
		}
		return result;
	}

	/**
	 * 判断任务是否执行成功
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 任务完成调用方法
	 * @param result
	 */
	protected abstract void onCompleteTask(Result result);

	/**
	 * 异步任务
	 * @param params
	 * @return
	 */
	protected abstract Result run(Params... params);
	/**
	 * 最后必须执行的方法
	 */
	protected void finallyRun(){}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		dismissDlg();
		finallyRun();
	}
	
	private void dismissDlg(){
		if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
			mLoadingDlg.dismiss();
		}
	}
}
