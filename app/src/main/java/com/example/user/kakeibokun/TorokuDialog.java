package com.example.user.kakeibokun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TorokuDialog extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.toroku)
                    .setMessage(R.string.msg1)
                    //ポジティブボタンを設定（支出）
                    .setPositiveButton(
                            R.string.sisyutu,
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(
                                        DialogInterface dialog, int which
                                ){
                                    //支出が押された時の処理（支出入力画面に遷移)
                                    Intent intent = new Intent(getActivity(), Shisyutu.class);
                                    startActivity(intent);
                                }
                            }
                    )
                    //ネガティブボタンを設定（収入）
                    .setNegativeButton(
                            R.string.syunyu,
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(
                                        DialogInterface dialog, int which
                                ){
                                    //収入が押された時の処理（収入入力画面に遷移）
                                    Intent intent = new Intent(getActivity(), Syunyu.class);
                                    startActivity(intent);
                                }
                            }
                    );
        return builder.create();
    }
}
