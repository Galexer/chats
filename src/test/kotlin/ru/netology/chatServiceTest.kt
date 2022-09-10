package ru.netology

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import ru.netology.ChatService.addMessage
import ru.netology.ChatService.addChat
import ru.netology.ChatService.deleteChat
import ru.netology.ChatService.deleteMessage
import ru.netology.ChatService.editMessage
import ru.netology.ChatService.getMessages
import ru.netology.ChatService.getChats
import ru.netology.ChatService.getUnreadChatsCount

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun addMassageNoChat() {
        val mess = Message(1, 5, "text", false)
        val result = addMessage(mess)

        assertTrue(result)
    }

    @Test
    fun addMassageToChat() {
        val mess1 = Message(1, 1, "text1", false,)
        val mess2 = Message(1, 2, "text2", false)
        val chat = Chat(1, 1, "good chat", true, 2, mutableListOf(mess1, mess2))
        addChat(1, chat)
        val mess = Message(1, 2, "text3", false)
        val result = addMessage(mess)

        assertTrue(result)
    }

    @Test
    fun addChat() {
        val chat = Chat(5, 1, "good chat", true)
        val result = addChat(1, chat)

        assertEquals(1, result.chatId)
    }

    @Test
    fun deleteChat() {
        val chat = Chat(5, 1, "good chat", true)
        addChat(1, chat)
        val result = deleteChat(1)

        assertEquals("good chat", result.title)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteChatException() {
        deleteChat(5)
    }

    @Test
    fun deleteMessage() {
        val mess1 = Message(1, 1, "text1", false,)
        val mess2 = Message(1, 2, "text2", false)
        val chat = Chat(1, 1, "good chat", true, 2, mutableListOf(mess1, mess2))
        addChat(1, chat)

        val result = deleteMessage(mess1)
        assertTrue(result)
    }

    @Test(expected = ChatNotFoundException::class)
    fun deleteMessageChatException() {
        val mess1 = Message(1, 1, "text1", false)
        deleteMessage(mess1)
    }

    @Test(expected = MessageNotFoundException::class)
    fun deleteMessageMessException() {
        val mess2 = Message(1, 2, "text2", false)
        val chat = Chat(1, 1, "good chat", true, 2, mutableListOf(mess2))
        addChat(1, chat)
        val mess1 = Message(1, 1, "text1", false)
        deleteMessage(mess1)
    }

    @Test
    fun editMessage() {
        val mess2 = Message(1, 2, "text2", false)
        val chat = Chat(1, 1, "good chat", true, 2, mutableListOf(mess2))
        addChat(1, chat)

        val result = editMessage(1, 2, "another text")
        assertEquals("another text", result.text)
    }

    @Test(expected = MessageNotFoundException::class)
    fun editMessageException() {
        val mess2 = Message(1, 2, "text2", false)
        val chat = Chat(1, 1, "good chat", true, 2, mutableListOf(mess2))
        addChat(1, chat)

        editMessage(0, 2, "another text")
    }

    @Test
    fun getMessages() {
        val mess1 = Message(1, 1, "text1", false)
        val mess2 = Message(1, 2, "text2", false)
        val mess3 = Message(1, 3, "text3", false)
        val chat = Chat(1, 1, "good chat", true, 2, mutableListOf(mess1, mess2, mess3))
        addChat(1, chat)
        val list = listOf(mess2, mess3)

        val result = getMessages(1, 2, 2)
        assertEquals(list, result)
    }

    @Test(expected = MessageNotFoundException::class)
    fun getMessagesException() {
        getMessages(1, 2, 2)
    }

    @Test
    fun getChats() {
        val chat = Chat(5, 1, "good chat", true)
        addChat(1, chat)
        val chat2 = Chat(5, 2, "chat", true)
        addChat(2, chat2)

        val result = ChatService.getChats()
        assertEquals(2, result.size)
    }

    @Test
    fun getUnreadChatsCount() {
        val chat = Chat(5, 1, "good chat", true)
        addChat(1, chat)
        val chat2 = Chat(5, 2, "chat", true)
        addChat(2, chat2)
        val chat3 = Chat(5, 3, "chat3", false)
        addChat(3, chat3)

        val result = ChatService.getUnreadChatsCount()
        assertEquals(2, result)
    }
}