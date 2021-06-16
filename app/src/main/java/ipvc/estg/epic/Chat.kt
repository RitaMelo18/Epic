package ipvc.estg.epic

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView
import ipvc.estg.epic.NewMessageActivity.Companion.USER_IMAGE
import ipvc.estg.epic.NewMessageActivity.Companion.USER_KEY
import ipvc.estg.epic.NewMessageActivity.Companion.USER_UID
import ipvc.estg.epic.classes.ChatMessage



class Chat : AppCompatActivity() {
    companion object{
        var currentUser: User? = null
    }
    val adapter = GroupAdapter<GroupieViewHolder>()

    val latestMessageMap = HashMap<String, ChatMessage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        findViewById<RecyclerView>(R.id.recycler_latest_message).adapter = adapter
        findViewById<RecyclerView>(R.id.recycler_latest_message).addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        fetchCurrentUser()
        //Click em cada item da recycler
        adapter.setOnItemClickListener{item, view ->
            val intent = Intent(this, ChatLog::class.java)

            val row = item as LatestMessageRow
            intent.putExtra(USER_KEY,row.chatPartnerUser?.username)
            intent.putExtra(USER_IMAGE, row.chatPartnerUser?.profileImageUrl)
            intent.putExtra(USER_UID, row.chatPartnerUser?.uid)

            val userPattern = row.chatPartnerUser?.uid
            val user = FirebaseAuth.getInstance().uid

            //Dar update ao estado on-click desaparece a notificação
            val mDatabase = FirebaseDatabase.getInstance().getReference("latest-messages").child("$user").child("$userPattern")

            mDatabase.child("estado").setValue(1)



            startActivity(intent)
        }


        listnerForLatestMessages()
    }

    private fun refreshRecyclerViewMessages(){
        adapter.clear() //Limpar o recycler
        latestMessageMap.values.forEach{ //Voltar a preenchela
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listnerForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                 val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[snapshot.key!!] = chatMessage
                refreshRecyclerViewMessages()



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

    class LatestMessageRow(val chat: ChatMessage): Item<GroupieViewHolder>(){
        var chatPartnerUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.latestMessage).text = chat.text

        val chatPartnerId : String
        if(chat.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chat.toId
        }else{
            chatPartnerId = chat.fromId
        }

        if(chat.estado == 0){  //Está por abrir
            viewHolder.itemView.findViewById<ImageButton>(R.id.notificacao).visibility = View.VISIBLE
        }else if(chat.estado == 1 ){
            viewHolder.itemView.findViewById<ImageButton>(R.id.notificacao).visibility = View.INVISIBLE
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatPartnerUser = snapshot.getValue(User::class.java)

                    viewHolder.itemView.findViewById<TextView>(R.id.textView13).text = chatPartnerUser?.username
                    val targetImageView = viewHolder.itemView.findViewById<CircleImageView>(R.id.imageView3)
                    Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

}

    fun addPerson(view: View) {
        var intent = Intent(this, NewMessageActivity::class.java)
        startActivity(intent)

        //Obter os dados do utilizador logado
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun back(view: View) {
        finish()
    }

    fun grupo(view: View) {
        val intent = Intent(this, Grupo::class.java)
        startActivity(intent)
    }
}
