package com.example.chatclientem
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.serialization.json.Json
import java.io.PrintWriter
import java.lang.Exception
import java.net.Socket
import java.util.*
/**
* MainActivity for chat client.
 * - All ui elements loaded on onCreate and only the element visibility changes
 * - Opens up a socket to the server.
 * - User interact with input username first. The server only accepts unique username and keeps asking till user inputs one.
 * - User is greeted by main chatui elements and can send messages to all users or select a user for private message thought
 *   userlist or directly clicking on users name.
 * - On the actionbar menu user can see a history of all chanmessages (private messages are not included). And a top 10 most active online chatters.
*/
class MainActivity : AppCompatActivity() {
    private lateinit var chatAdapter: ChatRecyclerAdapter
    private lateinit var usersAdapter: UserRecyclerAdapter
    private lateinit var socket: Socket
    private lateinit var input: Scanner
    private lateinit var output: PrintWriter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize userlist and chat recycler views.
        initChatRecyclerView()
        initUserRecyclerView()
        // Disable the send username button till the server socket is ready
        sendUsername.isEnabled = false
        supportActionBar?.hide()
        // Start a thread to open a socket conection
                Thread {
            this.socket = Socket("192.168.1.199", 23)
            this.input = Scanner(socket.getInputStream())
            this.output = PrintWriter(socket.getOutputStream(), true)
            // connnection is ready and sendusername button is active
            runOnUiThread{
                sendUsername.isEnabled = true
            }
            // Open a thread with a while loop to listen incoming messages from server
            Thread(IncomingListener(chatAdapter,usersAdapter, input, this)).start()
        }.start()
        // onclick for sendusername button
        sendUsername.setOnClickListener {
            // username need to be atleast 2 characters long
            if(enterUsername.length() > 2) {
                //Sends the username to the server
                Thread(SendMessage(ChatMessage(Commands.Register, enterUsername.text.toString(), "", "",ChatTime.getChatTime()),output)).start()
                ChatClientData.name = enterUsername.text.toString()
                enterUsername.setText("")
            } else {
                enterUsername.hint = "Username is too short"
            }
        }
        // onckick for send message on main chatui container
        sendButton.setOnClickListener {
            // sets the command type for ChatMessage based on if user have selected a specific user for private message
            // Makes sure user doesnt send empty messages
            if (chatText.length() > 0) {
                val command = if (receiverText.text == "All") {
                    Commands.Say
                } else Commands.Whisper
                // build a chatmessage
                val message = ChatMessage(
                    command,
                    ChatClientData.name,
                    receiverText.text.toString(),
                    "${chatText.text}",
                    ChatTime.getChatTime()
                )
                // open a thread to send to server with users message
                Thread(SendMessage(message, output)).start()
                chatText.setText("")
                chatText.clearFocus()
            } else {
                chatText.hint = "Cant send empty message"
            }
        }
        // listener for userlist button(left of edittext field for messages)
        receiverText.setOnClickListener{
            val message = ChatMessage(Commands.Users, ChatClientData.name, "","",ChatTime.getChatTime())
            Thread(SendMessage(message,output)).start()
        }
        // listener for button ALL in uselist recycler view
        selectAll.setOnClickListener{
            receiverText.text = "All"
            userListUI.visibility = View.INVISIBLE
            chatView.visibility = View.VISIBLE
        }
        // listener for close button on popupcontainer accessed thoutgh actionbar meny
        closePopUpButton.setOnClickListener{
            chatUi.visibility = View.VISIBLE
            popUpContainer.visibility = View.INVISIBLE
        }
        popUpText.canScrollVertically(0)
    }
    // initializer for chatView recycler
    private fun initChatRecyclerView() {
        chatView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            chatAdapter = ChatRecyclerAdapter(this@MainActivity, receiverText)
            adapter = chatAdapter
        }
    }
    // initializer for userList recycler
    private fun initUserRecyclerView() {
        userList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            usersAdapter = UserRecyclerAdapter(this@MainActivity, receiverText)
            adapter = usersAdapter
        }
    }
    // creates the action bar menyu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dropdown_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }
    // clicker on selected item from actionbar menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.history -> run {
                Thread(SendMessage(ChatMessage(Commands.History, "c", "", "",ChatTime.getChatTime()), output)).start()
                true
            }
            R.id.quit -> run {
                Thread(SendMessage(ChatMessage(Commands.Quit, ChatClientData.name, "", "",ChatTime.getChatTime()), output)).start()
                true
            }
            R.id.top -> run {
                Thread(SendMessage(ChatMessage(Commands.Top, "", "", "",ChatTime.getChatTime()), output)).start()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}











