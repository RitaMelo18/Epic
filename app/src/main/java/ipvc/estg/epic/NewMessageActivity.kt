package ipvc.estg.epic

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        fetchUsers()
    }

companion object{
    val USER_KEY = "USER_KEY"
    val USER_IMAGE = "USER_IMAGE"
    val USER_UID = "USER_UID"
    val ESTADO = 0
}
    private fun fetchUsers() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_newMessage)
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach{
                    Log.d("NewMassage", it.toString())
                    val user = it.getValue(User::class.java)
                    Log.d("Utilizador final", user.toString())
                        if(user != null){
                            adapter.add(UserItem(user))
                        }

                }
                adapter.setOnItemClickListener{item, view ->
                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLog::class.java)
                    intent.putExtra(USER_KEY, userItem.user.username)
                    intent.putExtra(USER_IMAGE, userItem.user.profileImageUrl)
                    intent.putExtra(USER_UID, userItem.user.uid)
                    startActivity(intent)

                    finish()
                }
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("NewMessage", "Cancelado")
            }
        })

    }
}

class User(val uid: String, val username: String, val profileImageUrl: String){
    constructor(): this ("", "", "")
}


class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.userName).text = user.username


        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.findViewById<CircleImageView>(R.id.userImage))

    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

}
