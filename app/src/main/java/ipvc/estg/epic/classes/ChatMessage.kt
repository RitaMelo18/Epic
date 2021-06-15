package ipvc.estg.epic.classes

class ChatMessage(val id: String,val text: String, val fromId: String, val toId: String, val timestamp: Long, val estado: Int){
    constructor(): this("", "", "","",-1, 1)
}
