package com.habitrpg.android.habitica.old.ui.adapter.social;

import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.databinding.ValueBarBinding;
import com.habitrpg.android.habitica.old.events.commands.OpenFullProfileCommand;
import com.habitrpg.android.habitica.old.ui.AvatarView;
import com.habitrpg.android.habitica.old.ui.AvatarWithBarsViewModel;
import com.habitrpg.android.habitica.old.ui.helpers.ViewHelper;
import com.magicmicky.habitrpgwrapper.lib.models.HabitRPGUser;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PartyMemberRecyclerViewAdapter extends RecyclerView.Adapter<PartyMemberRecyclerViewAdapter.MemberViewHolder> {


    public Context context;
    private List<HabitRPGUser> memberList;

    public void setMemberList(List<HabitRPGUser> memberList) {
        this.memberList = memberList;
        this.notifyDataSetChanged();
    }


    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.party_member, parent, false);

        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        holder.bind(memberList.get(position));
    }

    @Override
    public int getItemCount() {
        return memberList == null ? 0 : memberList.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatarView)
        AvatarView avatarView;

        @BindView(R.id.username)
        TextView userName;

        @BindView(R.id.user_lvl)
        TextView lvl;

        @BindView(R.id.class_label)
        TextView classLabel;

        @BindView(R.id.class_background_layout)
        View classBackground;

        ValueBarBinding hpBar;

        Resources resources;

        public MemberViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            View hpBarView = itemView.findViewById(R.id.hpBar);

            hpBar = DataBindingUtil.bind(hpBarView);
            hpBar.setPartyMembers(true);

            resources = itemView.getResources();
        }

        public void bind(HabitRPGUser user) {
            android.content.Context ctx = itemView.getContext();

            avatarView.setUser(user);

            AvatarWithBarsViewModel.setHpBarData(hpBar, user.getStats(), ctx);

            lvl.setText(context.getString(R.string.user_level, user.getStats().getLvl()));

            classLabel.setText(user.getStats().getCleanedClassName());

            int colorResourceID;
            switch (user.getStats()._class) {
                case healer: {
                    colorResourceID = R.color.class_healer;
                    break;
                }
                case warrior: {
                    colorResourceID = R.color.class_warrior;
                    break;
                }
                case rogue: {
                    colorResourceID = R.color.class_rogue;
                    break;
                }
                case wizard: {
                    colorResourceID = R.color.class_wizard;
                    break;
                }
                default:
                    colorResourceID = R.color.task_gray;
            }
            ViewHelper.SetBackgroundTint(classBackground, ContextCompat.getColor(context, colorResourceID));
            userName.setText(user.getProfile().getName());

            itemView.setClickable(true);
            itemView.setOnClickListener(view -> {
                OpenFullProfileCommand cmd = new OpenFullProfileCommand(user.getId());

                EventBus.getDefault().post(cmd);
            });
        }


    }
}
