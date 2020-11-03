package br.com.fiap.mergeam

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {
            message.contains("cidade") -> "Lorem"

            message.contains("sao paulo") || message.contains("sao bernardo do campo") ||
                    message.contains("santo andre") || message.contains("sao caetano") ||
                    message.contains("jundiai") || message.contains("maua") ->
                "Indique o número de atendimento desejado:\n\n[1] Contato com PJ\n[2] Consultar agendamento\n[3] Ouvidoria"

            message.contains("1") -> "Como deseja que o promotor entre em contato:\n\n[1] Whatsapp\n[2] Email\n[3] Ligacao"

            message.contains("2") -> ""
            message.contains("1") -> ""

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm hungry..."
                    2 -> "Pretty good! How about you?"
                    else -> "error"
                }
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "Não entendi..."
                    1 -> "Poderia repetir por favor?"
                    2 -> "Não encontrado"
                    else -> "error"
                }
            }
        }
    }
}
