package com.miaotu.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.GroupReomveListener;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.NotificationCompat;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.miaotu.R;
import com.miaotu.imutil.CommonUtils;
import com.miaotu.imutil.ContactInfo;
import com.miaotu.imutil.ExpandGridView;
import com.miaotu.imutil.ExpressionAdapter;
import com.miaotu.imutil.ExpressionPagerAdapter;
import com.miaotu.imutil.IMDatabaseHelper;
import com.miaotu.imutil.ImageGridActivity;
import com.miaotu.imutil.MessageAdapter;
import com.miaotu.imutil.PasteEditText;
import com.miaotu.imutil.SmileUtils;
import com.miaotu.imutil.VoicePlayClickListener;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.ReportDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * 聊天页面
 * 
 * @author Administrator
 * 
 */
public class ChatsActivity extends BaseActivity implements OnClickListener{
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	private String name;
	private String uid;
	private String headPhoto;
	private TextView tvTitle;
	private TextView btnleft,btnRight;
//	private MyDatabaseHelper helper;
	private ListView lvChat;
//	private ListView RefreshableView;
	private List<EMMessage> ChatInfoList = null;
	private MessageAdapter chatAdapter;
	private IntentFilter filter;
	private NewMessageBroadcastReceiver receiver;
	private NotificationManager notificationManager;
	private static final int notifiId = 11;
	private PowerManager.WakeLock wakeLock;
	public String playMsgId;
	private VoiceRecorder voiceRecorder;
	private View recordingContainer;
	private int chatType=1;//单聊
	private EMConversation conversation;
	public static int resendPos;
	private ClipboardManager clipboard;
	private File cameraFile;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;
	private View buttonSetModeVoice;
	private ImageView iv_emoticons_checked;
	private Button btnMore;
	private RelativeLayout edittext_layout;
	private View more;
	private ImageView iv_emoticons_normal;
	private LinearLayout emojiIconContainer;
	private LinearLayout btnContainer;
	private List<String> reslist;
	private Drawable[] micImages;
	private PasteEditText mEditTextContent;
	private ViewPager expressionViewpager;
	private ImageView micImage;
	private View buttonSetModeKeyboard;
	
	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	private EMGroup group;
	private View buttonPressToSpeak;
	private View buttonSend;
	private InputMethodManager manager;
	private GroupListener groupListener;
	private TextView recordingHint;
	public static ChatsActivity activityInstance = null;
	private ImageView locationImgview;
	private ImageView vioceCallImgview;
	// 给谁发送消息
		private String toChatUsername;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		Intent intent = getIntent();
        // 判断单聊还是群聊
        chatType = intent.getIntExtra("chatType", CHATTYPE_SINGLE);
		//聊天id 即MD5过的userid
        toChatUsername = intent.getStringExtra("id");
		uid = intent.getStringExtra("uid");
		name = intent.getStringExtra("name");
		headPhoto = intent.getStringExtra("headphoto");
		findView();
		bindView();
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		if(group != null)
//			((TextView) findViewById(R.id.name)).setText(group.getGroupName());
		chatAdapter.refresh();
	}
	@Override
	public void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
		if (VoicePlayClickListener.isPlaying && VoicePlayClickListener.currentPlayListener != null) {
			// 停止语音播放
			VoicePlayClickListener.currentPlayListener.stopPlayVoice();
		}

		try {
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
//		EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
		// 注销广播
		try {
			unregisterReceiver(receiver);
			receiver = null;
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
			ackMessageReceiver = null;
			unregisterReceiver(deliveryAckMessageReceiver);
			deliveryAckMessageReceiver = null;
		} catch (Exception e) {
		}
	
		recycleChatInfoList();
		name = null;
		tvTitle = null;
		btnleft = null;
		lvChat = null;
		if(ChatInfoList!=null){
			ChatInfoList.clear();
			ChatInfoList = null;
		}
		chatAdapter = null;
		filter = null;
	}

	private void findView() {
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(name);
		btnleft = (TextView) findViewById(R.id.tv_left);
		btnleft.setHeight(Util.dip2px(this, 19.5f));
		btnleft.setWidth(Util.dip2px(this, 13f));
		btnleft.setBackgroundResource(R.drawable.icon_back);
		btnleft.setVisibility(View.VISIBLE);
            btnRight = (TextView) findViewById(R.id.tv_right);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Util.dip2px(this,25),Util.dip2px(this,25));
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.rightMargin = Util.dip2px(this,10f);
            btnRight.setLayoutParams(params);

		buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
		iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
		btnMore = (Button) findViewById(R.id.btn_more);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
		expressionViewpager = (ViewPager) findViewById(R.id.vPager);
		buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSend = findViewById(R.id.btn_send);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		locationImgview = (ImageView) findViewById(R.id.btn_location);
		locationImgview.setVisibility(View.GONE);
		vioceCallImgview = (ImageView) findViewById(R.id.btn_voice_call);
		vioceCallImgview.setVisibility(View.GONE);

		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		lvChat = (ListView) findViewById(R.id.lv_chat);
		ChatInfoList = new ArrayList<EMMessage>();
        if (chatType == CHATTYPE_SINGLE) { // 单聊
//            toChatUsername = getIntent().getStringExtra("userId");
            btnRight.setBackgroundResource(R.drawable.icon_btn_more);
            btnRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //显示拉黑dialog
                    ReportDialog dialog = new ReportDialog(ChatsActivity.this, uid);
                    dialog.show();
                }
            });
            btnRight.setVisibility(View.VISIBLE);
        } else {
            // 群聊
            toChatUsername = getIntent().getStringExtra("groupImId");
            group = EMGroupManager.getInstance().getGroup(toChatUsername);
            btnRight.setBackgroundResource(R.drawable.icon_more_goup);
            btnRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ChatsActivity.this,GroupDetailActivity.class);
                    intent.putExtra("groupImId",toChatUsername);
//                    intent.putExtra("groupId",getIntent().getStringExtra("groupId"));
                    startActivity(intent);
                }
            });
            btnRight.setVisibility(View.VISIBLE);

            String groupname = "";
            if(group!=null){
                groupname = group.getGroupName();
            }
            if(StringUtil.isEmpty(groupname)){
                ((TextView) findViewById(R.id.tv_title)).setText(getIntent().getStringExtra("groupName"));
            }else{
                ((TextView) findViewById(R.id.tv_title)).setText(groupname);
            }
        }
		conversation = EMChatManager.getInstance().getConversation(toChatUsername);
		// 把此会话的未读数置为0
		conversation.resetUnreadMsgCount();
		chatAdapter = new MessageAdapter(this, toChatUsername, chatType,uid,headPhoto);
		lvChat.setAdapter(chatAdapter);
		lvChat.setOnScrollListener(new ListScrollListener());
		int count = lvChat.getCount();
		if (count > 0) {
			lvChat.setSelection(count - 1);
		}
		lvChat.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
		lvChat
				.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	};

	private void bindView() {
		buttonSend.setOnClickListener(this);
		btnleft.setOnClickListener(this);
	};

	private void init() {

		if(MessageFragment.getInstance()!=null){
			MessageFragment.getInstance().refresh();
		}



        activityInstance = this;
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		more = findViewById(R.id.more);

		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] { getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02), getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04), getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06), getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08), getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10), getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12), getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };

		// 表情list
		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
//				} else {
//					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
//				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
				more.setVisibility(View.GONE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		iv_emoticons_normal.setOnClickListener(this);
		iv_emoticons_checked.setOnClickListener(this);
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		conversation = EMChatManager.getInstance().getConversation(toChatUsername);
		// 把此会话的未读数置为0
		conversation.resetUnreadMsgCount();
		// 注册一个ack回执消息的BroadcastReceiver
				IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
				ackMessageIntentFilter.setPriority(5);
				registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

				// 注册一个消息送达的BroadcastReceiver
				IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getDeliveryAckMessageBroadcastAction());
				deliveryAckMessageIntentFilter.setPriority(5);
				registerReceiver(deliveryAckMessageReceiver, deliveryAckMessageIntentFilter);

				// 监听当前会话的群聊解散被T事件
				groupListener = new GroupListener();
				EMGroupManager.getInstance().addGroupChangeListener(groupListener);

		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 注册接收消息广播
				receiver = new NewMessageBroadcastReceiver();
				IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
				// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
				intentFilter.setPriority(5);
				registerReceiver(receiver, intentFilter);
//		getChatInfoFromDb(ChatHelper.getSimpleName(id),
//				ChatHelper.getSimpleName(myId));
	}

	private void getChatInfoFromDb(String fromId, String toId) {
//		List<ChatInfo> arrChatInfos = helper.getChatInfo(fromId, toId);
//		//
//		if (arrChatInfos != null) {
//			LogUtil.d("tttt--------------------------------------");
//			ChatInfoList.clear();
//			ChatInfoList.addAll(arrChatInfos);
//			chatAdapter.notifyDataSetChanged();
//			RefreshableView.setSelection(chatAdapter.getCount());
//		}
	}

//	private BroadcastReceiver receiver = new BroadcastReceiver() {
//
////		@Override
////		public void onReceive(Context context, Intent intent) {
////			// getChatInfoFromDb(ChatHelper.getSimpleName(id),
////			// ChatHelper.getSimpleName(myId));
////			Bundle bundle = intent.getExtras();
////			ChatInfo chatInfo = (ChatInfo) bundle
////					.getSerializable(ChatHelper.CHAT_INFO);
////			if (chatInfo != null) {
////				if (chatInfo.getFromId().equalsIgnoreCase(id)) {
////					if (!StringUtil.isEmpty(chatInfo.getContent())) {
////						ChatInfoList.add(chatInfo);
////						chatAdapter.notifyDataSetChanged();
////						RefreshableView.setSelection(chatAdapter.getCount());
////						helper.reSetNum(id);
////					}
////				}
////
////			}
////
////		}
//
//	    @Override
//	    public void onReceive(Context context, Intent intent) {
//	        //消息id
//	        String msgId = intent.getStringExtra("msgid");
//	        //发消息的人的username(userid)
//	        String msgFrom = intent.getStringExtra("from");
//	        //消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
//	        //所以消息type实际为是enum类型
//	        int msgType = intent.getIntExtra("type", 0);
//	        LogUtil.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
//	        //更方便的方法是通过msgId直接获取整个message
//	        EMMessage message = EMChatManager.getInstance().getMessage(msgId);
//	        }
//
//	};

	private void recycleChatInfoList() {
		try {
			if (ChatInfoList != null && ChatInfoList.size() > 0) {
				for (EMMessage chatInfo : ChatInfoList) {
					if (chatInfo != null ) {
//						chatInfo.recycle();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		// mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
		// {
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if(hasFocus){
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		// }
		// }
		// });
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}
private void sendMsg(){
	//获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
	EMConversation conversation = EMChatManager.getInstance().getConversation(toChatUsername);
	//创建一条文本消息
	EMMessage message = EMMessage.createSendMessage(Type.TXT);
	//如果是群聊，设置chattype,默认是单聊
    if(chatType==2){
	    message.setChatType(ChatType.GroupChat);
    }else{
	    message.setChatType(ChatType.Chat);
    }
	//设置消息body、
	TextMessageBody txtBody = new TextMessageBody(mEditTextContent.getText().toString());
	message.addBody(txtBody);
	message.setAttribute("uid", readPreference("uid"));
	message.setAttribute("nick_name", readPreference("name"));
	message.setAttribute("headphoto", readPreference("headphoto")
			+ "100x100");
	//设置接收人
	message.setReceipt(toChatUsername);
	//把消息加入到此会话对象中
	conversation.addMessage(message);
	chatAdapter.refresh();
	mEditTextContent.setText("");
	lvChat.setSelection(lvChat.getCount() - 1);
}
/**
 * 消息广播接收者
 * 
 */
private class NewMessageBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 记得把广播给终结掉


		String username = intent.getStringExtra("from");
		String msgid = intent.getStringExtra("msgid");
        // 消息id
        String msgId = intent.getStringExtra("msgid");
        // 发消息的人的username(userid)
        String msgFrom = intent.getStringExtra("from");
        // 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
        // 所以消息type实际为是enum类型
        int msgType = intent.getIntExtra("type", 0);

		// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
		EMMessage message = EMChatManager.getInstance().getMessage(msgid);
            ContactInfo contactInfo = new ContactInfo();
            contactInfo.setImId(message.getFrom());
        try {
            contactInfo.setUid(message.getStringAttribute("uid"));
            contactInfo.setNickName(message
                    .getStringAttribute("nick_name"));
            contactInfo.setHeadPhoto(message
                    .getStringAttribute("headphoto"));
            LogUtil.d("chatactivity收到IM消息"+ "new message id:" + msgId + " from:" + message
                    .getStringAttribute("nick_name")
                    + " type:" + msgType + " userid:" + message.getStringAttribute("uid"));
            IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(
                    getApplicationContext());
            imDatabaseHelper.saveContactInfo(contactInfo);
        } catch (EaseMobException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
//            getUserInfo(context,intent,this);
            return;
        }
        // 如果是群聊消息，获取到group id
		if (message.getChatType() == ChatType.GroupChat) {
			username = message.getTo();
		}
		if (!username.equals(toChatUsername)) {
			// 消息不是发给当前会话，return
		    notifyNewMessage(message);
			return;
		}
        abortBroadcast();
		// conversation =
		// EMChatManager.getInstance().getConversation(toChatUsername);
		// 通知adapter有新消息，更新ui
		chatAdapter.refresh();
		lvChat.setSelection(chatAdapter.getCount());

	}
}
//    //根据imid获取用户信息
//    private void getUserInfo(final Context context,final Intent intent ,final NewMessageBroadcastReceiver receiver) {
//        new BaseHttpAsyncTask<Void, Void, UserInfoResult>(this, false) {
//            @Override
//            protected void onCompleteTask(UserInfoResult result) {
//				if(databaseList()==null){
//					return;
//				}
//                if (result.getCode() == BaseResult.SUCCESS) {
//                    User user = new User();
//                    user = result.getUser();
//                    LogUtil.d("聊天界面从后台获取用户昵称  ： " + user.getNickname());
//
//                    String username = intent.getStringExtra("from");
//                    String msgid = intent.getStringExtra("msgid");
//                    // 消息id
//                    String msgId = intent.getStringExtra("msgid");
//                    // 发消息的人的username(userid)
//                    String msgFrom = intent.getStringExtra("from");
//                    // 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
//                    // 所以消息type实际为是enum类型
//                    int msgType = intent.getIntExtra("type", 0);
//
//                    // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
//                    EMMessage message = EMChatManager.getInstance().getMessage(msgid);
//                    ContactInfo contactInfo = new ContactInfo();
//                    contactInfo.setImId(message.getFrom());
//                    contactInfo.setUid(user.getId());
//                    contactInfo.setNickName(user.getNickname());
//                    contactInfo.setHeadPhoto(user.getAvatar().getUrl()+"&size=200x200");
//                    IMDatabaseHelper imDatabaseHelper = new IMDatabaseHelper(
//                            getApplicationContext());
//                    imDatabaseHelper.saveContactInfo(contactInfo);
//
//                    //刷新
//                    // 如果是群聊消息，获取到group id
//                    if (message.getChatType() == ChatType.GroupChat) {
//                        username = message.getTo();
//                    }
//                    if (!username.equals(toChatUsername)) {
//                        // 消息不是发给当前会话，return
//                        notifyNewMessage(message);
//                        return;
//                    }
//                    receiver.abortBroadcast();
//                    // conversation =
//                    // EMChatManager.getInstance().getConversation(toChatUsername);
//                    // 通知adapter有新消息，更新ui
//                    chatAdapter.refresh();
//                    lvChat.setSelection(chatAdapter.getCount());
//
//                } else {
//                    //获取用户信息失败
//                }
//            }
//
//            @Override
//            protected UserInfoResult run(Void... params) {
//                return HttpRequestUtil.getInstance().getUserInfoByIMId(EMChatManager.getInstance().getMessage(intent.getStringExtra("msgid")).getFrom());
//            }
//
//            @Override
//            protected void onError() {
//                // TODO Auto-generated method stub
//
//            }
//        }.execute();
//    }
/**
 * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
 * 如果不需要，注释掉即可
 * @param message
 */
protected void notifyNewMessage(EMMessage message) {
    //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
    //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
    if(!EasyUtils.isAppRunningForeground(this)){
        return;
    }
    
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(getApplicationInfo().icon)
            .setWhen(System.currentTimeMillis()).setAutoCancel(true);
    
    String ticker = CommonUtils.getMessageDigest(message, this);
    if(message.getType() == Type.TXT)
        ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
    //设置状态栏提示
    try {
        mBuilder.setTicker(message
                .getStringAttribute("nick_name") + ": " + ticker);
    } catch (EaseMobException e) {
        e.printStackTrace();
    }

    Notification notification = mBuilder.build();
    //状态栏提示
    notificationManager.notify(notifiId, notification);
    notificationManager.cancel(notifiId);
}

/**
 * 重发消息
 */
private void resendMessage() {
	EMMessage msg = null;
	msg = conversation.getMessage(resendPos);
	// msg.setBackSend(true);
	msg.status = EMMessage.Status.CREATE;

	chatAdapter.refresh();
	lvChat.setSelection(resendPos);
}
/**
 * onActivityResult
 */
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == RESULT_CODE_EXIT_GROUP) {
		setResult(RESULT_OK);
		finish();
		return;
	}
	if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
		switch (resultCode) {
		case RESULT_CODE_COPY: // 复制消息
			EMMessage copyMsg = ((EMMessage) chatAdapter.getItem(data.getIntExtra("position", -1)));
			// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
			// ((TextMessageBody) copyMsg.getBody()).getMessage()));
			clipboard.setText(((TextMessageBody) copyMsg.getBody()).getMessage());
			break;
		case RESULT_CODE_DELETE: // 删除消息
			EMMessage deleteMsg = (EMMessage) chatAdapter.getItem(data.getIntExtra("position", -1));
			conversation.removeMessage(deleteMsg.getMsgId());
			chatAdapter.refresh();
			lvChat.setSelection(data.getIntExtra("position", chatAdapter.getCount()) - 1);
			break;

//		case RESULT_CODE_FORWARD: // 转发消息
//			EMMessage forwardMsg = (EMMessage) chatAdapter.getItem(data.getIntExtra("position", 0));
//			Intent intent = new Intent(this, ForwardMessageActivity.class);
//			intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
//			startActivity(intent);
//
//			break;

		default:
			break;
		}
	}
	if (resultCode == RESULT_OK) { // 清空消息
		if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
			// 清空会话
			EMChatManager.getInstance().clearConversation(toChatUsername);
			chatAdapter.refresh();
		} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
			if (cameraFile != null && cameraFile.exists())
				sendPicture(cameraFile.getAbsolutePath());
		} else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频

			int duration = data.getIntExtra("dur", 0);
			String videoPath = data.getStringExtra("path");
			File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
			Bitmap bitmap = null;
			FileOutputStream fos = null;
			try {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
				if (bitmap == null) {
					EMLog.d("chatactivity", "problem load video thumbnail bitmap,use default icon");
					bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.app_panel_video_icon);
				}
				fos = new FileOutputStream(file);

				bitmap.compress(CompressFormat.JPEG, 100, fos);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fos = null;
				}
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}

			}
			sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

		} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
			if (data != null) {
				Uri selectedImage = data.getData();
				if (selectedImage != null) {
					sendPicByUri(selectedImage);
				}
			}
		} else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
//			if (data != null) {
//				Uri uri = data.getData();
//				if (uri != null) {
//					sendFile(uri);
//				}
//			}

		} else if (requestCode == REQUEST_CODE_MAP) { // 地图
//			double latitude = data.getDoubleExtra("latitude", 0);
//			double longitude = data.getDoubleExtra("longitude", 0);
//			String locationAddress = data.getStringExtra("address");
//			if (locationAddress != null && !locationAddress.equals("")) {
//				more(more);
//				sendLocationMsg(latitude, longitude, "", locationAddress);
//			} else {
//				Toast.makeText(this, "无法获取到您的位置信息！", 0).show();
//			}
			// 重发消息
		} else if (requestCode == REQUEST_CODE_TEXT || requestCode == REQUEST_CODE_VOICE
		        || requestCode == REQUEST_CODE_PICTURE || requestCode == REQUEST_CODE_LOCATION
		        || requestCode == REQUEST_CODE_VIDEO || requestCode == REQUEST_CODE_FILE) {
			resendMessage();
		} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
			// 粘贴
			if (!TextUtils.isEmpty(clipboard.getText())) {
				String pasteText = clipboard.getText().toString();
				if (pasteText.startsWith(COPY_IMAGE)) {
					// 把图片前缀去掉，还原成正常的path
					sendPicture(pasteText.replace(COPY_IMAGE, ""));
				}

			}
		} else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
			EMMessage deleteMsg = (EMMessage) chatAdapter.getItem(data.getIntExtra("position", -1));
			addUserToBlacklist(deleteMsg.getFrom());
		} else if (conversation.getMsgCount() > 0) {
			chatAdapter.refresh();
			setResult(RESULT_OK);
		} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
			chatAdapter.refresh();
		}
	}
}
/**
 * 照相获取图片
 */
public void selectPicFromCamera() {
	if (!CommonUtils.isExitsSdcard()) {
		Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
		return;
	}

	cameraFile = new File(PathUtil.getInstance().getImagePath(), readPreference("nick_name")
			+ System.currentTimeMillis() + ".jpg");
	cameraFile.getParentFile().mkdirs();
	startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
			REQUEST_CODE_CAMERA);
}
/**
 * 发送图片
 * 
 * @param filePath
 */
private void sendPicture(final String filePath) {
	String to = toChatUsername;
	// create and add image message in view
	final EMMessage message = EMMessage.createSendMessage(Type.IMAGE);
	// 如果是群聊，设置chattype,默认是单聊
	if (chatType == CHATTYPE_GROUP)
		message.setChatType(ChatType.GroupChat);

	message.setReceipt(to);
	message.setAttribute("uid", readPreference("id"));
	message.setAttribute("nick_name", readPreference("nick_name"));
    message.setAttribute("headphoto", readPreference("url")
            + "&size=100x100");
	ImageMessageBody body = new ImageMessageBody(new File(filePath));
	// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
	// body.setSendOriginalImage(true);
	message.addBody(body);
	conversation.addMessage(message);
	lvChat.setAdapter(chatAdapter);
	chatAdapter.refresh();
	lvChat.setSelection(lvChat.getCount() - 1);
	setResult(RESULT_OK);
	// more(more);
}
	/**
	 * 发送视频消息
	 */
	private void sendVideo(final String filePath, final String thumbPath, final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage.createSendMessage(Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP)
				message.setChatType(ChatType.GroupChat);
			String to = toChatUsername;
			message.setReceipt(to);
			message.setAttribute("uid", readPreference("id"));
			message.setAttribute("nick_name", readPreference("nick_name"));
            message.setAttribute("headphoto", readPreference("url")
                    + "&size=100x100");
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath, length, videoFile.length());
			message.addBody(body);
			conversation.addMessage(message);
			lvChat.setAdapter(chatAdapter);
			chatAdapter.refresh();
			lvChat.setSelection(lvChat.getCount() - 1);
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}
	/**
	 * 加入到黑名单
	 * 
	 * @param username
	 */
	private void addUserToBlacklist(String username) {
		try {
			EMContactManager.getInstance().addUserToBlackList(username, false);
			Toast.makeText(getApplicationContext(), "移入黑名单成功", Toast.LENGTH_SHORT).show();
		} catch (EaseMobException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "移入黑名单失败", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * listview滑动监听listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
					List<EMMessage> messages;
					try {
						// 获取更多messges，调用此方法的时候从db获取的messages
						// sdk会自动存入到此conversation中
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(chatAdapter.getItem(0).getMsgId(), pagesize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(chatAdapter.getItem(0).getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// 刷新ui
						chatAdapter.refresh();
						lvChat.setSelection(messages.size() - 1);
						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;

				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}

	}
	/**
	 * 消息图标点击事件
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {

		int id = view.getId();
		if(id == R.id.tv_left){
			ChatsActivity.this.finish();
		}else
		if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
			String s = mEditTextContent.getText().toString();
			sendMsg();
		} else if (id == R.id.btn_take_picture) {
			selectPicFromCamera();// 点击照相图标
		} else if (id == R.id.btn_picture) {
			selectPicFromLocal(); // 点击图片图标
		} else if (id == R.id.btn_location) { // 位置
//			startActivityForResult(new Intent(this, BaiduMapActivity.class), REQUEST_CODE_MAP);
		} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
			more.setVisibility(View.VISIBLE);
			iv_emoticons_normal.setVisibility(View.INVISIBLE);
			iv_emoticons_checked.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.GONE);
			emojiIconContainer.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
			iv_emoticons_normal.setVisibility(View.VISIBLE);
			iv_emoticons_checked.setVisibility(View.INVISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
			more.setVisibility(View.GONE);

		} else if (id == R.id.btn_video) {
			// 点击摄像图标
			Intent intent = new Intent(ChatsActivity.this, ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
		} else if (id == R.id.btn_file) { // 点击文件图标
			selectFileFromLocal();
		} else if (id == R.id.btn_voice_call) { // 点击语音电话图标
//			if (!EMChatManager.getInstance().isConnected())
//				Toast.makeText(this, "尚未连接至服务器，请稍后重试", 0).show();
//			else
//				startActivity(new Intent(ChatsActivity.this, VoiceCallActivity.class).putExtra("username", toChatUsername).putExtra(
//						"isComingCall", false));
		}
	}
	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}
	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this, 1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
							// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class.forName("com.miaotu.imutil.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(ChatsActivity.this, (String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText().toString();
									String tempStr = body.substring(0, selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i, selectionStart);
										if (SmileUtils.containsKey(cs.toString()))
											mEditTextContent.getEditableText().delete(i, selectionStart);
										else
											mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									} else {
										mEditTextContent.getEditableText().delete(selectionStart - 1, selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}
	/**
	 * 按住说话listener
	 * 
	 */
	class PressToSpeakListen implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					Toast.makeText(ChatsActivity.this, "发送语音需要sdcard支持！", Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername, getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(ChatsActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint.setText(getString(R.string.release_to_cancel));
					recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toast.makeText(getApplicationContext(), "无录音权限", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(ChatsActivity.this, "发送失败，请检测服务器是否连接", Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}
	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();

			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			chatAdapter.refresh();

		}
	};

	/**
	 * 消息送达BroadcastReceiver
	 */
	private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			abortBroadcast();

			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isDelivered = true;
				}
			}

			chatAdapter.refresh();
		}
	};
	/**
	 * 监测群组解散或者被T事件
	 * 
	 */
	class GroupListener extends GroupReomveListener {

		@Override
		public void onUserRemoved(final String groupId, String groupName) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (toChatUsername.equals(groupId)) {
//						Toast.makeText(ChatActivity.this, "你被群创建者从此群中移除", 1).show();
//						if (GroupDetailsActivity.instance != null)
//							GroupDetailsActivity.instance.finish();
//						finish();
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(final String groupId, String groupName) {
			// 群组解散正好在此页面，提示群组被解散，并finish此页面
			runOnUiThread(new Runnable() {
				public void run() {
//					if (toChatUsername.equals(groupId)) {
//						Toast.makeText(ChatActivity.this, "当前群聊已被群创建者解散", 1).show();
//						if (GroupDetailsActivity.instance != null)
//							GroupDetailsActivity.instance.finish();
//						finish();
//					}
				}
			});
		}

	}
	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}
	/**
	 * 选择文件
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}
	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		iv_emoticons_normal.setVisibility(View.VISIBLE);
		iv_emoticons_checked.setVisibility(View.INVISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
		emojiIconContainer.setVisibility(View.GONE);

	}
	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage.createSendMessage(Type.VOICE);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP)
				message.setChatType(ChatType.GroupChat);
			message.setReceipt(toChatUsername);
			message.setAttribute("uid", readPreference("id"));
			message.setAttribute("nick_name", readPreference("nick_name"));
            message.setAttribute("headphoto", readPreference("url")
                    + "&size=100x100");
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath), len);
			message.addBody(body);

			conversation.addMessage(message);
			chatAdapter.refresh();
			lvChat.setSelection(lvChat.getCount() - 1);
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void more(View view) {
		if (more.getVisibility() == View.GONE) {
			System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			btnContainer.setVisibility(View.VISIBLE);
			emojiIconContainer.setVisibility(View.GONE);
		} else {
			if (emojiIconContainer.getVisibility() == View.VISIBLE) {
				emojiIconContainer.setVisibility(View.GONE);
				btnContainer.setVisibility(View.VISIBLE);
				iv_emoticons_normal.setVisibility(View.VISIBLE);
				iv_emoticons_checked.setVisibility(View.INVISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}

		}

	}
	public String getToChatUsername() {
		return toChatUsername;
	}
//
//	@Override
//	public void showToast(String s) {
//		// TODO Auto-generated method stub
//		showToastMsg(s);
//	};
}
