package net.fofi.app.mobim.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.MobSDK;
import com.mob.imsdk.MobIMCallback;
import com.mob.imsdk.model.IMUser;

import net.fofi.app.R;
import net.fofi.app.api.RetrofitHelper;
import net.fofi.app.mobim.BaseFragment;
import net.fofi.app.mobim.biz.UserManager;
import net.fofi.app.mobim.component.QuickAdapter;
import net.fofi.app.mobim.component.ViewHolder;
import net.fofi.app.mobim.utils.LoadImageUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentUpdateUserAvatar extends BaseFragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_update_user_avatar, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        GridView gridView = (GridView) view.findViewById(R.id.gridView);

        view.findViewById(R.id.ivTitleLeft).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        tvTitle.setText(R.string.title_update_user_avatar);

        //加载数据
        final String currentAvatar = "";
        //模拟的已经上传过的用户头像
        RetrofitHelper retrofitHelper = new RetrofitHelper();
        List<String> list = new ArrayList<String>();

        list.add(retrofitHelper.IP + "lol/Aatrox.png");
        list.add(retrofitHelper.IP + "lol/Ahri.png");
        list.add(retrofitHelper.IP + "lol/Akali.png");
        list.add(retrofitHelper.IP + "lol/Alistar.png");
        list.add(retrofitHelper.IP + "lol/Amumu.png");
        list.add(retrofitHelper.IP + "lol/Anivia.png");
        list.add(retrofitHelper.IP + "lol/Annie.png");
        list.add(retrofitHelper.IP + "lol/Ashe.png");
        list.add(retrofitHelper.IP + "lol/AurelionSol.png");
        list.add(retrofitHelper.IP + "lol/Azir.png");

        list.add(retrofitHelper.IP + "lol/Bard.png");
        list.add(retrofitHelper.IP + "lol/Blitzcrank.png");
        list.add(retrofitHelper.IP + "lol/Brand.png");
        list.add(retrofitHelper.IP + "lol/Braum.png");
        list.add(retrofitHelper.IP + "lol/Caitlyn.png");
        list.add(retrofitHelper.IP + "lol/Camille.png");
        list.add(retrofitHelper.IP + "lol/Cassiopeia.png");
        list.add(retrofitHelper.IP + "lol/Chogath.png");
        list.add(retrofitHelper.IP + "lol/Corki.png");
        list.add(retrofitHelper.IP + "lol/Darius.png");

        list.add(retrofitHelper.IP + "lol/Diana.png");
        list.add(retrofitHelper.IP + "lol/Draven.png");
        list.add(retrofitHelper.IP + "lol/DrMundo.png");
        list.add(retrofitHelper.IP + "lol/Ekko.png");
        list.add(retrofitHelper.IP + "lol/Elise.png");
        list.add(retrofitHelper.IP + "lol/Evelynn.png");
        list.add(retrofitHelper.IP + "lol/Ezreal.png");
        list.add(retrofitHelper.IP + "lol/Fiddlesticks.png");
        list.add(retrofitHelper.IP + "lol/Fiora.png");
        list.add(retrofitHelper.IP + "lol/Fizz.png");


//		list.add(retrofitHelper.IP+"pvpqq/105.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/106.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/107.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/108.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/109.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/110.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/111.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/112.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/113.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/114.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/115.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/116.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/117.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/118.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/119.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/120.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/121.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/123.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/124.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/126.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/127.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/128.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/129.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/130.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/131.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/132.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/133.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/134.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/135.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/136.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/139.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/140.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/141.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/142.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/144.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/146.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/148.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/149.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/150.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/152.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/153.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/154.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/156.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/157.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/162.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/163.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/166.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/167.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/168.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/169.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/170.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/171.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/173.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/174.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/175.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/176.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/177.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/178.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/179.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/180.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/182.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/183.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/184.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/186.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/187.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/189.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/190.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/191.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/192.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/193.jpg");
//
//		list.add(retrofitHelper.IP+"pvpqq/194.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/195.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/196.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/198.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/199.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/501.jpg");
//		list.add(retrofitHelper.IP+"pvpqq/502.jpg");

        final QuickAdapter<String> adapter = new QuickAdapter<String>(getContext(), R.layout.update_user_avatar_item, list) {
            protected void initViews(ViewHolder viewHolder, int position, String item) {
                ImageView ivAvatar = viewHolder.getView(R.id.ivAvatar);
                LoadImageUtils.showAvatar(getContext(), ivAvatar, item, R.drawable.ic_group);
            }
        };
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String avatar = adapter.getItem(position);
                if (TextUtils.isEmpty(avatar)) {
                    return;
                }
                if (currentAvatar.equals(avatar)) {
                    return;
                }
                final IMUser imUser = UserManager.getUser();
                if (imUser == null) {
                    return;
                }
                //更新用户头像
                UserManager.updateUserInfo(imUser.getId(), null, avatar, new MobIMCallback<Boolean>() {
                    public void onSuccess(Boolean aBoolean) {
                        MobSDK.setUser(imUser.getId(), imUser.getNickname(), avatar, null);//更新IM用户信息
                        Intent data = new Intent();
                        data.putExtra("link", avatar);
                        getActivity().setResult(Activity.RESULT_OK, data);
                        getActivity().onBackPressed();
                    }

                    public void onError(int code, String message) {
                        getActivity().onBackPressed();
                    }
                });
            }
        });
        gridView.setAdapter(adapter);
    }
}
