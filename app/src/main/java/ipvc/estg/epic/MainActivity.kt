package ipvc.estg.epic

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import ipvc.estg.epic.api.EndPoints
import ipvc.estg.epic.api.ServiceBuilder
import ipvc.estg.epic.api.utilizador
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.LoginShared)]==true){
                var intent = Intent(this, Home::class.java)
                startActivity(intent)
                Log.d("****SHARED", R.string.Id_LoginUser.toString())
                Log.d("****SHARED", R.string.fotoUser.toString())
                Log.d("****SHARED", R.string.nomeUser.toString())
                //Log.d("****SHARED", "${R.string.nomeUser}")
            }
        }
    }
    //Teste

    //Teste dev

    //Ir para a Página Inicial
    fun IniciarSessao(view: View) {

        val emailInserido = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordInserida = findViewById<EditText>(R.id.editTextTextPassword)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postTest(emailInserido.text.toString())

        var intent = Intent(this, Home::class.java)

        //Validações
        if(emailInserido.text.isNullOrEmpty() || passwordInserida.text.isNullOrEmpty()){

            if(emailInserido.text.isNullOrEmpty() && !passwordInserida.text.isNullOrEmpty()){
                emailInserido.error = getString(R.string.Login_Email)
            }
            if(!emailInserido.text.isNullOrEmpty() && passwordInserida.text.isNullOrEmpty()){
                passwordInserida.error = getString(R.string.Login_Password)
            }
            if(emailInserido.text.isNullOrEmpty() && passwordInserida.text.isNullOrEmpty()){
                emailInserido.error = getString(R.string.Login_Email)
                passwordInserida.error = getString(R.string.Login_Password)
            }
        }

        call.enqueue(object : Callback<utilizador>{
            override fun onResponse(call: Call<utilizador>, response: Response<utilizador>) {
                if (response.isSuccessful){
                    val e: utilizador = response.body()!!

                    //Confirmação login
                    if(emailInserido.text.toString().equals(e.email) && (passwordInserida.text.toString().equals(e.password))){
                        /*Toast.makeText(this@MainActivity, e.email.toString() + "-" + e.password, Toast.LENGTH_SHORT).show()*/
                        startActivity(intent)

                        //Shared Preferences Login
                        val sharedPref: SharedPreferences = getSharedPreferences(
                            getString(R.string.preference_login), Context.MODE_PRIVATE
                        )
                        with(sharedPref.edit()){
                            putBoolean(getString(R.string.LoginShared), true)
                            putString(getString(R.string.nomeUser), "${e.nome}")
                            putInt(getString(R.string.Id_LoginUser), e.id)
                            putString(getString(R.string.fotoUser), "${e.foto_perfil}")
                            commit()
                            Log.d("****SHARED", R.string.Id_LoginUser.toString())
                            Log.d("****SHARED", R.string.fotoUser.toString())
                            Log.d("****SHARED", R.string.nomeUser.toString())
                        }
                        Log.d("****SHARED", R.string.LoginShared.toString())
                    }else if (!(emailInserido.text.toString().equals(e.email) && (passwordInserida.text.toString().equals(e.password)))){

                        Toast.makeText(this@MainActivity, R.string.Error_login, Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onFailure(call: Call<utilizador>, t: Throwable) {
                /* Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()*/
            }
        })

    }

}