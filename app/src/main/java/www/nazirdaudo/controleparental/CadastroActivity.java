package www.nazirdaudo.controleparental;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.lang.String;

public class CadastroActivity extends AppCompatActivity {

    private TextView cadastro_nome_usuario, cadastro_numero_cell, cadastro_senha;
    private Button btn_cadastrar;
    private ProgressDialog barradeProgresso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        cadastro_nome_usuario = (TextView) findViewById(R.id.cadastro_nome_usuario);
        cadastro_numero_cell = (TextView) findViewById(R.id.cadastro_numero_cell);
        cadastro_senha = (TextView) findViewById(R.id.cadastro_senha);
        btn_cadastrar = (Button) findViewById(R.id.btn_cadastrar);
        barradeProgresso = new ProgressDialog(this);

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CriarConta();
            }
        });
    }

    private void CriarConta() {

        String nome = cadastro_nome_usuario.getText().toString();
        String cell = cadastro_numero_cell.getText().toString();
        String senha = cadastro_senha.getText().toString();

        if (TextUtils.isEmpty(nome)){
            Toast.makeText(this, "Digite seu nome...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(cell)){
            Toast.makeText(this, "Digite seu numero de celular...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(senha)){
            Toast.makeText(this, "Digite sua senha...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            barradeProgresso.setTitle("Criar conta");
            barradeProgresso.setMessage("Aguarde enquanto verificamos seus dados.");
            barradeProgresso.setCanceledOnTouchOutside(false);
            barradeProgresso.show();

            ValidarNumero(nome, cell, senha);
        }
    }

    private void ValidarNumero(String nome, String cell, String senha) {

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Usuarios").child(cell).exists())){

                    HashMap<String, Object> mapadadosUsuario = new HashMap<>();

                    mapadadosUsuario.put("cell",cell);
                    mapadadosUsuario.put("nome",nome);
                    mapadadosUsuario.put("senha",senha);

                    RootRef.child("Usuarios").child(cell).updateChildren(mapadadosUsuario).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(CadastroActivity.this, "Parabens sua conta foi criada", Toast.LENGTH_SHORT).show();
                                        barradeProgresso.dismiss();

                                        Intent intent = new Intent(CadastroActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        barradeProgresso.dismiss();
                                        Toast.makeText(CadastroActivity.this, "Erro da rede: Tente de novo em breve", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
                else
                {
                    Toast.makeText(CadastroActivity.this, "O numero"+cell+" ja existe", Toast.LENGTH_SHORT).show();
                    barradeProgresso.dismiss();
                    Toast.makeText(CadastroActivity.this, "Tente novamente com outro numero", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CadastroActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

