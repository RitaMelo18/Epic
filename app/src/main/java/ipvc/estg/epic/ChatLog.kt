package ipvc.estg.epic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import ipvc.estg.epic.classes.ChatMessage

class ChatLog : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()
    var toUser:String? = null
    var profileUser:String? = null
    var username:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //GET EXTRAS from intent
        username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        findViewById<TextView>(R.id.textView6).text = username

        profileUser = intent.getStringExtra(NewMessageActivity.USER_IMAGE)
        Picasso.get().load(profileUser).into(findViewById<CircleImageView>(R.id.userImage))

        toUser = intent.getStringExtra(NewMessageActivity.USER_UID)
        listenForMessages()

        //Recycler View
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_Chat)
        recyclerView.adapter = adapter
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        //Dar refresh se detetar uma mensagem nova
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
               val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if(chatMessage != null){
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = Chat.currentUser
                        val foto = currentUser?.profileImageUrl
                        val nome = currentUser?.username
                        adapter.add(ChatToItem(chatMessage.text, foto, nome))
                    }else{
                        adapter.add(ChatFromItem(chatMessage.text, profileUser, username))
                    }

                }

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun sendMessage(view: View) {
        val message = findViewById<EditText>(R.id.editTextMessage).text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        //Mensagem reversa
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(ref.key!!, message, fromId!!, toUser!!, System.currentTimeMillis())

        ref.setValue(chatMessage).addOnSuccessListener {
            Log.d("Aaa", "Mensagem guardada: ${ref.key}")
            findViewById<EditText>(R.id.editTextMessage).text.clear()
            findViewById<RecyclerView>(R.id.recyclerView_Chat).scrollToPosition(adapter.itemCount -1)
        }

        toRef.setValue(chatMessage).addOnSuccessListener {
            Log.d("Aaa", "Mensagem guardada ao contrário: ${ref.key}")
        }
    }
}


//Linha de quem envia
class ChatToItem(val text: String, val foto: String?, val username: String?): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.mensagem).text = text

        //Carregar a imagem
        val target = viewHolder.itemView.findViewById<CircleImageView>(R.id.imagemUtilizador)
        Picasso.get().load(foto).into(target)

        //Username
        viewHolder.itemView.findViewById<TextView>(R.id.utilizador).text = username
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

//Linha de quem recebe
class ChatFromItem(val text: String, val foto: String?, val nome: String?): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.mensagemrecetor).text= text

        //Carregar a imagem
        val target = viewHolder.itemView.findViewById<CircleImageView>(R.id.imagemRecetor)
        Picasso.get().load(foto).into(target)

        //Username
        viewHolder.itemView.findViewById<TextView>(R.id.recetor).text = nome
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}