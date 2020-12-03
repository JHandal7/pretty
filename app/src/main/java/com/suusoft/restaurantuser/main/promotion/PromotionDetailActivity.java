package com.suusoft.restaurantuser.main.promotion;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suusoft.restaurantuser.R;
import com.suusoft.restaurantuser.base.view.BaseActivity;
import com.suusoft.restaurantuser.base.view.SingleAdapter;
import com.suusoft.restaurantuser.configs.Constant;
import com.suusoft.restaurantuser.model.Comment;
import com.suusoft.restaurantuser.model.Promotion;
import com.suusoft.restaurantuser.modelmanager.RequestManager;
import com.suusoft.restaurantuser.network.ApiResponse;
import com.suusoft.restaurantuser.network.BaseRequest;
import com.suusoft.restaurantuser.util.AppUtil;
import com.suusoft.restaurantuser.util.ImageUtil;
import com.suusoft.restaurantuser.util.StringUtil;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoBold;
import com.suusoft.restaurantuser.widgets.textview.TextViewLatoRegular;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Suusoft on 10/08/2017.
 */

public class PromotionDetailActivity extends BaseActivity implements View.OnClickListener {
    private ImageView imgPromotion, imgFavourite;
    private FrameLayout btnBack;
    private TextView tvTitle;
    private TextViewLatoBold tvDiscount, tvFavourite, tvComment;
    private TextView tvTime;
    private TextViewLatoRegular tvDescription;
    private RecyclerView rcvData;
    private EditText edtComment;
    private LinearLayout root_share;
    private ImageView imgSend;


    private LinearLayoutManager linearLayoutManager;
    private SingleAdapter adapterComment;
    private ArrayList<Comment> listComments;

    private Promotion promotion;

    @Override
    protected int getLayoutInflate() {
        return R.layout.activity_promotion_detail;
    }

    @Override
    protected void initView() {
        promotion = getIntent().getExtras().getParcelable(Constant.KEY_PROMOTION);
        initUI();
    }

    @Override
    protected void onViewCreated() {
        initControl();
        bindData(promotion);
        setUpRecyclerView();
    }

    @Override
    protected ToolbarType getToolbarType() {
        return ToolbarType.NONE;
    }

    @Override
    protected void onPrepareCreateView() {

    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) {
            finish();
        } else if (view == imgFavourite) {
            favourite();
        } else if (view == root_share) {
            share();
        } else if (view == imgSend) {
            sendComment();
        }
    }

    private void initUI() {
        imgPromotion = (ImageView) findViewById(R.id.img_promotion);
        imgFavourite = (ImageView) findViewById(R.id.img_favourite);
        btnBack = (FrameLayout) findViewById(R.id.btn_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDiscount = (TextViewLatoBold) findViewById(R.id.tv_discount);
        tvFavourite = (TextViewLatoBold) findViewById(R.id.tv_favourite);
        tvComment = (TextViewLatoBold) findViewById(R.id.tv_comment);
        tvDescription = (TextViewLatoRegular) findViewById(R.id.tv_description);
        edtComment = (EditText) findViewById(R.id.edt_comment);
        root_share = (LinearLayout) findViewById(R.id.root_share);
        rcvData = (RecyclerView) findViewById(R.id.rcv_data);
        imgSend = (ImageView) findViewById(R.id.img_send);
        tvTime = (TextView) findViewById(R.id.tv_time);
    }

    private void initControl() {
        btnBack.setOnClickListener(this);
        imgFavourite.setOnClickListener(this);
        root_share.setOnClickListener(this);
        edtComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND) {
                    String comment = edtComment.getText().toString().trim();
                    if (StringUtil.isEmpty(comment)) {
                        AppUtil.showToast(getApplicationContext(), R.string.msg_required_comment);
                        return true;
                    }
                    addComment(comment);
                }
                return false;
            }
        });
        imgSend.setOnClickListener(this);
    }

    private void sendComment() {
        String comment = edtComment.getText().toString().trim();
        if (StringUtil.isEmpty(comment) || comment.equals("")) {
            AppUtil.showToast(getApplicationContext(), R.string.msg_required_comment);
        }else {
            addComment(comment);
            edtComment.setText("");
        }

    }

    private void bindData(Promotion promotion) {
        ImageUtil.setImage(imgPromotion, promotion.getImage());
        tvTitle.setSelected(true);
        tvTitle.setText(promotion.getName());
        tvComment.setText(String.valueOf(promotion.getNumComment()));
        tvFavourite.setText(String.valueOf(promotion.getFavourite()));
        tvDiscount.setText(promotion.getName());
        tvDescription.setText(promotion.getDescription());
        if (!promotion.getIs_favourite()) {
            imgFavourite.setImageResource(R.drawable.ic_dislike);
        } else {
            imgFavourite.setImageResource(R.drawable.ic_like);
        }
        tvTime.setText(promotion.getTimeComment());
    }

    private void setUpRecyclerView() {
        listComments = new ArrayList<>();
        adapterComment = new SingleAdapter(this, R.layout.item_comment, listComments, ItemCommentVM.class);
        linearLayoutManager = new LinearLayoutManager(this);
        rcvData.setLayoutManager(linearLayoutManager);
        rcvData.setAdapter(adapterComment);
        rcvData.setNestedScrollingEnabled(false);
        rcvData.setFocusable(false);
        getComment();
    }

    private void addComment(String comment) {
        RequestManager.addComment(promotion.getId(), comment, new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    AppUtil.showToast(getApplicationContext(), R.string.msg_comment_success);
                    listComments.clear();
                    adapterComment.notifyDataSetChanged();
                    getComment();
                }
            }

            @Override
            public void onError(String message) {

            }
        });

    }

    private void getComment() {
        RequestManager.getListCommentByPromotion(promotion.getId(), new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    ArrayList<Comment> arrComments = (ArrayList<Comment>) response.getDataList(Comment.class);
                    if (listComments != null && listComments.isEmpty()) {
                        listComments.addAll(arrComments);
                        adapterComment.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void favourite() {
        RequestManager.favouritePromotion(promotion.getId(), new BaseRequest.CompleteListener() {
            @Override
            public void onSuccess(ApiResponse response) {
                if (!response.isError()) {
                    try {
                        int count = response.getDataObject().getInt("count_favourite");
                        int is_favourite = response.getDataObject().getInt("is_favourite");
                        if (is_favourite == 0) {
                            imgFavourite.setImageResource(R.drawable.ic_dislike);
                        } else if (is_favourite == 1) {
                            imgFavourite.setImageResource(R.drawable.ic_like);
                        }
                        tvFavourite.setText(String.valueOf(count));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void share() {
        AppUtil.sendEmail(this, promotion.getDescription());
    }
}
