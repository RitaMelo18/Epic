package ipvc.estg.epic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    //Teste

    //Teste dev

    //Ir para a Página Inicial
    fun IniciarSessao(view: View) {
        var intent = Intent(this, Home::class.java)
        startActivity(intent)
    }
}