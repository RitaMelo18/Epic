package ipvc.estg.epic

import android.content.ContentValues.TAG
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
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        fetchUsers()
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
