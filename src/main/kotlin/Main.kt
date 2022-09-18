package ru.netology

fun main() {}

data class Message(
    val userId: Int,
    val messId: Int,
    val text:String,
    val read: Boolean
    )

data class Chat(
    val chatId: Int,
    val userId: Int,
    val title: String = "",
    val unreadMess: Boolean,
    val lastMessId: Int = 0,
    val messages: MutableList<Message> = mutableListOf()
)

object ChatService {
    private var chats = mutableMapOf<Int, Chat>()
    private var chatCounter = 0
    fun clear() {
        chats = mutableMapOf()
        chatCounter = 0
    }

    fun addChat(userId: Int, chat: Chat): Chat{
        chatCounter++
        chats[userId] = chat.copy(chatId = chatCounter)
        return chats[userId] ?: throw ChatNoAddException("Chat not added")
    }

    fun deleteChat(chatId: Int) = chats.remove(chatId) ?: throw ChatNotFoundException("Chat with this ID not found")

    fun getChats() = chats.toList()

    fun getUnreadChatsCount() = chats.asSequence().filter { it.value.unreadMess }.count()

    fun addMessage(mess: Message): Boolean {
        val addMess = chats.getOrElse(mess.userId){addChat(mess.userId, Chat(0, mess.userId, unreadMess = true))}
            .messages.add(mess.copy(messId = chats[mess.userId]!!.lastMessId + 1))
        val newValueId = chats[mess.userId]!!.lastMessId + 1
        chats[mess.userId] = chats[mess.userId]!!.copy(lastMessId = newValueId)
        return addMess
    }

    fun deleteMessage(mess: Message): Boolean {
        val chat = chats[mess.userId] ?: throw ChatNotFoundException("Chat with this ID not found")
        return if(chat.messages.remove(mess)) {
            if(chat.messages.isEmpty()) deleteChat(mess.userId)
            true
            } else throw MessageNotFoundException("Message was not found")
    }

    fun editMessage(userId: Int, messId: Int, text: String) = chats[userId]?.messages?.find { it.messId == messId }?.copy(text = text) ?:
        throw MessageNotFoundException("Message not found")


    fun getMessages(userId: Int, messId: Int, count: Int): List<Message> {
        return chats[userId]?.messages?.asSequence()?.filter { it.messId in messId until messId + count }
            ?.onEach { it.copy(read = true) }?.toList() ?: throw MessageNotFoundException ("Messages not found")
    }
}

class MessageNotFoundException(text: String) : RuntimeException(text)
class ChatNotFoundException(text: String) : RuntimeException(text)
class ChatNoAddException(text: String) : RuntimeException(text)