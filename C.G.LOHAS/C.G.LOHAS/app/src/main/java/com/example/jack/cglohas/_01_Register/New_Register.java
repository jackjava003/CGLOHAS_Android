package com.example.jack.cglohas._01_Register;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.lib.recaptcha.ReCaptcha;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.InitFragmentActivity;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._00_GlobalService.RegularExpressionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by JACK on 2016/10/10.
 */

public class New_Register extends AppCompatActivity implements View.OnClickListener, ReCaptcha.OnShowChallengeListener, ReCaptcha.OnVerifyAnswerListener {
    private static final String PUBLIC_KEY = "6LcLfQcUAAAAACCrN4x3uNDWUi-zoiJLyco9BnfN";
    private static final String PRIVATE_KEY = "6LcLfQcUAAAAAJTAPcEeMLMfDxK3cPJoNjcFoa_x";
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int PICK_IMAGE_REQUEST = 1;

    private ReCaptcha reCaptcha;
    private ProgressBar progress;
    private EditText answer, account, password, name, cellphone, email, birthDate;
    private RadioGroup rg;
    private Button takePhoto,Select,verify;
    private String sex;
    register_bean rb = new register_bean();
    private File out;
    private Bitmap picture;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date date2 = null;
    java.sql.Date sqdate = null;
    private int acCorrectCount = 0 , pwCorrectCount = 0, gdCorrectCount=0, nmCorrectCount = 0, phCorrectCount=0, emCorrectCount=0, bdCorrectCount=0, photoCorrectCount=1;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Register");
        this.setContentView(R.layout.new_register);
        findView();


        this.findViewById(R.id.verify).setOnClickListener(this);
        this.findViewById(R.id.reload).setOnClickListener(this);

        this.showChallenge();
    }

    private void findView() {
        this.reCaptcha = (ReCaptcha) this.findViewById(R.id.recaptcha);
        this.progress = (ProgressBar) this.findViewById(R.id.progress);
        this.answer = (EditText) this.findViewById(R.id.answer);
        account = (EditText) findViewById(R.id.Account);
        password = (EditText) findViewById(R.id.Password);
        rg = (RadioGroup) findViewById(R.id.Gender);
        name = (EditText) findViewById(R.id.Name);
        cellphone = (EditText) findViewById(R.id.Phone);
        email = (EditText) findViewById(R.id.Email);
        birthDate = (EditText) findViewById(R.id.BirthDate);
        takePhoto = (Button) findViewById(R.id.TakePhoto);
        Select = (Button)findViewById(R.id.Select);
        verify = (Button)findViewById(R.id.verify);
        final TextView accountErr = (TextView) findViewById(R.id.errorAccount);
        final TextView pwErr = (TextView) findViewById(R.id.errorPassword);
        final TextView nameErr = (TextView) findViewById(R.id.errorName);
        final TextView cellPhoneErr = (TextView) findViewById(R.id.errorPhone);
        final TextView emailErr = (TextView) findViewById(R.id.errorEmail);
        final TextView bdErr = (TextView) findViewById(R.id.errorBirthDate);
        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String accountStr = account.getText().toString();
                    if (accountStr == null || accountStr.trim().length() == 0) {
                        accountErr.setText(" 【Account must enter】");
                        acCorrectCount=0;
                    } else if (RegularExpressionUtil.isAcValid(accountStr) == false) {
                        accountErr.setText(" 【Account can not contain symbols and must between 8 and 16 characters in English and numerals】");
                        acCorrectCount=0;
                    }else if (checkUser(accountStr,"Account")==false){
                        accountErr.setText(" 【This account already exists, please change account】");
                        acCorrectCount=0;
                    }else{
                        rb.setAccount(accountStr);
                        accountErr.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.OKAc)).setVisibility(View.VISIBLE);
                        acCorrectCount=1;
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String pwStr = password.getText().toString();
                    if (pwStr == null || pwStr.trim().length() == 0) {
                        pwErr.setText(" 【Password must enter】");
                        pwCorrectCount=0;
                    } else if (RegularExpressionUtil.isPwValid(pwStr) == false) {
                        pwErr.setText(" 【Password can not contain symbols and must between 8 and 16 characters in English and numerals】");
                        pwCorrectCount=0;
                    } else {
                        rb.setPassword(pwStr);
                        pwErr.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.OKPw)).setVisibility(View.VISIBLE);
                        pwCorrectCount=1;
                    }
                }
            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ((TextView) findViewById(R.id.errorGender)).setVisibility(View.GONE);
                ((ImageView) findViewById(R.id.OKGender)).setVisibility(View.VISIBLE);
                switch (checkedId) {
                    case R.id.MaleButton:
                        sex = "boy";
                        rb.setSex(sex);
                        gdCorrectCount=1;
                        break;
                    case R.id.FemaleButton:
                        sex = "girl";
                        rb.setSex(sex);
                        gdCorrectCount=1;
                        break;
                }
            }
        });

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String nmStr = name.getText().toString();
                    if (nmStr == null || nmStr.trim().length() == 0) {
                        nameErr.setText(" 【Name must enter】");
                        nmCorrectCount=0;
                    } else if (nmStr.length() > 8) {
                        nameErr.setText(" 【Name must be less than 8 characters】");
                        nmCorrectCount=0;
                    } else {
                        rb.setName(nmStr);
                        nameErr.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.OKName)).setVisibility(View.VISIBLE);
                        nmCorrectCount=1;
                    }
                }
            }
        });

        cellphone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String phoneStr = cellphone.getText().toString();
                    if (phoneStr == null || phoneStr.trim().length() == 0) {
                        cellPhoneErr.setText(" 【Mobile must enter】");
                        phCorrectCount=0;
                    } else if (RegularExpressionUtil.isPhoneValid(phoneStr) == false) {
                        cellPhoneErr.setText(" 【Please confirm your Mobile number】");
                        phCorrectCount=0;
                    }else if (checkUser(phoneStr,"Phone")==false){
                        cellPhoneErr.setText("【This Mobile already exists, please change Mobile】");
                        phCorrectCount=0;
                    }else{
                        rb.setCellphone(phoneStr);
                        cellPhoneErr.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.OKPhone)).setVisibility(View.VISIBLE);
                        phCorrectCount=1;
                    }
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String emailStr = email.getText().toString();
                    if (emailStr == null || emailStr.trim().length() == 0) {
                        emailErr.setText(" 【E-mail must enter】");
                        emCorrectCount=0;
                    } else if (RegularExpressionUtil.isEmailValid(emailStr) == false) {
                        emailErr.setText(" 【Please confirm your E-mail】");
                        emCorrectCount=0;
                    }else if (checkUser(emailStr,"Email")==false){
                        emailErr.setText("【This E-mail already exists, please change E-mail】");
                        emCorrectCount=0;
                    }else{
                        rb.setEmail(emailStr);
                        emailErr.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.OKEmail)).setVisibility(View.VISIBLE);
                        emCorrectCount=1;
                    }
                }
            }
        });

        birthDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    boolean checkBD = false;
                    String birthDateStr = birthDate.getText().toString();
                    if (birthDateStr == null || birthDateStr.trim().length() == 0) {
                        bdErr.setText(" 【Birthday must enter】");
                        checkBD = false;
                        bdCorrectCount=0;
                    } else {
                        try {
                            sdf.setLenient(false);
                            date2 = sdf.parse(birthDateStr);
                            System.out.println(date2);
                            sqdate = new java.sql.Date(date2.getTime());
                            checkBD = true;
                        } catch (ParseException e) {
                            bdErr.setText("【Birthday format error, should yyyy-MM-dd】");
                            checkBD = false;
                            bdCorrectCount=0;
                        }
                    }
                    if (checkBD == true) {
                        rb.setBirthdate1(sqdate.toString());
                        //Log.e("BBBBBBBBBBBBBDD",sqdate.toString());
                        bdErr.setVisibility(View.GONE);
                        ((ImageView) findViewById(R.id.OKBirthDate)).setVisibility(View.VISIBLE);
                        bdCorrectCount=1;
                    }
                }
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存檔路徑
                out = Environment.getExternalStorageDirectory();
                out = new File(out, "photo.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
                if (isIntentAvailable(New_Register.this, intent)) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    showToast(New_Register.this, R.string.msg_NoCameraApp);
                }
            }
        });

        Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 手機拍照App拍照完成後可以取得照片圖檔
                case REQ_TAKE_PICTURE:
                    // picture = (Bitmap) data.getExtras().get("data"); //只取得小圖
                    picture = downSize(out.getPath());
                    ((ImageView)findViewById(R.id.Picture)).setImageBitmap(picture);
                    break;

                case PICK_IMAGE_REQUEST:
                    try {
                        picture = MediaStore.Images.Media.getBitmap(getContentResolver(),  data.getData());
                        ((ImageView)findViewById(R.id.Picture)).setImageBitmap(picture);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private Bitmap downSize(String path) {
        Bitmap picture = BitmapFactory.decodeFile(path);
        int scaleSize = 1024;
        int longer = Math.max(picture.getWidth(), picture.getHeight());
        if (longer > scaleSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            // 若原始照片寬度無法整除width，則inSampleSize + 1，
            // 若則inSampleSize = 3，實際縮圖時為2，參看javadoc
            options.inSampleSize = longer % scaleSize == 0 ?
                    longer / scaleSize : longer / scaleSize + 1;
            picture = BitmapFactory.decodeFile(out.getPath(), options);
            System.gc();
        }
        return picture;
    }

    private void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.verify:
                verify.setClickable(false);
                this.verifyAnswer();

                break;

            case R.id.reload:
                this.showChallenge();

                break;
        }
    }

    @Override
    public void onChallengeShown(final boolean shown) {
        this.progress.setVisibility(View.GONE);

        if (shown) {
            // If a CAPTCHA is shown successfully, displays it for the user to enter the words
            this.reCaptcha.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.show_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAnswerVerified(final boolean success) {
        if (success) {

            if(rb.getSex()==null || rb.getSex().trim().length()==0){
                ((TextView) findViewById(R.id.errorGender)).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.errorGender)).setText("【Gender must select】");
                ((ImageView) findViewById(R.id.OKGender)).setVisibility(View.GONE);
                verify.setClickable(true);
                return;
            }else if ((acCorrectCount+pwCorrectCount+gdCorrectCount+nmCorrectCount+phCorrectCount+emCorrectCount+bdCorrectCount+photoCorrectCount)!=8){
                Toast.makeText(this, R.string.checkData, Toast.LENGTH_SHORT).show();
                verify.setClickable(true);
                return;
            }
            byte[] bArray = null;
            if(picture!=null && picture.getByteCount()!=0){
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                picture.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bArray = bos.toByteArray();
            }
            int userID = insertMember(rb,bArray);
            if(userID!=0){
                Common.login = true;
                rb.setUserID(userID);
                Common.rb = rb;
                Toast.makeText(this, R.string.registSucess, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(New_Register.this, InitFragmentActivity.class);
                startActivity(intent);
                return;
            }else{
              Toast.makeText(this, R.string.errorHappened, Toast.LENGTH_SHORT).show();
                verify.setClickable(true);
            }

        } else {
            Toast.makeText(this, R.string.verification_failed, Toast.LENGTH_SHORT).show();
            verify.setClickable(true);
        }

        // (Optional) Shows the next CAPTCHA
        this.showChallenge();
    }

    private void showChallenge() {
        // Displays a progress bar while downloading CAPTCHA
        this.progress.setVisibility(View.VISIBLE);
        this.reCaptcha.setVisibility(View.GONE);


        //this.reCaptcha.setLanguageCode("en");
        this.reCaptcha.showChallengeAsync(New_Register.PUBLIC_KEY, this);
    }

    private void verifyAnswer() {
        if (TextUtils.isEmpty(this.answer.getText())) {
            verify.setClickable(true);
            Toast.makeText(this, R.string.instruction, Toast.LENGTH_SHORT).show();
        } else {
            // Displays a progress bar while submitting the answer for verification
            this.progress.setVisibility(View.VISIBLE);
            this.reCaptcha.verifyAnswerAsync(New_Register.PRIVATE_KEY, this.answer.getText().toString(), this);
        }
    }

    private boolean checkUser(String data,String checkType) {
        boolean result = false;
        if (Common.networkConnected(New_Register.this)) {
            String url = Common.URL + "AD_"+checkType+"Servlet";
            try {
                String checkResult= new RegisterResultTask().execute(url,data).get();
                if (checkResult.equals("OK")) {
                    result = true;
                } else {
                }
            } catch (Exception e) {
                Log.e("LOGIN ERROR", e.toString());
            }
        } else {
            Common.showToast(New_Register.this, R.string.msg_NoNetwork);
        }
        return result;
    }

    private int insertMember(register_bean rb, byte[] bArray) {
        int userID = 0;
        if (Common.networkConnected(New_Register.this)) {
            String url = Common.URL + "AD_InsertMemberServlet";
            try {
                String checkResult= new InsertMemberTask().execute(url,rb,bArray).get();
                if (checkResult.equals("NG")==false) {
                    userID = Integer.parseInt(checkResult);
                } else {
                }
            } catch (Exception e) {
                Log.e("LOGIN ERROR", e.toString());
            }
        } else {
            Common.showToast(New_Register.this, R.string.msg_NoNetwork);
        }
        return userID;
    }

}
