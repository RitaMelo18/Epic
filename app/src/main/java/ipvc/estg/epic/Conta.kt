package ipvc.estg.epic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Conta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conta)
    }

    //Voltar para o Feed
    fun VoltarFeed(view: View) {
        var intent = Intent(this, Home::class.java)
        startActivity(intent)
    }
}