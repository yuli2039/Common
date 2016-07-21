package com.yu.picmultiselect.album;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yu.picmultiselect.R;
import com.yu.picmultiselect.listadapter.BaseAdapterHelper;
import com.yu.picmultiselect.listadapter.QuickAdapter;

import java.util.List;

/**
 * 选择相册
 * 
 * @author yu
 *
 */
public class BucketActivity extends Activity {

	private ListView lv;
	private QuickAdapter<ImageBucket> adapter;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_album);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		setFinishOnTouchOutside(false);

		lv = (ListView) findViewById(R.id.lv);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(BucketActivity.this, BucketDetailActivity.class);
				intent.putExtra(BucketDetailActivity.KEY_ALBUM_DATA,
						adapter.getData().get(position));
				startActivity(intent);
				finish();
			}
		});

		new AlbumLoader().execute();

	}

	/**
	 * 返回按钮
	 */
	public void btnBack(View view) {
		this.onBackPressed();
	}

	/**
	 * 加载图库的任务类
	 * 
	 * @author yu
	 *
	 */
	private class AlbumLoader extends AsyncTask<Void, Void, List<ImageBucket>> {

		@Override
		protected void onPreExecute() {
			progressDialog.show();
		}

		@Override
		protected List<ImageBucket> doInBackground(Void... params) {
			// 获取所有相册
			return AlbumHelper.getInstance().init(BucketActivity.this).getImagesBucketList(false);
		}

		@Override
		protected void onPostExecute(List<ImageBucket> result) {

			adapter = new QuickAdapter<ImageBucket>(BucketActivity.this,
					R.layout.item_album_floder, result) {

				@Override
				protected void convert(BaseAdapterHelper helper, ImageBucket item) {
					// 默认设置第一张图为相册封面
					ImageItem imageItem = item.imageList.get(0);

					helper.setImageUrl(R.id.imageView, TextUtils.isEmpty(imageItem.thumbnailPath)
							? imageItem.srcPath : imageItem.thumbnailPath);

					helper.setText(R.id.textview, item.bucketName);

				}
			};
			lv.setAdapter(adapter);

			progressDialog.dismiss();
		}

	}

}
