package com.suusoft.restaurantuser.main.feedback;

import android.content.Context;
import android.view.View;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.vm.BaseViewModel;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.StringUtil;

/**
 * Created by Suusoft on 3/16/17.
 */

public class FeedBackVM extends BaseViewModel {

    public FeedBackVM(Context self) {
        super(self);
    }

    private String name;
    private String email;
    private String subject;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void onClickSendFeedback(View view){
        if (StringUtil.isEmpty(name)) {
            AppUtil.showToast(self, R.string.message_name_empty);
            return;
        }
        if (StringUtil.isEmpty(email)) {
            AppUtil.showToast(self, R.string.message_email_empty);

            return;
        }
        if (!StringUtil.isValidEmail(email)) {
            AppUtil.showToast(self, R.string.message_email_wrong_format);

            return;
        }
        if (StringUtil.isEmpty(subject)) {
            AppUtil.showToast(self, R.string.message_subject_empty);

            return;
        }
        if (StringUtil.isEmpty(content)) {
            AppUtil.showToast(self, R.string.message_content_empty);

            return;
        }

        // TODO: 3/16/17  do something after validate


    }
}
