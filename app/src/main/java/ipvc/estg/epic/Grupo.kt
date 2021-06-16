package ipvc.estg.epic

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

class Grupo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grupo)
    }

    fun competicao(view: View) {}
    fun back(view: View) {
        finish()
    }
    fun sendMessage(view: View) {}
    fun vencedor(view: View) {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(this)
        val inflater: LayoutInflater = LayoutInflater.from(applicationContext)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.vencedor, null))
            .setNegativeButton("Ok",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
        builder.create()
        builder.show()
    }
}