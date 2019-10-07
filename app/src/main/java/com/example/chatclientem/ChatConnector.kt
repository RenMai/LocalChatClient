package com.example.chatclientem
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
/**
 * ChatConnector takes a incoming ChatMessage and decides action based on command parameter in the message.
 * Also updates ui elements on mainactivity
 * */

class ChatConnector(private val chatMessage: ChatMessage,
                    private val chatRecyclerAdapter: ChatRecyclerAdapter,
                    private val userRecyclerAdapter: UserRecyclerAdapter,
                    private val activity: MainActivity)
{
    fun command() {
        when(chatMessage.command()) {
            // Adds the message to the list
            Commands.Say -> {
                ChatClientData.newMessage(chatMessage)
                activity.runOnUiThread {
                    chatRecyclerAdapter.notifyDataSetChanged()
                    activity.chatView.scrollToPosition(chatRecyclerAdapter.itemCount-1)
                }
            }
            // Adds the message to the list
            Commands.Whisper-> {
                ChatClientData.newMessage(chatMessage)
                Log.d("whisperi","whisper inc" +  chatMessage)
                activity.runOnUiThread{
                    chatRecyclerAdapter.notifyDataSetChanged()
                    activity.chatView.scrollToPosition(chatRecyclerAdapter.itemCount-1)
                }
            }
            // If the response from server is true closes the enter sername ui
            // If false asks for another username
            Commands.Register -> {
                if(chatMessage.toString() == "true"){
                    activity.runOnUiThread {
                        changeUserSelectionUiVisibility()
                        changeChatUiVisibility()
                        activity.supportActionBar?.show()
                    }
                } else if (chatMessage.toString() == "false"){
                    activity.runOnUiThread{activity.enterUsername.hint = "Username already taken"}
                }
            }
            // Takes the userlist string and pushes each into a list in ChatCLientData object
            Commands.Users -> {
                //splits the string at , and removes whitespace
                val splitti = chatMessage.toString().dropWhile { !it.isLetter() }.dropLastWhile { !it.isLetter() }.split(",")
                for (n in splitti){
                    Log.d("users","splitti" +n)
                    if (n.dropWhile { !it.isLetter() }!= ChatClientData.name) {
                        ChatClientData.addUser(n.dropWhile { !it.isLetter() })
                    }
                }
                activity.runOnUiThread{
                    activity.chatView.visibility = View.INVISIBLE
                    activity.userListUI.visibility = View.VISIBLE
                    userRecyclerAdapter.notifyDataSetChanged()
                }
            }
            // prints out a history of chatmessages onto a popup containers textview
            Commands.History -> {
                val history = chatMessage.toString()
                activity.runOnUiThread {
                    changeChatUiVisibility()
                    activity.popUpTitle.text = "Chat History"
                    activity.popUpText.text = history
                    changePopUpSelectionVisibility()
                }
            }
            // prints out top 10 chatters onto popup containers textview
            Commands.Top -> {
                val top = chatMessage.toString()
                activity.runOnUiThread {

                    changeChatUiVisibility()
                    activity.popUpTitle.text = "TOP 10 Chatters"
                    activity.popUpText.text = top
                    changePopUpSelectionVisibility()
                }
            }
                        // exits the app
            Commands.Quit -> {
                ChatClientData.chatMessages.clear()
                activity.finishAffinity()
            }
        }
    }

    // Changes the ui elemts visibility status
    private fun changeUserSelectionUiVisibility(){
        if (activity.usernameUi.visibility == View.VISIBLE) {
            activity.usernameUi.visibility =  View.INVISIBLE
        } else {
            activity.usernameUi.visibility= View.VISIBLE
        }
    }
    private fun changePopUpSelectionVisibility() {
        if (activity.popUpContainer.visibility == View.VISIBLE){
            activity.popUpContainer.visibility = View.INVISIBLE
        }   else {
            activity.popUpContainer.visibility = View.VISIBLE
        }
    }
    private fun changeChatUiVisibility() {
        if (activity.chatUi.visibility == View.VISIBLE){
            activity.chatUi.visibility = View.INVISIBLE
        } else {
            activity.chatUi.visibility = View.VISIBLE
        }
    }
}
