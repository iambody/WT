package io.vtown.WeiTangApp.comment.view.pop;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.pop.PHomeSelect.SeleckClickListener;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-12 下午5:27:35 分享时候调用的代码
 */
public class PShare extends PopupWindow implements OnClickListener {
	/**
	 * 上下文
	 */
	private Context pContext;

	/**
	 * 基view
	 */
	private View BaseView;
	/**
	 * view
	 */
	private View pop_share_haoyou, pop_share_pyq;
	private TextView pop_share_cancle;
	/**
	 * 回掉接口
	 * 
	 * @param pContext
	 */
	private SeleckClickListener mClickListener;

	/**
	 * 保存分享信息的实体类
	 * 
	 * @param pContext
	 */
	private BNew ShareBeanNew;

	public boolean IsErWeiMaShare = false;

	public PShare(Context pContext, BNew sharebeanNew) {
		super();
		this.pContext = pContext;

		BaseView = LayoutInflater.from(pContext).inflate(R.layout.pop_share,
				null);
		this.ShareBeanNew = sharebeanNew;
		if (null == ShareBeanNew) {
			this.dismiss();
//			PromptManager.ShowCustomToast(pContext, "请传入BNew实体类(后台是否存在?)");
			return;
		}
		IPop();
		IView();
		ShareSDK.initSDK(pContext);
	}

	private void IView() {
		pop_share_haoyou = ViewHolder.get(BaseView, R.id.pop_share_haoyou);
		pop_share_pyq = ViewHolder.get(BaseView, R.id.pop_share_pyq);
		pop_share_cancle = ViewHolder.get(BaseView, R.id.pop_share_cancle);
		pop_share_cancle.setOnClickListener(this);
		SetCommentIV("朋友圈", R.drawable.sharer_weixin, pop_share_pyq);
		SetCommentIV("微信好友", R.drawable.share_wxpy, pop_share_haoyou);
	}

	/**
	 * 上边是IV下边是文字的布局
	 * 
	 * @param title
	 * @param IvRource
	 * @param V
	 */
	public void SetCommentIV(String title, int IvRource, View V) {

		((ImageView) V.findViewById(R.id.comment_ivtxt_iv))
				.setBackgroundResource(IvRource);
		((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setText(title);
		V.setOnClickListener(this);

	}

	private void IPop() {
		BaseView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(BaseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pop_share_haoyou:
			Share(1);
			PShare.this.dismiss();
			break;
		case R.id.pop_share_pyq:
			Share(2);
			PShare.this.dismiss();
			break;
		case R.id.pop_share_cancle:
			PShare.this.dismiss();
			break;

		default:
			break;
		}
	}

	private void Share(int Type) {
		
		Platform platform = null;
		ShareParams sp = new ShareParams();
		switch (Type) {
		case 1:// 好友分享
			platform = ShareSDK.getPlatform(pContext, Wechat.NAME);
//			sp.setShareType(Platform.SHARE_WEBPAGE);
			if (IsErWeiMaShare) {//二维码=》图片
				sp.setShareType(Platform.SHARE_IMAGE);
			} else {//飞二维码=》网页
				sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
			}
			// sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);
			// sp.setText("大兔兔的测试数据");
			// sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			// sp.setTitle("大兔兔的title");//
			// sp.setUrl("www.baidu.com");

			sp.setText(ShareBeanNew.getShare_content());
			sp.setImageUrl(ShareBeanNew.getShare_log());
			sp.setTitle(ShareBeanNew.getShare_title());//
			sp.setUrl(ShareBeanNew.getShare_url());
			break;
		case 2:// 朋友圈分享
			platform = ShareSDK.getPlatform(pContext, WechatMoments.NAME);
			if (IsErWeiMaShare) {//二维码=》图片
				sp.setShareType(Platform.SHARE_IMAGE);
			} else {//飞二维码=》网页
				sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
			}
			// sp.setText("大兔兔的测试数据");
			// sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			// sp.setTitle("大兔兔的测试数据");//
			// sp.setUrl("www.baidu.com");
			sp.setText(ShareBeanNew.getShare_content());
			sp.setImageUrl(ShareBeanNew.getShare_log());
			sp.setTitle(ShareBeanNew.getShare_title());//
			sp.setUrl(ShareBeanNew.getShare_url());
			break;
		default:
			break;
		}
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				PromptManager.ShowCustomToast(pContext, "分享取消");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				PromptManager.ShowCustomToast(pContext, "分享完成");
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
			}
		});
		platform.share(sp);
	}

	public boolean isIsErWeiMaShare() {
		return IsErWeiMaShare;
	}

	public void setIsErWeiMaShare(boolean isErWeiMaShare) {
		IsErWeiMaShare = isErWeiMaShare;
	}
}
