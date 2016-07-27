package com.mz.autoclick;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.mz.bean.AppInfo;
import com.mz.config.Contants;
import com.mz.config.PackageNameConfig;
import com.mz.db.DBDao;
import com.mz.utils.ActivityUtil;
import com.mz.utils.FileUtil;
import com.mz.utils.ListViewHelpUtil;
import com.mz.utils.Logger;
import com.mz.utils.SprefUtil;
import com.mz.utils.TTSUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	static MainActivity mainActivity;

	private TextView tv_percent_tips/*, tv_pn_tips*/, tv_install_tips,
			tvRebootRecodeTips,tvChooseApk;
	private ListView lv_appInfo;
	private static Button btn_down;
	private Button btn_get_app_sd, btn_install_time_set;// broadcast
	private Button btn_percent_set, btn_addToDB;
	private EditText et_percent, et_add_pn, et_add_app_name, et_install_time;
	private ToggleButton tb_tomarket, tb_to_hdj_hook, tb_auto_click,
			tb_auto_reboot;
	private RadioGroup rgMarket;
	private RadioButton rbBaiDu, rb360, rbYyb, rbWdj, rbPPAssistant,rbAnZhi,rbMu;
	private boolean isIMEISame = true;
	private Toast toast = null;
	private Context context;
	private MyAdapter adapter;
	private DBDao dbDao;
	private int selection;
	private int installTime;
	private int market;
	private final static int MARKET_BAIDU = 0;
	private final static int MARKET_360 = 1;
	private final static int MARKET_YYB = 2;
	private final static int MARKET_WDJ = 3;
	private final static int MARKET_PP = 4;
	private final static int MARKET_AZ = 5;
	private final static int MARKET_MU = 6;

	/**
	 * 供广播调用跳转到应用市场
	 */
	public static void handleDownload() {
		btn_down.performClick();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		mainActivity = this;
		dbDao = DBDao.getInstance(context);
		startService(new Intent(this, PauseService.class));
		setTitle(String.valueOf(SprefUtil.getInt(context,
				SprefUtil.C_COUNT, 0)));
		selection = SprefUtil.getInt(context, SprefUtil.C_SELECTION, 0);
		installTime = SprefUtil.getInt(context, SprefUtil.C_INSTALL_TIME,
				0);
		market = SprefUtil.getInt(context, SprefUtil.C_MARKET,
				MARKET_BAIDU);
		findViewById();
		ListViewHelpUtil.setListViewHeightBasedOnChildren(lv_appInfo);
		ActivityUtil.checkAccessibility(context);
		getPrepare();

	}

	private void getPrepare() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				FileUtil.getInstance(context).copySoundFile(Contants.SOUND_NAME,
						Contants.SOUND_NAME);
				ActivityUtil.showPermission(context);
			}
		}).start();

		
	}

	List<AppInfo> list;

	private void findViewById() {
		lv_appInfo = (ListView) findViewById(R.id.lv_app_choose_list);
		list = dbDao.loadAll();
		if (list != null)
			adapter = new MyAdapter(context, list);
		
		adapter.setSelection(selection);
		if(lv_appInfo!=null)
		lv_appInfo.setAdapter(adapter);
		tv_percent_tips = (TextView) findViewById(R.id.tv_percent_tips);
//		tv_pn_tips = (TextView) findViewById(R.id.tv_package_name_tips);
		tv_install_tips = (TextView) findViewById(R.id.tv_install_tips);
		tvRebootRecodeTips = (TextView) findViewById(R.id.tv_reboot_recode_tips);
		tvChooseApk = (TextView) findViewById(R.id.tv_choose_apk);
		btn_percent_set = (Button) findViewById(R.id.btn_percent_set);
		btn_addToDB = (Button) findViewById(R.id.btn_package_name_set);
		btn_down = (Button) findViewById(R.id.btn_down);
		btn_get_app_sd = (Button) findViewById(R.id.btn_get_app_sd);
		btn_install_time_set = (Button) findViewById(R.id.btn_install_time_set);
		et_percent = (EditText) findViewById(R.id.et_percent);
		et_add_pn = (EditText) findViewById(R.id.et_package_name);
		et_add_app_name = (EditText) findViewById(R.id.et_app_name);
		et_install_time = (EditText) findViewById(R.id.et_install_time);
		tb_tomarket = (ToggleButton) findViewById(R.id.tb_to_market);
		tb_to_hdj_hook = (ToggleButton) findViewById(R.id.tb_to_hdj_hook);
		tb_auto_click = (ToggleButton) findViewById(R.id.tb_auto_click);
		tb_auto_reboot = (ToggleButton) findViewById(R.id.tb_auto_reboot);
		rgMarket = (RadioGroup) findViewById(R.id.rg_market);
		rbBaiDu = (RadioButton) findViewById(R.id.rb_bd);
		rb360 = (RadioButton) findViewById(R.id.rb_360);
		rbYyb = (RadioButton) findViewById(R.id.rb_yyb);
		rbWdj = (RadioButton) findViewById(R.id.rb_wdj);
		rbPPAssistant = (RadioButton) findViewById(R.id.rb_pp_assistant);
		rbAnZhi = (RadioButton) findViewById(R.id.rb_az);
		rbMu = (RadioButton) findViewById(R.id.rb_mu);
		setEvent();
		getpackageName();
		getRebootTime();
		
	
		tv_install_tips.setText(String.format(
				getString(R.string.set_install_tips), installTime));
		et_install_time.setText(installTime + "");
		tb_auto_click
				.setChecked(SprefUtil.getBool(context, SprefUtil.C_AUTO_CLICK, false));
		tb_auto_reboot  
				.setChecked(SprefUtil.getBool(context, SprefUtil.C_AUTO_REBOOT, false));
	}

	private void getRebootTime() {
		int rebootCount =FileUtil.getRebootTimes();
		if(rebootCount>0){
			tvRebootRecodeTips.setText(String.format(getString(R.string.reboot_recode_tips), rebootCount));
		}else {
			tvRebootRecodeTips.setText(getString(R.string.reboot_recode_tips_no_reboot));
		}
	}

	private void setEvent() {
		int percent = SprefUtil.getInt(context, SprefUtil.C_HALF_DOWNLOAD, 100);
			tv_percent_tips.setText(String.format(
					getString(R.string.current_percent_tips), percent));
			et_percent.setText(percent + "");
			if(market==MARKET_WDJ){
				tv_percent_tips.setText(getString(R.string.wdj_no_half));
			}
		
		boolean autoMarket = SprefUtil.getBool(context,
				SprefUtil.C_AUTO_MARKET, true);
		tb_tomarket.setChecked(autoMarket);
		tb_tomarket
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SprefUtil.putBool(context,
								SprefUtil.C_AUTO_MARKET, isChecked);
					}
				});
		boolean autoHook = SprefUtil.getBool(context,
				SprefUtil.C_AUTO_HOOK, true);
		tb_to_hdj_hook.setChecked(autoHook);
		tb_to_hdj_hook
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SprefUtil.putBool(context, SprefUtil.C_AUTO_HOOK,
								isChecked);
					}
				});
		tb_auto_click
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SprefUtil.putBool(context, SprefUtil.C_AUTO_CLICK, isChecked);
					}
				});
		tb_auto_reboot
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SprefUtil.putBool(context, SprefUtil.C_AUTO_REBOOT, isChecked);
					}
				});
		btn_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpMarket(context);
			}
		});

		btn_percent_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(et_percent.getText().toString().trim())
						|| et_percent.getText().toString().trim().equals("0")
						|| et_percent.getText().toString().trim().equals("00")) {
					showToast(getString(R.string.set_percent_err_tips));

				}
				if (Integer.parseInt(et_percent.getText().toString().trim()) > 100) {
					et_percent
							.setError(getString(R.string.set_percent_err_tips2));
					showToast(getString(R.string.set_percent_err_tips2));
				} else {
					int value = Integer.parseInt(et_percent.getText()
							.toString().trim());
					SprefUtil.putInt(context, SprefUtil.C_HALF_DOWNLOAD, value);
					
					showToast(getString(R.string.set_percent_ok));
					int percent = SprefUtil.getInt(context, SprefUtil.C_HALF_DOWNLOAD, 100);
					tv_percent_tips.setText(String.format(
							getString(R.string.current_percent_tips), percent));
				}
			}
		});
		btn_addToDB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!TextUtils.isEmpty(et_add_pn.getText().toString())
						&& et_add_pn.getText().toString().length() > 0) {
					AppInfo app = new AppInfo();
					app.setAppName(et_add_app_name.getText().toString());
					app.setPackageName(et_add_pn.getText().toString());
					dbDao.insertAPP(app);
					notifyList();
				}
			}
		});
		lv_appInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.setSelection(position);
				adapter.notifyDataSetChanged();
				selection = position;
				SprefUtil.putInt(context, SprefUtil.C_SELECTION, position);
				getpackageName();
			}
		});
		lv_appInfo.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				showChooseDialog(position);
				// showDeleteDialog(position);
				return true;
			}
		});
		btn_get_app_sd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("*/*");// 设置类型，我这里是任意类型，任意后缀的可以这样写。
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				startActivityForResult(intent, 1);
				//
			}
		});
		btn_install_time_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(et_install_time.getText().toString())) {
					et_install_time
							.setError(getString(R.string.set_install_error_tips));
					return;
				}
				try {
					installTime = Integer.parseInt(et_install_time.getText()
							.toString());
				} catch (Exception e) {
					// TODO: handle exception
				}
				SprefUtil.putInt(context, SprefUtil.C_INSTALL_TIME,
						installTime);
				tv_install_tips.setText(String.format(
						getString(R.string.set_install_tips), installTime));
			}
		});
		et_percent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s.toString())) {
					try {
						int per = Integer.parseInt(s.toString());
						if (per > 100) {
							et_percent
									.setError(getString(R.string.set_percent_err_tips2));
						}
					} catch (Exception e) {

					}
				}
			}
		});
		switch (market) {
		case MARKET_BAIDU:
			rbBaiDu.setChecked(true);
			break;
		case MARKET_360:
			rb360.setChecked(true);
			break;
		case MARKET_YYB:
			rbYyb.setChecked(true);
			break;
		case MARKET_WDJ:
			rbWdj.setChecked(true);
			break;
		case MARKET_PP:
			rbPPAssistant.setChecked(true);
			break;
		case MARKET_AZ:
			rbAnZhi.setChecked(true);
			break;
		case MARKET_MU:
			rbMu.setChecked(true);
			break;
		default:
			break;
		}
		rgMarket.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_bd:
					market = MARKET_BAIDU;
					break;
				case R.id.rb_360:
					market = MARKET_360;
					break;
				case R.id.rb_yyb:
					market = MARKET_YYB;
					break;
				case R.id.rb_wdj:
					market = MARKET_WDJ;
					showToast(getString(R.string.wdj_no_half));
					
					break;
				case R.id.rb_pp_assistant:
					market = MARKET_PP;
					break;
				case R.id.rb_az:
					market = MARKET_AZ;
					break;
				case R.id.rb_mu:
					market = MARKET_MU;
					break;
				default:
					break;
				}
				if(checkedId==R.id.rb_wdj){
					tv_percent_tips.setText(getString(R.string.wdj_no_half));
					showToast(getString(R.string.wdj_no_half));
				}else {
					int percent = SprefUtil.getInt(context, SprefUtil.C_HALF_DOWNLOAD, 100);
					tv_percent_tips.setText(String.format(getString(R.string.current_percent_tips), percent));
				}
				SprefUtil.putInt(context, SprefUtil.C_MARKET, market);
			}
		});
	}

	protected void showChooseDialog(final int position) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String[] items = { getString(R.string.change_item),
				getString(R.string.delete_item) };
		builder.setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					showChangeDialog(position);
					break;
				case 1:
					showDeleteDialog(position);
					break;

				default:
					break;
				}

			}
		});
		builder.create().show();
	}

	public void showChangeDialog(final int position) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(getString(R.string.change_item_info));
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_chang_item, null);
		builder.setView(view);
		final EditText et_appName = (EditText) view.findViewById(R.id.app_name);
		final EditText et_packageName = (EditText) view
				.findViewById(R.id.app_package_name);
		final String oldPN = list.get(position).getPackageName();
		et_appName.setText(list.get(position).getAppName());
		et_packageName.setText(list.get(position).getPackageName());
		builder.setPositiveButton(getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						list.get(position).setAppName(
								et_appName.getText().toString());
						list.get(position).setAppName(
								et_packageName.getText().toString());
						AppInfo app = new AppInfo(et_packageName.getText()
								.toString(), et_appName.getText().toString(), 0);
						boolean result = dbDao.update(app, oldPN);
						if (!result)
							showToast(getString(R.string.change_qpp_error));
						notifyList();
					}
				});
		builder.setNegativeButton(getString(R.string.dialog_cancle),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.create().dismiss();
					}
				});
		builder.create().show();

	}

	public void showDeleteDialog(final int position) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setPositiveButton(getString(R.string.dialog_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dbDao.remove(list.get(position));
								if (selection == position) {
									if(selection>=1)
									selection = position - 1;
									SprefUtil.putInt(context,
											SprefUtil.C_SELECTION, selection);
								}
								notifyList();
							}
						})
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		dialog.setTitle(getString(R.string.dialog_tips));
		dialog.show();
	}

	protected void notifyList() {
		List<AppInfo> list2 = dbDao.loadAll();
		list.clear();
		list.addAll(list2);
		adapter.setList(list);
		adapter.setSelection(selection);
		getpackageName();
		adapter.notifyDataSetChanged();
		ListViewHelpUtil.setListViewHeightBasedOnChildren(lv_appInfo);
	}

	private void getpackageName() {
		Logger.i(list.size()+"====="+selection+(selection<list.size()));
		if(selection<0)selection=0;
		if (list.size() > 0 && selection<list.size())
			{tvChooseApk.setText(String.format(
					getString(R.string.current_pn_tips), list.get(selection)
							.getPackageName()));}
		else {
			tvChooseApk.setText(getString(R.string.choose_apk));
		}
	}

	/**
	 * 直接跳转不判断是否存在市场应用
	 * 
	 * @param context
	 * 
	 */
	public void jumpMarket(Context context) {

		selection = SprefUtil.getInt(context, SprefUtil.C_SELECTION, 0);
		if (list == null || list.size() <= 0 || list.size() < selection) {
			showToast(getString(R.string.not_set_package_yet));
			et_add_app_name.requestFocus();
			return;
		}
		Logger.i(selection+"============");
		String url = list.get(selection).getPackageName();
		if (TextUtils.isEmpty(url)) {
			showToast(getString(R.string.not_set_package_yet));
			et_add_app_name.requestFocus();
			return;
		}
		String packageName = "";
		switch (market) {
		case MARKET_BAIDU:
			packageName = PackageNameConfig.BAIDU_PACKAGE_NAME;
			break;
		case MARKET_360:
			packageName = PackageNameConfig._360_PACKAGE_NAME;
			break;
		case MARKET_YYB:
			packageName = PackageNameConfig.YYB_PACKAGE_NAME;
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
				ActivityUtil.openYYB(context);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case MARKET_WDJ:
			packageName = PackageNameConfig.WDJ_PACKAGE_NAME;
			break;
		case MARKET_PP:
			packageName = PackageNameConfig.PP_PACKAGE_NAME;
			break;
		case MARKET_AZ:
			packageName = PackageNameConfig.AZ_PACKAGE_NAME;
			break;
//		case MARKET_MU:
//			packageName = PackageNameConfig.MU_PN;
//			break;
		default:
			break;
		}
		Logger.i("--------------------------------------------------------------------------");
		Uri localUri = Uri.parse("market://details?id=" + url);
		Intent localIntent = new Intent("android.intent.action.VIEW", localUri);
		localIntent.setPackage(packageName);

		// localIntent.addCategory("android.intent.category.BROWSABLE");//可不用加
		// localIntent.addCategory("android.intent.category.DEFAULT");//可不用加
		localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			context.startActivity(localIntent);
		} catch (Exception e) {
			Logger.e(e.toString());
		}
		Logger.i("--------------------------------------------------------------------------");

		//
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {// 是否选择，没选择就不会继续
			Uri uri = data.getData();// 得到uri，后面就是将uri转化成file的过程。
			try {
				File file = new File(new URI(uri.toString()));
				String path = file.getAbsolutePath();
				String appName = path.substring(path.lastIndexOf("/") + 1,
						path.length());
				String packageName = ActivityUtil
						.getPackageName(context, path);
				Toast.makeText(MainActivity.this, packageName,
						Toast.LENGTH_SHORT).show();
				if (!TextUtils.isEmpty(packageName)) {
					et_add_app_name.setText(appName);
					et_add_pn.setText(packageName);
					btn_addToDB.performClick();
					et_add_app_name.setText("");
					et_add_pn.setText("");
				}
				// }
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	};

	private void showToast(String tips) {
		toast = toast == null ? Toast.makeText(getApplicationContext(), "",
				Toast.LENGTH_SHORT) : toast;
		toast.setText(tips);
		toast.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.btn_clear_count) {
			SprefUtil.putInt(context, SprefUtil.C_COUNT, 0);
			FileUtil.removeRebootRecord();
			getRebootTime();
			setTitle(SprefUtil.getInt(context, SprefUtil.C_COUNT, 0) + "");
		} else if (id == R.id.btn_test_tip) {
			TTSUtil.getInstance(this).ring();
//			Intent in=new Intent(getApplicationContext(), AlertActivity.class);
//			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(in);

		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
