package com.qwx.commonlist.demo.list.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.qwx.commonlist.demo.R;
import com.qwx.commonlist.demo.list.repository.PersonBean;
import com.qwx.commonlist.lib.ItemView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by qqin on 2020/8/27
 * <p>
 * email qqin@finbtc.net
 */
public class PersonItemView extends ConstraintLayout implements ItemView<PersonBean> {
    private TextView tvSeq;
    private TextView tvName;
    private TextView tvHobby;
    public PersonItemView(@NonNull Context context) {
        super(context);
    }

    public PersonItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvSeq = findViewById(R.id.tv_seq);
        tvName = findViewById(R.id.tv_name);
        tvHobby = findViewById(R.id.tv_hobby);
    }

    @Override
    public void bindData(PersonBean bean) {
        tvSeq.setText(bean.seq + "");
        tvName.setText(bean.name);
        tvHobby.setText(bean.hobby);
    }
}
