package com.viethcn.duanandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;


import com.facebook.CallbackManager;

import com.facebook.login.widget.LoginButton;

import com.google.firebase.auth.FirebaseAuth;




public class Login extends AppCompatActivity {
    private TextView txtDangKy, txtQuenMk;
    private EditText edtTenDangNhap, edtMatKhau;
    private Button btnDangNhap;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false); // Thay thế EdgeToEdge
        setContentView(R.layout.activity_login);
        txtDangKy = findViewById(R.id.txtDangky);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        txtQuenMk = findViewById(R.id.txtQuenMk);

        firebaseAuth = FirebaseAuth.getInstance();
        btnDangNhap.setOnClickListener(v -> {
            String tenDangNhap = edtTenDangNhap.getText().toString();
            String matKhau = edtMatKhau.getText().toString();

            if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            firebaseAuth.signInWithEmailAndPassword(tenDangNhap, matKhau).addOnCompleteListener(Login.this, task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Toast.makeText(Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        txtQuenMk.setOnClickListener(v -> showDialogQuenMK());

        txtDangKy.setOnClickListener(v -> startActivity(new Intent(Login.this, Register.class)));

    }

    private void showDialogQuenMK() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText edtUsername = view.findViewById(R.id.edtUserName);
        Button btnSend = view.findViewById(R.id.btnSend);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(v -> alertDialog.dismiss());

        btnSend.setOnClickListener(v -> {
            String email = edtUsername.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Đã gửi email đặt lại mật khẩu. Kiểm tra hộp thư đến của bạn.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Login.this, "Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            });
        });
    }
}
