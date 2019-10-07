package com.example.chatclientem
import android.app.Activity
import android.content.BroadcastReceiver
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_user.view.*
/**
 *  Adapater for userlist. Populates the view with clickable buttons of users.
 *  also shows a toas fo clickef user
 * */
class UserRecyclerAdapter(private val context: Activity, private val receiver: TextView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return UsersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is UsersViewHolder -> {
                holder.bind(ChatClientData.users.toList()[position])
                holder.itemView.userItem.setOnClickListener{
                    receiver.text = holder.itemView.userItem.text
                    val toast = Toast.makeText(context ,"private message to: "  +holder.itemView.userItem.text, Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.CENTER, 0 , 0)
                    toast.show()
                    context.userListUI.visibility = View.INVISIBLE
                    context.chatView.visibility = View.VISIBLE
                    ChatClientData.users.clear()
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return ChatClientData.users.size
    }
    class UsersViewHolder(usersList: View): RecyclerView.ViewHolder(usersList) {
        private val username = usersList.userItem

        fun bind(user: String){
            username.text = user

        }

    }



}