package com.mz.utils;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.widget.Toast;

import com.mz.autoclick.R;
import com.mz.config.Contants;

public class TTSUtil {
	static TTSUtil instance;

	public static TTSUtil getInstance(Context context) {
		if (instance == null) {
			instance = new TTSUtil(context);
		}
		return instance;
	}

	private TTSUtil(Context context) {
		this.context = context;
		textToSpeech = new TextToSpeech(context, new TTSListener());
	}

	TextToSpeech textToSpeech = null;
	Context context;

	public void ring() {
		int result = textToSpeech.setLanguage(Locale.CHINESE);
		Logger.i(Build.VERSION.SDK_INT+"=====result==========="+result);
		if (result == TextToSpeech.LANG_MISSING_DATA
				|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
			playRadio();

		} else {
			if (textToSpeech == null) {
				textToSpeech = new TextToSpeech(context, new TTSListener());
			} else {
				textToSpeech.speak(context.getString(R.string.tts_reboot),
						TextToSpeech.QUEUE_FLUSH, null);
			}
		}

	}

	private void playRadio() {
		MediaPlayer player = new MediaPlayer();
		File soundFile=new File(Contants.EXTRA_PATH + File.separator
				+ Contants.SOUND_NAME);
		if(!soundFile.exists()){
			FileUtil.getInstance(context).copySoundFile(Contants.SOUND_NAME,
					Contants.SOUND_NAME);
		}
		try {
			player.setDataSource(Contants.EXTRA_PATH + File.separator
					+ Contants.SOUND_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			player.prepare();
			player.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class TTSListener implements OnInitListener {

		@Override
		public void onInit(int status) {
			// TODO Auto-generated method stub
			if (status == TextToSpeech.SUCCESS) {
				// int result = mSpeech.setLanguage(Locale.ENGLISH);
				int result = textToSpeech.setLanguage(Locale.CHINESE);
				// int result = textToSpeech.setLanguage(Locale.ENGLISH);
				// 如果打印为-2，说明不支持这种语言
//				Toast.makeText(context, "" + result, Toast.LENGTH_LONG).show();
				if (result == TextToSpeech.LANG_MISSING_DATA
						|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
					Toast.makeText(context,
							context.getString(R.string.tts_error) + result,
							Toast.LENGTH_LONG).show();
				} else {
					textToSpeech.speak(context.getString(R.string.tts_ok),
							TextToSpeech.QUEUE_FLUSH, null);
				}
			}
		}
	}

}
