package www.nazirdaudo.controleparental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.nazirdaudo.controleparental.Modelos.Usuarios;

public class LoginActivity extends AppCompatActivity {

    private Button btn_entrar;
    private EditText login_numero_cell, login_senha;
    private ProgressDialog barradeProgresso;
    private String pai = "Usuarios";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btn_entrar = (Button) findViewById(R.id.btn_entrar);
        login_numero_cell = (EditText) findViewById(R.id.login_numero_cell);
        login_senha = (EditText) findViewById(R.id.login_senha);
        barradeProgresso = new ProgressDialog(this);

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Entrar();
            }
        });


        
    }

    private void Entrar() {
        String cell = login_numero_cell.getText().toString();
        String senha = login_senha.getText().toString();

        if (TextUtils.isEmpty(cell)){
            Toast.makeText(this, "Digite seu numero de celular...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(senha)){
            Toast.makeText(this, "Digite sua senha...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            barradeProgresso.setTitle("Entrar na conta");
            barradeProgresso.setMessage("Aguarde enquanto verificamos seus dados.");
            barradeProgresso.setCanceledOnTouchOutside(false);
            barradeProgresso.show();

            PermitirAcesso(cell, senha);


        }
    }

   private void PermitirAcesso(String cell, String senha) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(pai).child(cell).exists()){

                    Usuarios dadosUsuario = snapshot.child(pai).child(cell).getValue(Usuarios.class);

                    if(dadosUsuario.getCell().equals(cell))
                    {
                        if(dadosUsuario.getSenha().equals(senha)){


                            Intent push = new Intent(LoginActivity.this, FingerActivity.class);
                            startActivity(push);
                        }
                        else
                        {

                            Toast.makeText(LoginActivity.this, "Senha errada", Toast.LENGTH_SHORT).show();
                            barradeProgresso.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Nao existe Nenhuma conta com o numero: "+cell, Toast.LENGTH_SHORT).show();

                    barradeProgresso.dismiss();
                    Toast.makeText(LoginActivity.this, "Crie uma conta para entrar", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}