package ipvc.estg.epic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TreinadorPessoal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treinador_pessoal)
    }

    fun voltarServicos(view: View) {
        var intent = Intent(this, Servicos::class.java)
        startActivity(intent)
    }
}